package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<Socket> sockets;
    private static ArrayList<ArrayList<NetworkData>> networkDataOfClients;

    public static void main(String[] args) {
        sockets = new ArrayList<>();
        networkDataOfClients = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        int count = 0;
        try (ServerSocket welcomingSocket = new ServerSocket(Constants.port)) {
            System.out.print("Server started.\nWaiting for a client ... ");
            while (count < Constants.clientsNumber) {
                Socket connectionSocket = welcomingSocket.accept();
                networkDataOfClients.add(new ArrayList<>());
                count++;
                System.out.println("client accepted!");
                sockets.add(connectionSocket);
                pool.execute(new ClientHandler(connectionSocket, count));
            }
            pool.shutdown();
            System.out.print("done.\nClosing server ... ");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("done.");
    }

    private static class ClientHandler implements Runnable {

        private Socket connectionSocket;
        private int clientNum;

        public ClientHandler(Socket connectionSocket, int clientNum) {
            this.connectionSocket = connectionSocket;
            this.clientNum = clientNum;
        }

        @Override
        public void run() {
            try {
                InputStream in = connectionSocket.getInputStream();
                ObjectInputStream oin = new ObjectInputStream(in);
                //receiving null from client means the client tank is blasted but still see other players match
                //receiving another null means that the the game has finished
                // or the client do not like to see other's match
                int nullCounter = 0;
                while (nullCounter != 2) {
                    NetworkData networkDataToSendClients = null;
                    try {
                        networkDataToSendClients = (NetworkData) oin.readObject();
                    } catch (NullPointerException ignored) {
                        nullCounter++;
                    }
                    if (nullCounter == 0) {
                        for (Socket socket : sockets) {
                            if (socket != connectionSocket) {
                                OutputStream outputStream = socket.getOutputStream();
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.writeObject(networkDataToSendClients);
                            }
                        }
                    }
                    //update the game based on ping
                    Thread.sleep(Constants.PING);
                }
                System.out.print("Game finished\nClosing client" + clientNum + " ... ");
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    connectionSocket.close();
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        }
    }

}

