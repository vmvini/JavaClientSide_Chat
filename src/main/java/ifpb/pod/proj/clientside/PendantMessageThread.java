package ifpb.pod.proj.clientside;

import java.util.List;
import java.util.Map;

/**
 * Created by vmvini on 07/05/16.
 */
public class PendantMessageThread extends Thread {


    private PendantMessages pm;
    private UserSideClientJava clientJava;

    public PendantMessageThread(PendantMessages pm, UserSideClientJava clientJava){
        this.pm = pm;
        this.clientJava = clientJava;
    }

    public void run(){

        while(true){

            List< Map<String, String> > messages = pm.getAll();

            for( Map<String, String> msg : messages ){
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
