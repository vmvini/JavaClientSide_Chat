package ifpb.pod.proj.clientside;

import java.util.List;
import java.util.Map;

/**
 * Created by vmvini on 07/05/16.
 */
public class PendantMessageThread extends Thread {


    private PendantMessages pm;
    private UserSideClientJava clientJava;
    private String token;
    private String email;

    public PendantMessageThread(PendantMessages pm, UserSideClientJava clientJava,String token,String email){
        this.pm = pm;
        this.clientJava = clientJava;
        this.token = token;
        this.email = email;
    }

    public void run(){

        while(true){

            List< Map<String, String> > messages = pm.getAll(email);

            for( Map<String, String> msg : messages ){
                msg.put("token", token);
                clientJava.escreverMensagem(msg);
            }

            try {
                Thread.sleep(3000);
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }

        }

    }

}
