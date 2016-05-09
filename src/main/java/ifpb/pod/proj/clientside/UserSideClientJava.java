package ifpb.pod.proj.clientside;

import ifpb.pod.proj.interfaces.Server;
import ifpb.pod.proj.interfaces.Usuario;
import ifpb.pod.proj.utils.StringCommand;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vmvini on 07/05/16.
 */

//Responsavel por realizar garantia de entrega de mensagem
public class UserSideClientJava {

    private SocketMessages socketMessages;
    private Socket nodejsClient;
    private PendantMessages pendantMessages;

    private Usuario loggedUser;

    public UserSideClientJava(PendantMessages pendantMessages){
        socketMessages = new SocketMessages();
        this.pendantMessages = pendantMessages;
    }




    //serversocket escutar na porta 10889
    //NAO PRECISA DE THREAD POIS ESSE Ã‰ SÃ“ O CLIENTE JAVA. NÃƒO O SERVIDOR
    public void listenConnection(){
        try {
            ServerSocket serverSocket = new ServerSocket(10889);
            while(true){
                nodejsClient = serverSocket.accept();
                System.out.println("nodejs cliente se conectou");
                byte[] b = new byte[1024];
                nodejsClient.getInputStream().read(b);

                String command = new String(b).trim();

                Map<String, String> map = StringCommand.convert(command);
                if (map.get("command").equals("cadastrarUsuario")) {

                    cadastrarUsuario(map);


                } else if (map.get("command").equals("hasUsuario")) {

                    login(map);

                } else if (map.get("command").equals("escreverMensagem")) {

                    escreverMensagem(map);

                } else if (map.get("command").equals("entrarGrupo")) {
                        inscreverGrupo(map);

                }
                else if(map.get("command").equals("sair")){
                    logoff(map);
                }

                nodejsClient.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }


    private void logoff(Map<String, String> map){
        //Usuario usr, String token

        try{
            Server server = lookupRMIServer();
            server.logoff(loggedUser, map.get("token"));

        }
        catch(NotBoundException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
        catch(RemoteException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
    }

    private Server lookupRMIServer() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 10800);
        Server server = (Server) registry.lookup("server");
        return server;
    }


    private String getNotificacao(String tokenId){
        //CONECTAR AO AppDataPublicServer porta 10666
        try {
            Socket socket = new Socket("localhost", 10666);

            //gerar comando
            String command = "getNotificacao?token="+tokenId;

            //enviar comando listarPendentes
            socket.getOutputStream().write(command.getBytes("UTF-8"));

            //escutar resposta
            byte[] b = new byte[1024];
            socket.getInputStream().read(b);
            String resp = new String(b).trim();

            return resp;


        }
        catch(IOException e){
            return null;
        }
    }

    private boolean sendMessagesToNode(String msgs){
        try {
            Socket socket = new Socket("localhost", 3020);

            socket.getOutputStream().write(msgs.getBytes("UTF-8"));

            byte[] b = new byte[1024];
            socket.getInputStream().read(b);
            String resp = new String(b).trim();
            if(resp.equals("SUCCESS"))
                return true;
            return false;
        }
        catch(IOException e){
            return false;
        }


    }

    public boolean sendNotificationToUser(String tokenId){

        String notificacao = getNotificacao(tokenId);

        if(notificacao == null)
            return false;

        return sendMessagesToNode(notificacao);

    }



    private void login(Map<String, String> map){
        try{
            Server server = lookupRMIServer();
            loggedUser = new UserImpl(map.get("email"), map.get("senha"), this);

            System.out.println("email: " + map.get("email"));
            System.out.println("senha: " + map.get("senha"));

            String token = server.login(loggedUser);
            if (token == null) {
                socketMessages.sendMessage(nodejsClient, socketMessages.LOGINFAIL);
                System.out.println("token de login nulo");
            }

            else{
                System.out.println("Token de login: " + token);
                socketMessages.sendMessage(nodejsClient, "token:"+token);
                PendantMessageThread pt = new PendantMessageThread(pendantMessages, this,token,map.get("email"));
                pt.start();
            }
        }
        catch(NotBoundException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
        catch(RemoteException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
    }

    private void cadastrarUsuario(Map<String, String> map){
        try{
            Server server = lookupRMIServer();
            server.cadastrarUsuario(map.get("nome"), map.get("email"), map.get("senha"));
            socketMessages.sendMessage(nodejsClient, socketMessages.USER_REG_SUC);
        }
        catch(NotBoundException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
        catch(RemoteException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
    }


    private void inscreverGrupo(Map<String, String> map){
        try{
            Server server = lookupRMIServer();

            try{
                server.inscreverGrupo(loggedUser, map.get("grupoId"), map.get("sessionToken"));
                socketMessages.sendMessage(nodejsClient, socketMessages.SUCCESS_SIGNUP_GROUP);
            }
            catch(AuthenticationException e){
                socketMessages.sendMessage(nodejsClient, socketMessages.NOT_LOGGED);
            }

        }
        catch(NotBoundException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
        catch(RemoteException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTSERVER);
        }
    }

    public void escreverMensagem(Map<String, String> map){
        try {
            Server server = lookupRMIServer();
            try {
                server.escreverMensagem(map.get("email"), map.get("grupoId"), map.get("conteudo"), map.get("token"));
                pendantMessages.confirmSendedMessage(map);
                socketMessages.sendMessage(nodejsClient, socketMessages.MESSAGE_SENDED);
            }
            catch(AuthenticationException e){
                socketMessages.sendMessage(nodejsClient, socketMessages.NOT_LOGGED);
            }
        }
        catch(NotBoundException e){
            socketMessages.sendMessage(nodejsClient, socketMessages.LOSTMESSAGE);
            pendantMessages.save(map);
            //por enquanto, as mensagens pendentes vao ser enviadas com novas datas em cada tentativa
            //pois nao to salvando a data de envio original

        }
        catch(RemoteException e){
            //Removido por erro quando não foi realizada nenhuma conecção com o socket
            //antes da Thread de mensagens pendente ser executada
          //socketMessages.sendMessage(nodejsClient, socketMessages.LOSTMESSAGE);
            pendantMessages.save(map);
        }

    }









}
