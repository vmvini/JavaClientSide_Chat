package ifpb.pod.proj.clientside;

import ifpb.pod.proj.interfaces.Server;
import ifpb.pod.proj.utils.StringCommand;

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

    private Socket nodejsClient;

    //serversocket escutar na porta 10889
    //NAO PRECISA DE THREAD POIS ESSE É SÓ O CLIENTE JAVA. NÃO O SERVIDOR
    public void listenConnection(){
        try {
            ServerSocket serverSocket = new ServerSocket(10889);
            while(true){
                nodejsClient = serverSocket.accept();
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

                nodejsClient.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }


    public Server lookupRMIServer() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 10800);
        Server server = (Server) registry.lookup("server");
        return server;
    }


    private void login(Map<String, String> map){
        try{
            Server server = lookupRMIServer();
        }
        catch(Exception e){
            try {
                nodejsClient.getOutputStream().write("SERVER_CONNECTION_LOST".getBytes("UTF-8"));
            }
            catch(IOException io){
                io.printStackTrace();
            }
        }
    }

    private void cadastrarUsuario(Map<String, String> map){
        //rmiServer.cadastrarUsuario()
        try{
            Server server = lookupRMIServer();
        }
        catch(Exception e){
            try {
                nodejsClient.getOutputStream().write("SERVER_CONNECTION_LOST".getBytes("UTF-8"));
            }
            catch(IOException io){
                io.printStackTrace();
            }
        }
    }


    private void inscreverGrupo(Map<String, String> map){
        //rmiServer.inscre...
        try{
            Server server = lookupRMIServer();
        }
        catch(Exception e){
            try {
                nodejsClient.getOutputStream().write("SERVER_CONNECTION_LOST".getBytes("UTF-8"));
            }
            catch(IOException io){
                io.printStackTrace();
            }
        }
    }

    private void escreverMensagem(Map<String, String> map){
        //rmiServer.escreverM
        try{
            Server server = lookupRMIServer();
        }
        catch(Exception e){
            try {
                nodejsClient.getOutputStream().write("SERVER_CONNECTION_LOST".getBytes("UTF-8"));
            }
            catch(IOException io){
                io.printStackTrace();
            }
        }

    }






}
