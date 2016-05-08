package ifpb.pod.proj.interfaces;

import javax.naming.AuthenticationException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by vmvini on 07/05/16.
 */
public interface Server extends Remote {

    String login(Usuario usr) throws RemoteException;

    String cadastrarUsuario(String nome, String email, String senha) throws RemoteException;

    void inscreverGrupo(Usuario user, String grupoId, String sessionToken) throws RemoteException, AuthenticationException;

    void escreverMensagem(String usrEmail, String grupoId, String conteudo, String sessionToken) throws RemoteException, AuthenticationException;

    void excluirUsuario(Usuario usr, String token) throws RemoteException;

    void logoff(Usuario usr, String token) throws RemoteException;
}
