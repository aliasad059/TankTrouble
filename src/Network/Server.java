package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<ObjectOutputStream> objectWriters;
//    private static ArrayList<ObjectInputStream> objectReaders;
//    private static ArrayList<ArrayList<NetworkData>> networkDataOfClients;

    public static void main(String[] args) {
//        objectReaders = new ArrayList<>();
        objectWriters = new ArrayList<>();
//        networkDataOfClients = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        int counter = 0;
        try (ServerSocket welcomingSocket = new ServerSocket(Constants.port)) {
            System.out.print("Server started.\nWaiting for a client ... ");
            while (counter < Constants.clientsNumber) {
                Socket connectionSocket = welcomingSocket.accept();
//                networkDataOfClients.add(new ArrayList<>());
                counter++;
                System.out.println("client accepted!");
//                synchronized (objectWriters) {
//                    objectWriters.add(new ObjectOutputStream(connectionSocket.getOutputStream()));
//                    objectReaders.add(new ObjectInputStream(connectionSocket.getInputStream()));
//                }
                pool.execute(new ClientHandler(connectionSocket, counter));
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
        private ObjectOutputStream objectWriter;
        private ObjectInputStream objectReader;
        private int clientNum;

        public ClientHandler(Socket connectionSocket, int clientNum) {
            this.connectionSocket = connectionSocket;
            this.clientNum = clientNum;
        }

        @Override
        public void run() {
            try {
                objectWriter = new ObjectOutputStream(connectionSocket.getOutputStream());
                objectReader = new ObjectInputStream(connectionSocket.getInputStream());
                objectWriters.add(objectWriter);
                System.out.println(objectWriters.size());
                //receiving null from client means the client tank is blasted but still see other players match
                //receiving another null means that the the game has finished
                // or the client do not like to see other's match
                int nullCounter = 0;
                while (nullCounter != 2) {
                    NetworkData networkDataToSendClients = null;
                    try {
                        networkDataToSendClients = (NetworkData) objectReader.readObject();
                    } catch (NullPointerException e) {
                        System.out.println("++");
                        nullCounter++;
                    }
                    if (nullCounter == 0) {
                        for (ObjectOutputStream writer : objectWriters) {
                            if (objectWriter != writer) {
                                writer.writeObject(networkDataToSendClients);
                            }
                        }
                    }
                    //update the game based on ping
//                    Thread.sleep(Constants.PING);
                }
                System.out.print("Game finished\nClosing client" + clientNum + " ... ");
            } catch (IOException | ClassNotFoundException e) {
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

