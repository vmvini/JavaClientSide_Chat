package ifpb.pod.proj.clientside;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by vmvini on 07/05/16.
 */
public class SocketMessages {

    public final String LOSTSERVER = "SERVER_CONNECTION_LOST";
    public final String LOGINFAIL = "AUTHENTICATION_ERROR";


    public void sendMessage(Socket socket, String msg){
        try {
            socket.getOutputStream().write(msg.getBytes("UTF-8"));
        }
        catch(IOException io){
            io.printStackTrace();
        }
    }



}
