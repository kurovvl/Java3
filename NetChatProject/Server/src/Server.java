import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static Server server;

    public static Server getServer() {
        return server;
    }

    private final int PORT = 5190;
    private List<ClientHandler> clients;
    private AuthService authService;

    public Server() {
        server = this;
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Ждем подключения клиента");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMsg(msg);
        }
    }
//    public synchronized void sendTo(String nickName, String msg) {
//        var client = clients.stream().filter(c -> c.getName().equals(nickName)).findFirst();
//        client.ifPresent(c -> sendTo(c.getName(), msg));
//    }

    public synchronized void sendMsgToClient(ClientHandler fromClient, String nickTo, String msg) {
        for (ClientHandler client : clients) {
            if (client.getName().equals(nickTo)) {
                client.sendMsg("от пользователя " + fromClient.getName() + ": " + msg);
                fromClient.sendMsg("клиенту " + nickTo + ": " + msg);
                return;
            }
        }
        fromClient.sendMsg("Участник с ником \"" + nickTo + "\" не найден");
    }

    public synchronized void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clients");
        for (ClientHandler client : clients) {
            sb.append(" ").append(client.getName());
        }
        broadcastMsg(sb.toString());
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public AuthService getAuthService() {
        return authService;
    }
}
