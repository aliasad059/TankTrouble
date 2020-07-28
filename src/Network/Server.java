package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<Socket>sockets;
    private static ArrayList<ArrayList<NetworkData>>networkDataOfClients;
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
            this.clientNum=clientNum;
        }

        @Override
        public void run() {
            try {
                OutputStream out = connectionSocket.getOutputStream();
                InputStream in = connectionSocket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                while (in.readAllBytes() != null) {
//                    for (String msg : messages) {
                        networkDataOfClients.get(clientNum).add((NetworkData) objectInputStream.readObject());
//                        out.write(msg.getBytes());
//                        System.out.println("SENT to " + clientNum + ": " + msg);
                        Thread.sleep(2000);
//                    }
                }
                System.out.print("All messages sent.\nClosing client ... ");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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

