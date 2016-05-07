package ifpb.pod.proj.clientside;

import ifpb.pod.proj.interfaces.Usuario;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by vmvini on 07/05/16.
 */
public class UserImpl implements Usuario {

    private String email;
    private String senha;


    public UserImpl(String email, String senha){
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() throws RemoteException{
        return email;
    }
    public String getSenha() throws RemoteException{
        return senha;
    }

    public boolean notificar(String str) throws RemoteException{
        return false;
    }
}
