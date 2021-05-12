import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        var srv = new Server();
//        srv.start();
//
//        while (srv.isWorking() ) {
//            Thread.onSpinWait();
//        }

        // for debug >>>
        User u = new User().getUser("login","password");
        if (u== null || !u.isUserInitialized()) u.createUser("login","password","nickName");
        System.out.println(u);
        // for debug <<<

        Server s = new Server();



    }
}
