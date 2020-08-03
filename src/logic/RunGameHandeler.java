package logic;

import logic.Player.UserPlayer;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RunGameHandeler {
    private ArrayList<RunGame> runGameArrayList;

    public RunGameHandeler() {
        runGameArrayList = new ArrayList<>();
    }

    public void checkAllGame() {
        for (RunGame runGame : runGameArrayList) {
            if (!runGame.getMapFrame().getTankTroubleMap().isGameOver()) return;
        }
        int firstGroupWins = 0;
        int secondGroupWins = 0;
        for (RunGame runGame : runGameArrayList) {
            if (runGame.getMapFrame().getTankTroubleMap().getWinnerGroup() == 1) firstGroupWins++;
            else secondGroupWins++;
        }
        int winnerGroup;
        if (firstGroupWins > secondGroupWins) {
            winnerGroup = 1;
        } else {
            winnerGroup = 2;
        }

        //needed actions after game over
        actionAfterGameOver(winnerGroup);

    }

    private void actionAfterGameOver(int winnerGroup) {
        System.out.println("size of run game array list: " + runGameArrayList.size());
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
}
