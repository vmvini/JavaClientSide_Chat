package ifpb.pod.proj.clientside;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by vmvini on 07/05/16.
 */
public class SocketMessages {

    public final String LOSTSERVER = "SERVER_CONNECTION_LOST";
    public final String LOGINFAIL = "AUTHENTICATION_ERROR";
    public final String USER_REG_SUC = "SUCCESS_USER_REGISTER";
    public final String SUCCESS_SIGNUP_GROUP = "SUCCESS_SIGNUP_GROUP";
    public final String NOT_LOGGED = "NOT_LOGGED";
    public final String MESSAGE_SENDED = "MESSAGE_SENDED";
    public final String LOSTMESSAGE = "MESSAGE_WAITING";

    public void sendMessage(Socket socket, String msg){
        try {
            socket.getOutputStream().write(msg.getBytes("UTF-8"));
        }
        catch(IOException io){
            io.printStackTrace();
        }
    }



}
