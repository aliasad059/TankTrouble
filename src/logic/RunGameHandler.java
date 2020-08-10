package logic;

import graphic.Interface;
import logic.Engine.MapFrame;
import logic.Player.BotPlayer;
import logic.Player.UserPlayer;
import logic.Wall.DestructibleWall;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class get game and handle its finish and  also after that game was finished do action after game include
 * set XP and level etc.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
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
    private Interface anInterface;

    /**
     * This is only constructor of this class and fill some filed based on input parameters and also initialize some fields
     *
     * @param numberOfGroupInTheGame is number of Group that there in the game
     * @param gameMode               is mode of game (lig / death match)
     * @param tankHealth             is health of tank that there in the pam
     */
    public RunGameHandler(int numberOfGroupInTheGame, @NotNull String gameMode, int tankHealth, Interface anInterface) {
        runGameArrayList = new ArrayList<>();
        this.numberOfGroupInTheGame = numberOfGroupInTheGame;
        this.gameMode = gameMode;
        if (gameMode.equals("lig")) {
            winOfGroupsLIG = new Integer[numberOfGroupInTheGame];
            for (int i = 0; i < numberOfGroupInTheGame; i++) {
                winOfGroupsLIG[i] = 0;
            }
            ligIsOver = false;
        }
        this.tankHealth = tankHealth;
        saveSetUser = new ArrayList<>();
        saveSetBot = new ArrayList<>();
        this.anInterface = anInterface;
    }

    /**
     * This method check games and if it was finished do needed action it fore example if need run game again for lig.
     * And after game finish do action after game include set XP and level etc.
     */
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
        //needed actions after game over
        if (gameMode.equals("lig")) {
            winOfGroupsLIG[winnerGroup - 1]++;
            for (int i = 0; i < winOfGroupsLIG.length; i++) {
                if (winOfGroupsLIG[i] >= Constants.LIG_MATCH_NUMBER) {
                    ligIsOver = true;
                    winnerGroup = i + 1;
                    actionAfterGameOver();
                    break;
                }
            }
            if (!ligIsOver) {
                newGame();
            }
        } else {
            actionAfterGameOver();
        }

    }

    /**
     * This method use for lig match and run game again.
     */
    private void newGame() {

        RunGameHandler runGameHandler = new RunGameHandler(numberOfGroupInTheGame, "lig", tankHealth, anInterface);
        MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);

        for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
            destructibleWall.setHealth(runGameArrayList.get(0).getGame().getTankTroubleMap().getUsers().get(0).getWallHealth());
        }

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
            bot.getAiTank().setHealth(tankHealth); //tank health
            bots.add(bot);
        }

        mapFrame.getTankTroubleMap().setUsers(userPlayers);
        mapFrame.getTankTroubleMap().setBots(bots);

        mapFrame.getTankTroubleMap().setController(mapFrame.getTankTroubleMap().getUsers().get(0));
        for (RunGame runGame : runGameArrayList) {
            runGame.getMapFrame().setVisible(false);
        }

        RunGame runGame = new RunGame(mapFrame, runGameHandler);
        runGameHandler.getRunGameArrayList().add(runGame);
        runGameHandler.setWinOfGroupsLIG(winOfGroupsLIG);
        runGame.run();
    }

    /**
     * This method return index of biggest number in a array.
     *
     * @param array is array that you wanna iterate
     * @return integer as index
     */
    private int getIndexOfLargest(Integer[] array) {
        if (array == null || array.length == 0) return -1;
        int largest = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[largest]) largest = i;
        }
        return largest;
    }


    /**
     * This method do action after that game was finished.
     * set level, XP etc and also save all these changes.
     */
    private void actionAfterGameOver() {
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
        File dir = new File("dataBase/rememberMe");
        File[] allFile = dir.listFiles();
        if (allFile.length != 0) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("dataBase/rememberMe/rememberMe.src"))) {
                UserPlayer user = (UserPlayer) objectInputStream.readObject();
                for (RunGame runGame : runGameArrayList) {
                    for (UserPlayer userPlayer : runGame.getMapFrame().getTankTroubleMap().getUsers()) {
                        if (user.getName().equals(userPlayer.getName())) {
                            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase/rememberMe/rememberMe.src"))) {
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
        try {
            Thread.sleep(5000);
            for (RunGame runGame : runGameArrayList) {
                runGame.getMapFrame().setVisible(false);
            }
            anInterface.gameFrame(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method save a user in file.
     *
     * @param userPlayer is user that you wanna save
     */
    private void saveAUser(UserPlayer userPlayer) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase/" + userPlayer.getDataBaseFileName()))) {
            objectOutputStream.writeObject(userPlayer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Getter method of runGameArrayList field
     *
     * @return array list of game that there in handler
     */
    public ArrayList<RunGame> getRunGameArrayList() {
        return runGameArrayList;
    }

    /**
     * This is setter method for winOfGroupsLIG field.
     *
     * @param winOfGroupsLIG is a array list that show number of win of every groups
     */
    public void setWinOfGroupsLIG(Integer[] winOfGroupsLIG) {
        this.winOfGroupsLIG = winOfGroupsLIG;
    }

    /**
     * This is setter method for saveSetUser field.
     *
     * @param saveSetUser array list of users of the game at the beginning of the game
     */
    public void setSaveSetUser(ArrayList<UserPlayer> saveSetUser) {
        this.saveSetUser = saveSetUser;
    }

    /**
     * This is setter method for saveSetBot field.
     *
     * @param saveSetBot array list of bots of the game at the beginning of the game
     */
    public void setSaveSetBot(ArrayList<BotPlayer> saveSetBot) {
        this.saveSetBot = saveSetBot;
    }

    /**
     * Getter method of tankHealth field
     *
     * @return an integer as health of tank
     */
    public int getTankHealth() {
        return tankHealth;
    }
}
