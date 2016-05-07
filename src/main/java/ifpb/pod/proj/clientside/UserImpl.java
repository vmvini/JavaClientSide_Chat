package ifpb.pod.proj.clientside;

import ifpb.pod.proj.interfaces.Usuario;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by vmvini on 07/05/16.
 */
public class UserImpl extends UnicastRemoteObject implements Usuario {

    private String email;
    private String senha;
    private UserSideClientJava clientJava;


    public UserImpl(String email, String senha, UserSideClientJava clientJava) throws RemoteException{
        super();
        this.email = email;
        this.senha = senha;
        this.clientJava = clientJava;
    }

    public String getEmail() throws RemoteException{
        return email;
    }
    public String getSenha() throws RemoteException{
        return senha;
    }

    //str é token de notificação de mensagens pendentes
    public boolean notificar(String str) throws RemoteException{
        return clientJava.sendNotificationToUser(str);

    }
}
