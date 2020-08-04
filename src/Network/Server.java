package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<ObjectOutputStream> objectWriters;

    public static void main(String[] args) {
        objectWriters = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        int counter = 0;
        try (ServerSocket welcomingSocket = new ServerSocket(Constants.port)) {
            System.out.print("Server started.\nWaiting for a client ... ");
            //TODO: read clients number from server file
            while (counter < Constants.clientsNumber) {
                Socket connectionSocket = welcomingSocket.accept();
                counter++;
                System.out.println("client accepted!");
                pool.execute(new ClientHandler(connectionSocket, counter));
//                clientHandlers.add(new ClientHandler(connectionSocket, counter));
            }
//            System.out.println("Game started");
//            for (ClientHandler client : clientHandlers) {
//                pool.execute(client);
//            }
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

