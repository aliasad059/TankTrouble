package Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represent a server for game.
 * Every server have some game.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class Server {
    private static ArrayList<ObjectOutputStream> objectWriters;
    private static int gameCapacity = Constants.clientsNumber;
    private static ServerGame serverGame;

    /**
     * This is main method of server class catch client (or user) and when they was enough for selected game run it.
     *
     * @param args is array of string
     */
    public static void main(String[] args) {
        objectWriters = new ArrayList<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket welcomingSocket = new ServerSocket(Constants.port)) {
            System.out.println("Server started.\nWaiting for a client ... ");

            //NOTE: lines 40 to 42 must be comment and counter sets to 0 when using Client Class (Not from GUI)
            int counter = 0;
            while (counter <= gameCapacity) {
                Socket connectionSocket = welcomingSocket.accept();
                System.out.println("Client accepted!");
                if (counter == 0) {//assigning ServerGame
                    pool.execute(new SettingClientHandler(connectionSocket));
                } else//adding clients handlers
                    pool.execute(new ClientHandler(connectionSocket, counter));
                counter++;
            }
            pool.shutdown();
            System.out.print("done.\nClosing server ... ");
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    /**
     * This is first inner class of Server class and handle them fore example set group number for them and save network data/
     */
    private static class ClientHandler implements Runnable {

        private Socket connectionSocket;
        private ObjectOutputStream objectWriter;
        private ObjectInputStream objectReader;
        private int clientNum;

        /**
         * this constructor just fill fields based on input parameters.
         *
         * @param connectionSocket is connection socket for network game
         * @param clientNum        is number of client
         */
        public ClientHandler(Socket connectionSocket, int clientNum) {
            this.connectionSocket = connectionSocket;
            this.clientNum = clientNum;
        }

        /**
         * This method is override of run method of "Runnable" class and grouped user for game and update network
         * data of all players in every moment
         */
        @Override
        public void run() {
            try {
                OutputStream out = connectionSocket.getOutputStream();
                InputStream in = connectionSocket.getInputStream();
                objectWriter = new ObjectOutputStream(out);
                objectReader = new ObjectInputStream(in);

                //NOTE: lines 88 - 98 must be comment when using Client Class (Not from GUI)

                int groupNumber;
                if (serverGame.getGameType().equals("solo")) {
                    groupNumber = clientNum;
                } else {
                    groupNumber = ((clientNum - 1) % 2) + 1;
                }
                objectWriter.writeObject(new ClientConfigs(groupNumber
                        , serverGame.getTankHealth()
                        , serverGame.getBulletDamage()
                        , serverGame.getDWallHealth())
                );

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


    /**
     * This is second inner class
     * this class is used for sending data of the game settings from creator of the game
     */
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

