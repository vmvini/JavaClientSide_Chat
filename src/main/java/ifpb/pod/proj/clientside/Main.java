package ifpb.pod.proj.clientside;

/**
 * Created by vmvini on 07/05/16.
 */
public class Main {

    public static void main(String[] args){


        PendantMessages pm = new PendantMessages();
        UserSideClientJava clientJava = new UserSideClientJava(pm);
        PendantMessageThread pt = new PendantMessageThread(pm, clientJava);

        pt.start();
        clientJava.listenConnection();

    }

}
