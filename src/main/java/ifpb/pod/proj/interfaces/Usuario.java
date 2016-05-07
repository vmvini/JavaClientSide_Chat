package ifpb.pod.proj.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by vmvini on 07/05/16.
 */
public interface Usuario extends Remote {
    String getEmail() throws RemoteException;
    String getSenha() throws RemoteException;
    boolean notificar(String str) throws RemoteException;
}
