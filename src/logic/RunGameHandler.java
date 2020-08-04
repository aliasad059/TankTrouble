package logic;

import graphic.Run;
import logic.Engine.MapFrame;
import logic.Player.BotPlayer;
import logic.Player.UserPlayer;
import logic.Tank.Tank;
import logic.Wall.DestructibleWall;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RunGameHandler {
    private ArrayList<RunGame> runGameArrayList;
    private int numberOfGroupInTheGame;
    private String gameMode;
    private int winnerGroup;
    private Integer[] winOfGroupsLIG;
    private boolean ligIsOver;
    private int tankHealth;
    private ArrayList<UserPlayer> saveSetUser;
    private ArrayList<BotPlayer> saveSetBot;

    public RunGameHandler(int numberOfGroupInTheGame, String gameMode, int tankHealth) {
        runGameArrayList = new ArrayList<>();
        this.numberOfGroupInTheGame = numberOfGroupInTheGame;
        this.gameMode = gameMode;
        if (gameMode.equals("lig")) {
            System.out.println("is in the lig...........................");
            winOfGroupsLIG = new Integer[numberOfGroupInTheGame];
            for (int i = 0; i < numberOfGroupInTheGame; i++) {
                winOfGroupsLIG[i] = 0;
            }
            ligIsOver = false;
        }
        this.tankHealth = tankHealth;
        saveSetUser = new ArrayList<>();
        saveSetBot = new ArrayList<>();
    }


    public void checkAllGame() {

        for (RunGame runGame : runGameArrayList) {
            if (!runGame.getMapFrame().getTankTroubleMap().isGameOver()) return;
        }
        Integer[] groupWins = new Integer[numberOfGroupInTheGame];
        for (int i = 0; i < numberOfGroupInTheGame; i++) {
            groupWins[i] = 0;
        }
        for (RunGame runGame : runGameArrayList) {
            groupWins[runGame.getMapFrame().getTankTroubleMap().getWinnerGroup() - 1]++;
        }
        winnerGroup = getIndexOfLargest(groupWins) + 1;
        System.out.println("winner group is: " + winnerGroup);
        //needed actions after game over
        if (gameMode.equals("lig")) {
            winOfGroupsLIG[winnerGroup - 1]++;
            System.out.println("win of winner group is:" + winOfGroupsLIG[winnerGroup - 1]);
            for (Integer integer : winOfGroupsLIG) {
                if (integer >= 3) {
                    ligIsOver = true;
                    winnerGroup = integer + 1;
                    actionAfterGameOver();
                    break;
                }
            }
            if (!ligIsOver) {
                System.out.println("in the if of new game....................");
                newGame();
            }
        } else {
            System.out.println("in else..................");
            actionAfterGameOver();
        }

    }

    private void newGame() {
        /*
        for(UserPlayer userPlayer:runGameArrayList.get(0).getGame().getTankTroubleMap().getUsers()){
            userPlayer.getUserTank().setHealth(tankHealth);
        }
        runGameArrayList.get(0).run();

         */

        System.out.println("in the new game..............");
        //runGameArrayList.get(0).getMapFrame().dispatchEvent(new WindowEvent(runGameArrayList.get(0).getMapFrame(), WindowEvent.WINDOW_CLOSING));

        RunGameHandler runGameHandler = new RunGameHandler(numberOfGroupInTheGame, "lig", tankHealth);
        MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);
        mapFrame.setLocationRelativeTo(null); // put frame at center of screen
        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapFrame.setVisible(true);
        mapFrame.initBufferStrategy();

        for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
            destructibleWall.setHealth(runGameArrayList.get(0).getGame().getTankTroubleMap().getUsers().get(0).getWallHealth());
        }

        // create and add user
        System.out.println("size of saved player is: " + saveSetUser.size());
        ArrayList<UserPlayer> userPlayers = new ArrayList<>();
        for (UserPlayer player : saveSetUser) {
            UserPlayer userPlayer = new UserPlayer(player.getName(), player.getPassword(), player.getColor(), mapFrame.getTankTroubleMap(), player.getDataBaseFileName());
            userPlayer.getUserTank().setBulletDamage(player.getUserTank().getBulletDamage());
            userPlayer.setGroupNumber(player.getGroupNumber());
            userPlayers.add(userPlayer);
        }


        // bots
        ArrayList<BotPlayer> bots = new ArrayList<>();

        // create user's team (friends bots)
        for (BotPlayer botPlayer : saveSetBot) {
            BotPlayer bot = new BotPlayer(botPlayer.getName(), botPlayer.getColor(), mapFrame.getTankTroubleMap(), botPlayer.getGroupNumber());
            bot.getAiTank().setBulletDamage(botPlayer.getAiTank().getBulletDamage()); //bullet damage
            System.out.println("tank health in bot loop:" + tankHealth);
            bot.getAiTank().setHealth(tankHealth); //tank health
            bots.add(bot);
        }
        mapFrame.getTankTroubleMap().setUsers(userPlayers);
        mapFrame.getTankTroubleMap().setBots(bots);


        RunGame runGame = new RunGame(mapFrame, runGameHandler);
        runGameHandler.getRunGameArrayList().add(runGame);
        runGameHandler.setWinOfGroupsLIG(winOfGroupsLIG);
        System.out.println("run....................................");
        runGame.run();
    }

    private int getIndexOfLargest(Integer[] array) {
        if (array == null || array.length == 0) return -1; // null or empty

        int largest = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[largest]) largest = i;
        }
        return largest;
    }


    private void actionAfterGameOver() {
        System.out.println("in afterGameOver..................");
        for (RunGame runGame : runGameArrayList) {
            for (UserPlayer userPlayer : runGame.getMapFrame().getTankTroubleMap().getUsers()) {
                LocalDateTime time = LocalDateTime.now();
                Duration duration = Duration.between(runGame.getMapFrame().getTankTroubleMap().getStartTime(), time);
                userPlayer.setTimePlay(userPlayer.getTimePlay() + duration.getSeconds());
                if (userPlayer.getGroupNumber() == winnerGroup) {
                    userPlayer.setXP(userPlayer.getXP() + 0.5); // winner prize
                    double newXP = (double) userPlayer.getUserTank().getNumberOfDestroyedTank() / 2; // 2.............
                    userPlayer.setXP(userPlayer.getXP() + newXP);
                    userPlayer.XPToLevel();
                    if (runGame.getMapFrame().getTankTroubleMap().isNetwork()) {
                        userPlayer.setWinInNetworkMatch(userPlayer.getWinInNetworkMatch() + 1);
                    } else {
                        userPlayer.setWinInBotMatch(userPlayer.getWinInBotMatch() + 1);
                    }
                }
                //lose
                else {
                    double newXP = (double) userPlayer.getUserTank().getNumberOfDestroyedTank() / 5; // 5...........
                    userPlayer.setXP(userPlayer.getXP() + newXP);
                    userPlayer.XPToLevel();
                    if (runGame.getMapFrame().getTankTroubleMap().isNetwork()) {
                        userPlayer.setLoseInNetworkMatch(userPlayer.getLoseInNetworkMatch() + 1);
                    } else {
                        userPlayer.setLoseInBotMatch(userPlayer.getLoseInBotMatch() + 1);
                    }
                }
                saveAUser(userPlayer);
            }
        }
        File dir = new File("dataBase\\rememberMe");
        File[] allFile = dir.listFiles();
        if (allFile.length != 0) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("dataBase\\rememberMe\\rememberMe.src"))) {
                UserPlayer user = (UserPlayer) objectInputStream.readObject();
                for (RunGame runGame : runGameArrayList) {
                    for (UserPlayer userPlayer : runGame.getMapFrame().getTankTroubleMap().getUsers()) {
                        if (user.getName().equals(userPlayer.getName())) {
                            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\rememberMe\\rememberMe.src"))) {
                                objectOutputStream.writeObject(userPlayer);
                            }
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAUser(UserPlayer userPlayer) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\" + userPlayer.getDataBaseFileName()))) {
            objectOutputStream.writeObject(userPlayer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RunGame> getRunGameArrayList() {
        return runGameArrayList;
    }

    public boolean isLigIsOver() {
        return ligIsOver;
    }

    public void setLigIsOver(boolean ligIsOver) {
        this.ligIsOver = ligIsOver;
    }

    public void setWinOfGroupsLIG(Integer[] winOfGroupsLIG) {
        this.winOfGroupsLIG = winOfGroupsLIG;
    }

    public void setSaveSetUser(ArrayList<UserPlayer> saveSetUser) {
        this.saveSetUser = saveSetUser;
    }

    public void setSaveSetBot(ArrayList<BotPlayer> saveSetBot) {
        this.saveSetBot = saveSetBot;
    }

    public int getTankHealth() {
        return tankHealth;
    }
}
