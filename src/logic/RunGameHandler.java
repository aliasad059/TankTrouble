package logic;

import logic.Player.UserPlayer;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RunGameHandler {
    private ArrayList<RunGame> runGameArrayList;
    private int numberOfGroupInTheGame;
    private String gameMode;
    private int winnerGroup;
    private Integer[] winOfGroupsLIG;
    private boolean ligIsOver;

    public RunGameHandler(int numberOfGroupInTheGame, String gameMode) {
        runGameArrayList = new ArrayList<>();
        this.numberOfGroupInTheGame = numberOfGroupInTheGame;
        this.gameMode = gameMode;
        if (gameMode.equals("lig")) {
            winOfGroupsLIG = new Integer[numberOfGroupInTheGame];
            ligIsOver = false;
        }
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
        //needed actions after game over
        if (gameMode.equals("lig")) {
            winOfGroupsLIG[winnerGroup - 1]++;
            for (Integer integer : winOfGroupsLIG) {
                if (integer >= 3) {
                    ligIsOver = true;
                    winnerGroup = integer + 1;
                    actionAfterGameOver();
                    break;
                }
            }
        } else {
            System.out.println("in else..................");
            actionAfterGameOver();
        }

    }


    public int getIndexOfLargest(Integer[] array) {
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
}
