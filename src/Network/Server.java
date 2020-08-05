package Network;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<ObjectOutputStream> objectWriters;
    private static int gameCapacity = Constants.clientsNumber;
    private static ServerGame serverGame;


    public static void main(String[] args) {
        objectWriters = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        try (ServerSocket welcomingSocket = new ServerSocket(Constants.port)) {
            System.out.print("Server started.\nWaiting for a client ... ");
            int counter = 0;
            while (counter <= gameCapacity) {
                Socket connectionSocket = welcomingSocket.accept();
                System.out.println("client accepted!");
                if (counter == 0) {//assigning ServerGame
                    pool.execute(new SettingClientHandler(connectionSocket));
                    System.out.println("setter socket executed");
                } else//adding clients handlers
                    clientHandlers.add(new ClientHandler(connectionSocket, counter));
                counter++;
            }
            System.out.println("Game started");
            for (ClientHandler client : clientHandlers) {
                pool.execute(client);
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
                OutputStream out = connectionSocket.getOutputStream();
                InputStream in = connectionSocket.getInputStream();
                System.out.println("setting group team number");
                if (serverGame.getGameType().equals("solo")) {
                    //all are in group 1
                    out.write((clientNum + "").getBytes());
                    System.out.println("sent");
                } else {
                    connectionSocket.getOutputStream().write(((clientNum - 1) % 2) + 1);
                    System.out.println("sent");
                }
                objectWriter = new ObjectOutputStream(out);
                objectReader = new ObjectInputStream(in);
                objectWriters.add(objectWriter);


                //receiving null from client means the client tank is blasted but still see other players match
                //receiving another null means that the the game has finished
                // or the client do not like to see other's match
                int nullCounter = 0;
                while (nullCounter != 2) {
                    NetworkData networkDataToSendClients = null;
                    try {
                        networkDataToSendClients = (NetworkData) objectReader.readObject();
                    } catch (NullPointerException e) {
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

    private static class SettingClientHandler implements Runnable {

        private Socket connectionSocket;

        public SettingClientHandler(Socket connectionSocket) {
            this.connectionSocket = connectionSocket;
        }

        @Override
        public void run() {
            try {
                OutputStream out = connectionSocket.getOutputStream();
                InputStream in = connectionSocket.getInputStream();
                serverGame = (ServerGame) new ObjectInputStream(in).readObject();
                gameCapacity = serverGame.getPlayersNumber();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

