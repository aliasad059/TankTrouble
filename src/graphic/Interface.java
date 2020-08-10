package graphic;


import Network.ServerConfigs;
import Network.ServerGame;
import logic.Engine.MapFrame;
import logic.Player.BotPlayer;
import logic.Player.UserPlayer;
import logic.RunGame;
import logic.RunGameHandler;
import logic.SoundsOfGame;
import logic.TankTroubleMap;
import logic.Wall.DestructibleWall;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class represent interface of "Tank Trouble" game.
 * Ont the other word this class is main part of graphic and have
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class Interface {
    private JFrame logInAndSignUpPageFrame;
    private JPTextField pTextField;
    private JPPasswordField pPasswordField, confirmPPasswordField;
    private JLabel level, timePlayLabel, winVSComputerLabel, loseVSComputerLabel, winInOnlineModeLabel, loseInOnlineModeLabel, shapeOfTankLabel;
    private UserPlayer user;
    private String gameMode;
    private SoundsOfGame mainMenuMusic;
    private SoundsOfGame loginPageMusic;
    private boolean isRememberMeActive;
    private DefaultListModel<String> serverListModel;
    private ArrayList<DefaultListModel<String>> arrayListOfGameOfServer;
    private ArrayList<ServerConfigs> serverConfigs;


    /**
     * This is constructor of interface class and set "Nimbus" look and feel and initialize our field.
     */
    public Interface() {

        //set look and fill[
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: " + e.getException());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("This system does not support this look and feel.");
        } catch (InstantiationException e) {
            System.out.println("Instantiation exception.");
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccess Exception");
        }

        //initialization
        pTextField = new JPTextField("User name");
        pPasswordField = new JPPasswordField("Password");
        confirmPPasswordField = new JPPasswordField("Confirm");
        level = new JLabel(" 0");
        timePlayLabel = new JLabel(" 0");
        winVSComputerLabel = new JLabel(" 0");
        loseVSComputerLabel = new JLabel(" 0");
        winInOnlineModeLabel = new JLabel(" 0");
        loseInOnlineModeLabel = new JLabel(" 0");
        shapeOfTankLabel = new JLabel(new ImageIcon("kit/smallTanks/Brown/normal.png"));
        gameMode = "deathMatch";
        loginPageMusic = new SoundsOfGame("loginPage", true);
        mainMenuMusic = new SoundsOfGame("mainMenu", true);
        isRememberMeActive = false;
        serverListModel = new DefaultListModel<>();
        arrayListOfGameOfServer = new ArrayList<>();
        serverConfigs = new ArrayList<>();
    }

    /**
     * This method check player select remember me action.
     * if it was selected login his account automatically.
     */
    private void rememberMe() {
        // is there remember me?
        File dir = new File("dataBase/rememberMe");
        File[] allFile = dir.listFiles();
        if (allFile.length != 0) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("dataBase/rememberMe/rememberMe.src"))) {
                user = (UserPlayer) objectInputStream.readObject();
                File[] serversConfigFile = new File("serverConfigs").listFiles();

                for (File serverFile : serversConfigFile) {
                    ObjectInputStream objectReader = new ObjectInputStream(new FileInputStream(serverFile));
                    ServerConfigs serverToAdd = (ServerConfigs) objectReader.readObject();
                    serverConfigs.add(serverToAdd);
                    String string = serverToAdd.getServerName() + "                           " + serverToAdd.getServerIP();
                    serverListModel.addElement(string);
                    arrayListOfGameOfServer.add(new DefaultListModel<>());
                    objectReader.close();
                }
                isRememberMeActive = true;
                gameFrame(false);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            logInAndSignUpPage();
        }
    }

    /**
     * this method create login page of game that include sign in and sign up buttons.
     */
    private void logInAndSignUpPage() {
        loginPageMusic.playSound();
        logInAndSignUpPageFrame = new JFrame("login page");
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            logInAndSignUpPageFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logInAndSignUpPageFrame.setLocationRelativeTo(null);
        logInAndSignUpPageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel loginBackgroundLabel = new JLabel(new ImageIcon("kit/backGround/Fish.gif"));
        logInAndSignUpPageFrame.setContentPane(loginBackgroundLabel);

        JButton login = new JButton("Login");
        //JButton login = new JButton(new ImageIcon("kit/button/logIn.png"));
        login.addActionListener(loginEvent -> loginButtonAction());

        JButton signUp = new JButton("SingUp");
        //JButton signUp = new JButton(new ImageIcon("kit/button/signUp.png"));
        signUp.addActionListener(signUpEvent -> signUpButtonAction());

        logInAndSignUpPageFrame.setLayout(new BorderLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(login);
        southPanel.add(signUp);
        logInAndSignUpPageFrame.add(southPanel, BorderLayout.SOUTH);

        JLabel northLabel = new JLabel("Welcome to TANK TROUBLE");
        northLabel.setForeground(new Color(60, 40, 20));
        northLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logInAndSignUpPageFrame.add(northLabel, BorderLayout.NORTH);

        logInAndSignUpPageFrame.pack();
        logInAndSignUpPageFrame.setVisible(true);

    }

    /**
     * This method get frame and set logo of game for that.
     *
     * @param frame is frame that you wanna set logo for it
     */
    private void setLogoFrame(@NotNull JFrame frame) {
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            frame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is action of login button in login page.
     */
    private void loginButtonAction() {
        logInAndSignUpPageFrame.dispatchEvent(new WindowEvent(logInAndSignUpPageFrame, WindowEvent.WINDOW_CLOSING));
        JFrame loginFrame = new JFrame("Login");

        setLogoFrame(loginFrame);

        loginFrame.setSize(250, 150);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        loginFrame.setContentPane(panel);

        JLabel usernameLabel = new JLabel(" User name : ");
        JLabel passwordLabel = new JLabel(" Password : ");

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        centerPanel.add(usernameLabel);
        centerPanel.add(pTextField);
        centerPanel.add(passwordLabel);
        centerPanel.add(pPasswordField);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(loginCheckEvent -> loginCheckButtonAction(loginFrame));

        JButton backToLogInAndSignUpPageButton = new JButton("Back");
        backToLogInAndSignUpPageButton.addActionListener(actionEvent -> backToLogInAndSignUpPageButtonAction(loginFrame));

        southPanel.add(loginButton);
        southPanel.add(backToLogInAndSignUpPageButton);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        loginFrame.setVisible(true);

    }

    /**
     * This method close login frame and clear pTextField and confirmPPasswordField and show logInAndSignUpPageFrame.
     *
     * @param loginFrame is login frame of game
     */
    private void backToLogInAndSignUpPageButtonAction(@NotNull JFrame loginFrame) {
        loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
        pTextField.setText("");
        pPasswordField = new JPPasswordField("password");
        confirmPPasswordField = new JPPasswordField("password");
        logInAndSignUpPageFrame.setVisible(true);
    }

    /**
     * This method find src file in input folder.
     *
     * @param dir is path of a folder
     * @return array of file with src format
     */
    private File[] srcFileFinder(String dir) {
        File file = new File(dir);
        return file.listFiles((dir1, filename) -> filename.endsWith(".src"));
    }

    /**
     * This method is action of check button and check input information with data base.
     *
     * @param loginFrame is login frame
     */
    private void loginCheckButtonAction(JFrame loginFrame) {
        File[] files = srcFileFinder("dataBase/");
        boolean falseInformation = true;
        for (File file : files) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.getPath()))) {
                UserPlayer userPlayer = (UserPlayer) objectInputStream.readObject();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < pPasswordField.getPassword().length; i++) {
                    stringBuilder.append(pPasswordField.getPassword()[i]);
                }
                if (userPlayer.getName().equals(pTextField.getText()) && userPlayer.getPassword().equals(stringBuilder.toString())) {
                    falseInformation = false;
                    loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
                    int dialogButton = JOptionPane.showConfirmDialog(null, "Do you want active remember me option?", "Remember me?", JOptionPane.YES_NO_OPTION);
                    if (dialogButton == 0) {
                        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase/rememberMe/rememberMe.src"))) {
                            isRememberMeActive = true;
                            objectOutputStream.writeObject(userPlayer);
                        }
                    }
                    //set config based on user
                    user = userPlayer;
                    level = new JLabel(" " + user.getLevel());
                    timePlayLabel = new JLabel(" " + user.getTimePlay());
                    winVSComputerLabel = new JLabel(" " + user.getWinInBotMatch());
                    loseVSComputerLabel = new JLabel(" " + user.getLoseInBotMatch());
                    winInOnlineModeLabel = new JLabel(" " + user.getWinInNetworkMatch());
                    loseInOnlineModeLabel = new JLabel(" " + user.getLoseInNetworkMatch());
                    shapeOfTankLabel = new JLabel(new ImageIcon("kit/smallTanks/" + user.getColor() + "/normal.png"));

                    File[] serversConfigFile = new File("serverConfigs").listFiles();
                    serverListModel.clear();
                    for (File serverFile : serversConfigFile) {
                        ObjectInputStream objectReader = new ObjectInputStream(new FileInputStream(serverFile));
                        ServerConfigs serverToAdd = (ServerConfigs) objectReader.readObject();
                        serverConfigs.add(serverToAdd);
                        String string = serverToAdd.getServerName() + "                           " + serverToAdd.getServerIP();
                        serverListModel.addElement(string);
                        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
                        for (ServerGame serverGame : serverToAdd.getServerGames()) {
                            defaultListModel.addElement("Name: " + serverGame.getGameName() + " |    Game type: " + serverGame.getGameType() + " |    Capacity: " + (serverGame.getPlayersNumber()/*-newGame.getConnectedUser*/) + " |    Match type: " + serverGame.getEndingMode());
                        }
                        arrayListOfGameOfServer.add(defaultListModel);
                        objectReader.close();
                    }
                    loginPageMusic.pause();
                    gameFrame(false);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (falseInformation) {
            JOptionPane.showMessageDialog(null, "Unmatched information.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is action of sign up button in login page.
     */
    private void signUpButtonAction() {
        logInAndSignUpPageFrame.dispatchEvent(new WindowEvent(logInAndSignUpPageFrame, WindowEvent.WINDOW_CLOSING));
        JFrame signUpFrame = new JFrame("Sign Up");
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            signUpFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        signUpFrame.setSize(250, 180);
        signUpFrame.setResizable(false);
        signUpFrame.setLocationRelativeTo(null);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        signUpFrame.setContentPane(panel);

        JLabel usernameLabel = new JLabel(" User name : ");
        JLabel passwordLabel = new JLabel(" Password : ");
        JLabel confirmPasswordLabel = new JLabel(" Confirm Password : ");

        JPanel centerPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        centerPanel.add(usernameLabel);
        centerPanel.add(pTextField);
        centerPanel.add(passwordLabel);
        centerPanel.add(pPasswordField);
        centerPanel.add(confirmPasswordLabel);
        centerPanel.add(confirmPPasswordField);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(registerEvent -> registerButtonAction(signUpFrame));

        JButton backToLogInAndSignUpPageButton = new JButton("Back");
        backToLogInAndSignUpPageButton.addActionListener(actionEvent -> backToLogInAndSignUpPageButtonAction(signUpFrame));

        southPanel.add(registerButton);
        southPanel.add(backToLogInAndSignUpPageButton);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        signUpFrame.setVisible(true);
    }

    /**
     * This method do action of register button and make a new file in data base based on input information.
     * Also read server data and server config file.
     *
     * @param loginFrame is login frame of game
     */
    private void registerButtonAction(JFrame loginFrame) {
        File[] files = srcFileFinder("dataBase/");
        boolean isNewUserName = true;
        for (File file : files) {
            UserPlayer userPlayer;
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.getPath()))) {
                userPlayer = (UserPlayer) objectInputStream.readObject();
                if (userPlayer.getName().equals(pTextField.getText())) {
                    JOptionPane.showMessageDialog(loginFrame, "This user name already exist in the server please write another one.", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    isNewUserName = false;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (isNewUserName) {
            if (Arrays.toString(pPasswordField.getPassword()).equals(Arrays.toString(confirmPPasswordField.getPassword()))) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase/" + (files.length + 1) + ".src"))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < pPasswordField.getPassword().length; i++) {
                        stringBuilder.append(pPasswordField.getPassword()[i]);
                    }
                    user = new UserPlayer(pTextField.getText(), stringBuilder.toString(), "Brown", new TankTroubleMap("./maps/map3.txt", false, LocalDateTime.now(), new RunGameHandler(1, "deathMatch", 100, this)), "" + (files.length + 1) + ".src");
                    objectOutputStream.writeObject(user);

                    File[] serversConfigFile = new File("serverConfigs").listFiles();

                    serverListModel.clear();
                    for (File serverFile : serversConfigFile) {
                        ObjectInputStream objectReader = new ObjectInputStream(new FileInputStream(serverFile));
                        ServerConfigs serverToAdd = (ServerConfigs) objectReader.readObject();
                        serverConfigs.add(serverToAdd);
                        String string = serverToAdd.getServerName() + "                           " + serverToAdd.getServerIP();
                        serverListModel.addElement(string);
                        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
                        for (ServerGame serverGame : serverToAdd.getServerGames()) {
                            defaultListModel.addElement("Name: " + serverGame.getGameName() + " |    Game type: " + serverGame.getGameType() + " |    Capacity: " + (serverGame.getPlayersNumber()/*-newGame.getConnectedUser*/) + " |    Match type: " + serverGame.getEndingMode());
                        }
                        arrayListOfGameOfServer.add(defaultListModel);
                        objectReader.close();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
                int dialogButton = JOptionPane.showConfirmDialog(null, "Do you want active remember me option?", "Remember me?", JOptionPane.YES_NO_OPTION);
                if (dialogButton == 0) {
                    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase/rememberMe/rememberMe.src"))) {
                        isRememberMeActive = true;
                        objectOutputStream.writeObject(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                loginPageMusic.pause();
                gameFrame(false);
            } else {
                JOptionPane.showMessageDialog(null, "Password and confirm password are not the same.", "Alert", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * this method create main frame of game.
     * this frame show 3 button for user "setting", "vsComputer" and "netWork"
     */
    public void gameFrame(boolean isAfterGame) {
        if (isAfterGame) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("dataBase/" + user.getDataBaseFileName()))) {
                user = (UserPlayer) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        mainMenuMusic.playSound();
        // frame[
        FrameWithBackGround mainGameFrame = new FrameWithBackGround("kit/backGround/3.jpg");
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            mainGameFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainGameFrame.setLocationRelativeTo(null);
        mainGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainGameFrame.setSize(1038, 538);
        mainGameFrame.setLocation(200, 200);
        mainGameFrame.setResizable(false);
        // frame]

        mainGameFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.CENTER;

        JButton vsComputerButton = new JButton(new ImageIcon("kit/button/vsComputer.png"));
        vsComputerButton.addActionListener(vsComputerEvent -> vsComputerButtonAction(mainGameFrame));

        JButton network = new JButton(new ImageIcon("kit/button/netWork.png"));
        network.addActionListener(onlineEvent -> networkButtonAction(mainGameFrame));

        JButton settingButton = new JButton(new ImageIcon("kit/button/setting.png"));
        settingButton.addActionListener(settingEvent -> settingButtonAction(mainGameFrame));

        JButton logOut = new JButton(new ImageIcon("kit/button/logOut.png"));
        logOut.addActionListener(logOutEvent -> logOutButtonAction(mainGameFrame));

        mainGameFrame.add(network, gbc);
        mainGameFrame.add(vsComputerButton, gbc);
        mainGameFrame.add(settingButton, gbc);
        mainGameFrame.add(logOut, gbc);
        mainGameFrame.setVisible(true);
    }

    /**
     * This method log out account of player.
     * Close game frame and show login frame.
     *
     * @param mainGameFrame is main frame of game that we wanna close
     */
    private void logOutButtonAction(JFrame mainGameFrame) {
        mainMenuMusic.pause();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase/" + user.getDataBaseFileName()))) {
            objectOutputStream.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File dir = new File("dataBase/rememberMe");
        File[] allFile = dir.listFiles();
        if (allFile.length != 0) {
            allFile[0].delete();
        }
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        pTextField.setText("");
        pPasswordField = new JPPasswordField("password");
        confirmPPasswordField = new JPPasswordField("password");
        logInAndSignUpPage();
    }

    /**
     * this method run a game that user fight with bot
     *
     * @param mainGameFrame is main frame of game
     */
    private void vsComputerButtonAction(FrameWithBackGround mainGameFrame) {

        secondGameFrame(mainGameFrame);
    }

    /**
     * this method show second frame, frame that user choose lig or death
     *
     * @param mainGameFrame is main frame of game
     */
    private void secondGameFrame(@NotNull JFrame mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        FrameWithBackGround gameTypeMenuFrame = new FrameWithBackGround("kit/backGround/2.jpg");
        setLogoFrame(gameTypeMenuFrame);
        gameTypeMenuFrame.setLocationRelativeTo(null);
        gameTypeMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameTypeMenuFrame.setSize(1000, 600);
        gameTypeMenuFrame.setLocation(200, 200);
        gameTypeMenuFrame.setResizable(false);

        gameTypeMenuFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton solo = new JButton(new ImageIcon("kit/button/solo.png"));
        solo.addActionListener(soloEvent -> soloButtonAction(gameTypeMenuFrame));

        JButton team = new JButton(new ImageIcon("kit/button/team.png"));
        team.addActionListener(teamEvent -> teamButtonAction(gameTypeMenuFrame));

        JLabel guide = new JLabel("Choose Match Type:");
        guide.setFont(new Font("TimesRoman", Font.BOLD, 17));
        guide.setForeground(new Color(60, 5, 3));
        //guide.setForeground(Color.WHITE);
        guide.setBorder(BorderFactory.createLineBorder(new Color(100, 10, 20), 5));

        RadioButtonWithImage radioButton1 = new RadioButtonWithImage(new ImageIcon("kit/button/deathMatch.png"));
        radioButton1.setSelected(true);
        radioButton1.addActionListener(actionEvent -> gameMode = "deathMatch");
        RadioButtonWithImage radioButton2 = new RadioButtonWithImage(new ImageIcon("kit/button/ligMatch.png"));
        radioButton2.addActionListener(actionEvent -> gameMode = "ligMatch");
        RadioButtonWithImage radioButton3 = new RadioButtonWithImage(new ImageIcon("kit/button/soloInMap.png"));
        radioButton3.addActionListener(actionEvent -> gameMode = "soloInMap");
        ButtonGroup buttonGroup = new ButtonGroup();
        radioButton1.addToButtonGroup(buttonGroup);
        radioButton2.addToButtonGroup(buttonGroup);

        gameTypeMenuFrame.add(solo, gbc);
        gameTypeMenuFrame.add(team, gbc);
        gameTypeMenuFrame.add(guide, gbc);
        gameTypeMenuFrame.add(radioButton1, gbc);
        gameTypeMenuFrame.add(radioButton2, gbc);

        gameTypeMenuFrame.setVisible(true);
    }

    /**
     * this method run tank trouble game lig mode
     */
    private void soloButtonAction(@NotNull JFrame gameTypeMenuFrame) {
        mainMenuMusic.pause();
        gameTypeMenuFrame.dispatchEvent(new WindowEvent(gameTypeMenuFrame, WindowEvent.WINDOW_CLOSING));

        JFrame SoloFrame = new JFrame("Number Of Players");
        SoloFrame.setSize(250, 100);
        SoloFrame.setResizable(false);
        SoloFrame.setLocationRelativeTo(null);
        SoloFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        SoloFrame.setContentPane(panel);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        JLabel soloPlayersLabel = new JLabel("Number Of Players: ");
        JPTextField soloPlayersTextField = new JPTextField("Enter an integer...");

        centerPanel.add(soloPlayersLabel);
        centerPanel.add(soloPlayersTextField);


        JButton runSoloGameButton = new JButton("Run game");
        runSoloGameButton.addActionListener(runSoloGameEvent -> runSoloGameButtonAction(SoloFrame, soloPlayersTextField));


        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(runSoloGameButton, BorderLayout.SOUTH);

        SoloFrame.setVisible(true);
    }

    /**
     * This method solo game button action based on input parameters (game type etc)
     *
     * @param soloFrame            is solo game frame
     * @param soloPlayersTextField is text filed that show number of player
     */
    private void runSoloGameButtonAction(JFrame soloFrame, @NotNull JPTextField soloPlayersTextField) {
        if (isInteger(soloPlayersTextField.getText())) {
            soloFrame.dispatchEvent(new WindowEvent(soloFrame, WindowEvent.WINDOW_CLOSING));
            int numberOfPlayers = Integer.parseInt(soloPlayersTextField.getText());
            RunGameHandler runGameHandler;
            if (gameMode.equals("ligMatch")) { // lig
                runGameHandler = new RunGameHandler(numberOfPlayers, "lig", user.getUserTank().getHealth(), this);
            } else { // death match
                runGameHandler = new RunGameHandler(numberOfPlayers, "deathMatch", user.getUserTank().getHealth(), this);
            }
            MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);

            for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
                destructibleWall.setHealth(user.getWallHealth());
            }

            // create and add user
            ArrayList<UserPlayer> userPlayers = new ArrayList<>();
            UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), mapFrame.getTankTroubleMap(), user.getDataBaseFileName());
            userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
            userPlayer.getUserTank().setHealth(user.getUserTank().getHealth());
            userPlayer.setLoseInBotMatch(user.getLoseInBotMatch());
            userPlayer.setWinInBotMatch(user.getWinInBotMatch());
            userPlayer.setLoseInNetworkMatch(user.getLoseInNetworkMatch());
            userPlayer.setWinInNetworkMatch(user.getWinInNetworkMatch());
            userPlayer.setXP(user.getXP());
            userPlayer.setLevel(user.getLevel());
            userPlayer.setGroupNumber(1);
            mapFrame.getTankTroubleMap().setController(userPlayer);
            userPlayers.add(mapFrame.getTankTroubleMap().getController());
            mapFrame.getTankTroubleMap().setUsers(userPlayers);

            // create and add bots
            ArrayList<BotPlayer> bots = new ArrayList<>();
            File dir = new File("kit/tanks");
            File[] allTanks = dir.listFiles();
            Random rand = new Random();
            for (int i = 0; i < numberOfPlayers - 1; i++) {
                File randomTank = allTanks[rand.nextInt(allTanks.length)];
                BotPlayer bot = new BotPlayer("BOT", randomTank.getName(), mapFrame.getTankTroubleMap(), i + 2);
                bot.getAiTank().setBulletDamage(user.getUserTank().getBulletDamage()); //bullet damage
                bot.getAiTank().setHealth(user.getUserTank().getHealth()); //tank health
                bots.add(bot);
            }
            mapFrame.getTankTroubleMap().setBots(bots);
            RunGame runGame = new RunGame(mapFrame, runGameHandler);
            runGameHandler.getRunGameArrayList().add(runGame);
            runGame.run();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input for number of team players.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is action of network button and connect client to the server.
     *
     * @param gameSetting is a game of server that you wanna connect to it.
     */
    private void runNetworkButtonAction(@NotNull ServerGame gameSetting) {
        mainMenuMusic.pause();
        if (user == gameSetting.getCreator()) {//sending settings to server as creator of game
            try (Socket client = new Socket(gameSetting.getServerIP(), gameSetting.getPort())) {
                OutputStream out = client.getOutputStream();
                InputStream in = client.getInputStream();
                ObjectOutputStream socketObjectWriter = new ObjectOutputStream(out);
                socketObjectWriter.writeObject(gameSetting);

            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

        RunGameHandler runGameHandler;
        if (gameSetting.getGameType().equals("solo")) {
            if (gameMode.equals("deathMatch")) {
                runGameHandler = new RunGameHandler(gameSetting.getPlayersNumber(), "deathMatch", user.getUserTank().getHealth(), this);
            } else {//lig mode
                runGameHandler = new RunGameHandler(gameSetting.getPlayersNumber(), "lig", user.getUserTank().getHealth(), this);
            }
        } else { //team mode
            if (gameMode.equals("deathMatch")) {
                runGameHandler = new RunGameHandler(2, "deathMatch", user.getUserTank().getHealth(), this);

            } else {//lig mode
                runGameHandler = new RunGameHandler(2, "lig", user.getUserTank().getHealth(), this);
            }
        }

        String IP = gameSetting.getServerIP();
        int port = gameSetting.getPort();

        MapFrame mapFrame = new MapFrame("Client", true, runGameHandler);
        // create and add user
        UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), mapFrame.getTankTroubleMap(), user.getDataBaseFileName());
        //userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
        //userPlayer.setGroupNumber(1);
        mapFrame.getTankTroubleMap().setController(userPlayer);
        mapFrame.getTankTroubleMap().getUsers().add(mapFrame.getTankTroubleMap().getController());
        mapFrame.getTankTroubleMap().setUsers(mapFrame.getTankTroubleMap().getUsers());

        RunGame runGame = new RunGame(mapFrame, runGameHandler);
        runGameHandler.getRunGameArrayList().add(runGame);
        runGame.run(IP, port);
    }

    /**
     * this method run tank trouble game team mode and with that ending type that you choose (lig or death match)
     */
    private void teamButtonAction(@NotNull JFrame gameTypeMenuFrame) {
        mainMenuMusic.pause();
        gameTypeMenuFrame.dispatchEvent(new WindowEvent(gameTypeMenuFrame, WindowEvent.WINDOW_CLOSING));
        JFrame teamFrame = new JFrame("Team Players");
        teamFrame.setSize(250, 100);
        teamFrame.setResizable(false);
        teamFrame.setLocationRelativeTo(null);
        teamFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        teamFrame.setContentPane(panel);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        JLabel teamPlayersLabel = new JLabel(" Number of team players : ");
        JPTextField teamPlayersTextField = new JPTextField("Enter an integer...");

        centerPanel.add(teamPlayersLabel);
        centerPanel.add(teamPlayersTextField);


        JButton runTeamGameButton = new JButton("Run game");
        runTeamGameButton.addActionListener(runTeamGameEvent -> runTeamGameButtonAction(teamFrame, teamPlayersTextField));


        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(runTeamGameButton, BorderLayout.SOUTH);

        teamFrame.setVisible(true);
    }

    /**
     * This method is action of run button in team frame and create needed bots and add them to the game.
     *
     * @param teamFrame            is team frame of game that you wanna close it
     * @param teamPlayersTextField is textfield that have number of players in each taem
     */
    private void runTeamGameButtonAction(JFrame teamFrame, @NotNull JPTextField teamPlayersTextField) {
        if (isInteger(teamPlayersTextField.getText())) {

            teamFrame.dispatchEvent(new WindowEvent(teamFrame, WindowEvent.WINDOW_CLOSING));
            int numberOfTeamPlayers = Integer.parseInt(teamPlayersTextField.getText());

            RunGameHandler runGameHandler;
            if (gameMode.equals("deathMatch")) {
                runGameHandler = new RunGameHandler(2, "deathMatch", user.getUserTank().getHealth(), this);
            } else { //ligMatch
                runGameHandler = new RunGameHandler(2, "lig", user.getUserTank().getHealth(), this);
            }
            MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);
            for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
                destructibleWall.setHealth(user.getWallHealth());
            }

            // create and add user
            ArrayList<UserPlayer> userPlayers = new ArrayList<>();
            UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), mapFrame.getTankTroubleMap(), user.getDataBaseFileName());
            userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
            userPlayer.getUserTank().setHealth(user.getUserTank().getHealth());
            userPlayer.setLoseInBotMatch(user.getLoseInBotMatch());
            userPlayer.setWinInBotMatch(user.getWinInBotMatch());
            userPlayer.setLoseInNetworkMatch(user.getLoseInNetworkMatch());
            userPlayer.setWinInNetworkMatch(user.getWinInNetworkMatch());
            userPlayer.setXP(user.getXP());
            userPlayer.setLevel(user.getLevel());
            userPlayer.setGroupNumber(1);
            mapFrame.getTankTroubleMap().setController(userPlayer);
            userPlayers.add(mapFrame.getTankTroubleMap().getController());
            mapFrame.getTankTroubleMap().setUsers(userPlayers);

            // bots
            ArrayList<BotPlayer> bots = new ArrayList<>();

            // create user's team (friends bots)
            File dir = new File("kit/tanks");
            File[] allTanks = dir.listFiles();
            Random rand = new Random();
            for (int i = 0; i < numberOfTeamPlayers - 1; i++) {
                File randomTank = allTanks[rand.nextInt(allTanks.length)];
                BotPlayer bot = new BotPlayer("Friend BOT", randomTank.getName(), mapFrame.getTankTroubleMap(), 1);
                bot.getAiTank().setBulletDamage(user.getUserTank().getBulletDamage()); //bullet damage
                bot.getAiTank().setHealth(user.getUserTank().getHealth()); //tank health
                bots.add(bot);
            }

            // create another team
            for (int i = 0; i < numberOfTeamPlayers; i++) {
                File randomTank = allTanks[rand.nextInt(allTanks.length)];
                BotPlayer bot = new BotPlayer("BOT", randomTank.getName(), mapFrame.getTankTroubleMap(), 2);
                bot.getAiTank().setBulletDamage(user.getUserTank().getBulletDamage()); //bullet damage
                bot.getAiTank().setHealth(user.getUserTank().getHealth()); //tank health
                bots.add(bot);
            }

            mapFrame.getTankTroubleMap().setUsers(userPlayers);
            mapFrame.getTankTroubleMap().setBots(bots);

            RunGame runGame = new RunGame(mapFrame, runGameHandler);
            runGameHandler.getRunGameArrayList().add(runGame);
            runGame.run();

        } else {
            JOptionPane.showMessageDialog(null, "Invalid input for number of team players.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * This method check input string is string of integer number or not
     *
     * @param s is string that uou wanna check
     * @return answer as boolean
     */
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * This method close game frame and show frame of list of servers
     *
     * @param mainGameFrame is main frame of game that it must be closed
     */
    private void networkButtonAction(@NotNull FrameWithBackGround mainGameFrame) {

        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        JFrame networkFrame = new JFrame("Network");
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            networkFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkFrame.setLayout(new BorderLayout());
        networkFrame.setLocationRelativeTo(null);
        networkFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        networkFrame.setLocation(200, 200);
        networkFrame.setSize(520, 500);

        JLabel guide = new JLabel("Choose server:");
        guide.setFont(new Font("TimesRoman", Font.BOLD, 15));
        networkFrame.add(guide, BorderLayout.NORTH);

        JList<String> serverList = new JList<>(serverListModel);
        networkFrame.add(serverList, BorderLayout.CENTER);
        serverList.addListSelectionListener(serverListEvent -> selectActionForServerList(networkFrame, serverList.getSelectedIndex()));

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));

        JButton addServer = new JButton("Add server");
        addServer.addActionListener(addServerEvent -> addServerButtonAction(networkFrame));

        JButton backToMainFrameButton = new JButton("Back to main menu");
        backToMainFrameButton.addActionListener(backEvent -> backToSettingButtonAction(mainGameFrame, networkFrame));

        southPanel.add(addServer);
        southPanel.add(backToMainFrameButton);

        networkFrame.add(southPanel, BorderLayout.SOUTH);

        networkFrame.setVisible(true);
    }

    /**
     * This method is action selection of list of server.
     * open server that you want and show it's game.
     *
     * @param networkFrame        is network frame that must be closed
     * @param selectedServerIndex is index of server that you choose from the server
     */
    private void selectActionForServerList(@NotNull JFrame networkFrame, int selectedServerIndex) {
        networkFrame.dispatchEvent(new WindowEvent(networkFrame, WindowEvent.WINDOW_CLOSING));
        JFrame gameOfServerFrame = new JFrame("Game of server");
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            gameOfServerFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameOfServerFrame.setLayout(new BorderLayout());
        gameOfServerFrame.setLocationRelativeTo(null);
        gameOfServerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameOfServerFrame.setSize(520, 500);

        JLabel guide = new JLabel("Choose game:");
        guide.setFont(new Font("TimesRoman", Font.BOLD, 15));
        gameOfServerFrame.add(guide, BorderLayout.NORTH);

        JList<String> gameList = new JList<>(arrayListOfGameOfServer.get(selectedServerIndex));
        gameOfServerFrame.add(gameList, BorderLayout.CENTER);
        gameList.addListSelectionListener(gameListEvent -> {
            if (!gameListEvent.getValueIsAdjusting()) {
                runNetworkButtonAction(serverConfigs.get(selectedServerIndex).getServerGames().get(gameList.getSelectedIndex()));
            }
        });

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));

        JButton addGame = new JButton("Add game");
        addGame.addActionListener(addGameEvent -> addGameButtonAction(gameOfServerFrame, selectedServerIndex));

        JButton backToServersButton = new JButton("Back to Servers");
        backToServersButton.addActionListener(backEvent -> backToSettingButtonAction(networkFrame, gameOfServerFrame));

        southPanel.add(addGame);
        southPanel.add(backToServersButton);

        gameOfServerFrame.add(southPanel, BorderLayout.SOUTH);

        gameOfServerFrame.setVisible(true);
    }


    /**
     * This method is action of add game button and show a frame that with you can enter game information.
     *
     * @param gameOfServerFrame   is frame that show servers and must be closed
     * @param selectedServerIndex is index of server that you choose from the server
     */
    private void addGameButtonAction(@NotNull JFrame gameOfServerFrame, int selectedServerIndex) {
        ServerGame newGame = new ServerGame();
        gameOfServerFrame.dispatchEvent(new WindowEvent(gameOfServerFrame, WindowEvent.WINDOW_CLOSING));
        FrameWithBackGround addGameFrame = new FrameWithBackGround("kit/backGround/1.jpg");
        setLogoFrame(addGameFrame);
        addGameFrame.setLocationRelativeTo(null);
        addGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addGameFrame.setSize(500, 600);
        addGameFrame.setResizable(false);
        addGameFrame.setLayout(new BorderLayout());

        addGameFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel matchPort = new JLabel("Port");
        matchPort.setFont(new Font("TimesRoman", Font.BOLD, 17));
        matchPort.setForeground(new Color(60, 5, 3));

        JPTextField matchPortField = new JPTextField("Game Port.....");

        JLabel gameNameLabel = new JLabel("Game Name:");
        gameNameLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        gameNameLabel.setForeground(new Color(60, 5, 3));

        JPTextField gameNameField = new JPTextField("Game Name.....");

        JLabel gameTypeLabel = new JLabel("Choose Game Type:");
        gameTypeLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        gameTypeLabel.setForeground(new Color(60, 5, 3));

        JRadioButton soloRadio = new JRadioButton("Solo");
        soloRadio.setSelected(true);
        JRadioButton teamRadio = new JRadioButton("Team");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(soloRadio);
        buttonGroup.add(teamRadio);

        JLabel numberOfPlayers = new JLabel("Number Of Players:");
        numberOfPlayers.setFont(new Font("TimesRoman", Font.BOLD, 17));
        numberOfPlayers.setForeground(new Color(60, 5, 3));

        JPTextField numberOfPlayersTextField = new JPTextField("Enter An Integer...");

        soloRadio.addActionListener(soloActionEvent -> {

            numberOfPlayers.setText("Number Of Players:");
        });

        teamRadio.addActionListener(teamActionEvent -> {

            numberOfPlayers.setText("Number Of Players In Each Teams:");
        });

        JLabel matchTypeLabel = new JLabel("Choose Match Type (Ending Mode):");
        matchTypeLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        matchTypeLabel.setForeground(new Color(60, 5, 3));


        JRadioButton deathMatchRadio = new JRadioButton("Death Match");
        deathMatchRadio.setSelected(true);
        deathMatchRadio.addActionListener(actionEvent -> gameMode = "deathMatch");
        JRadioButton ligMatchRadio = new JRadioButton("Lig Match");
        ligMatchRadio.addActionListener(actionEvent -> gameMode = "ligMatch");
        JRadioButton soloInMapMatchRadio = new JRadioButton("Solo in map match");
        soloInMapMatchRadio.addActionListener(actionEvent -> gameMode = "soloInMap");
        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(deathMatchRadio);
        buttonGroup2.add(ligMatchRadio);
        buttonGroup2.add(soloInMapMatchRadio);

        JLabel tankHealthTextLabel = new JLabel(" Health of tank : ");
        tankHealthTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        tankHealthTextLabel.setForeground(new Color(60, 5, 3));


        JSlider tankHealthSlider = new JSlider(50, 500, user.getUserTank().getHealth());
        tankHealthSlider.setMajorTickSpacing(50);
        tankHealthSlider.setMinorTickSpacing(10);
        tankHealthSlider.setPaintTrack(true);
        tankHealthSlider.setPaintTicks(true);
        tankHealthSlider.setPaintLabels(true);
        tankHealthSlider.addChangeListener(changeEvent -> changeActionTankHealthSlider(tankHealthSlider.getValue()));


        JLabel bulletDamageTextLabel = new JLabel(" Damage of bullet : ");
        bulletDamageTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        bulletDamageTextLabel.setForeground(new Color(60, 5, 3));


        JSlider bulletDamageSlider = new JSlider(5, 55, user.getUserTank().getBulletDamage());
        bulletDamageSlider.setMajorTickSpacing(10);
        bulletDamageSlider.setMinorTickSpacing(2);
        bulletDamageSlider.setPaintTrack(true);
        bulletDamageSlider.setPaintTicks(true);
        bulletDamageSlider.setPaintLabels(true);
        bulletDamageSlider.addChangeListener(changeEvent -> changeActionBulletDamageSlider(bulletDamageSlider.getValue()));

        JLabel wallHealthTextLabel = new JLabel(" health of destructible wall : ");
        wallHealthTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        wallHealthTextLabel.setForeground(new Color(60, 5, 3));

        JSlider wallHealthSlider = new JSlider(30, 100, user.getWallHealth());
        wallHealthSlider.setMajorTickSpacing(10);
        wallHealthSlider.setMinorTickSpacing(2);
        wallHealthSlider.setPaintTrack(true);
        wallHealthSlider.setPaintTicks(true);
        wallHealthSlider.setPaintLabels(true);
        wallHealthSlider.addChangeListener(changeEvent -> changeActionWallHealthSlider(wallHealthSlider.getValue()));

        JButton createGame = new JButton("Create Game");
        createGame.addActionListener(e -> {
            newGame.setGameName(gameNameField.getText());
            if (soloRadio.isSelected()) {
                newGame.setGameType("solo");
            } else newGame.setGameType("team");
            if (soloRadio.isSelected()) {
                newGame.setPlayersNumber(Integer.parseInt(numberOfPlayersTextField.getText()));
            } else {
                newGame.setPlayersNumber(2 * Integer.parseInt(numberOfPlayersTextField.getText()));
            }
            if (deathMatchRadio.isSelected()) {
                newGame.setEndingMode("deathMatch");
            } else if (ligMatchRadio.isSelected()) newGame.setEndingMode("ligMatch");
            else newGame.setEndingMode("soloInMap");
            newGame.setTankHealth(user.getUserTank().getHealth());
            newGame.setDWallHealth(user.getWallHealth());
            newGame.setBulletDamage(user.getUserTank().getBulletDamage());
            newGame.setPort(Integer.parseInt(matchPortField.getText()));
            serverConfigs.get(selectedServerIndex).addNewGame(newGame);
            arrayListOfGameOfServer.get(selectedServerIndex).addElement("Name: " + newGame.getGameName() + " |    Game type: " + newGame.getGameType() + " |    Capacity: " + (newGame.getPlayersNumber()/*-newGame.getConnectedUser*/) + " |    Match type: " + newGame.getEndingMode());
            addGameFrame.dispose();
            gameOfServerFrame.setVisible(true);
            newGame.setCreator(user);

        });
        addGameFrame.add(matchPort, gbc);
        addGameFrame.add(matchPortField, gbc);
        addGameFrame.add(gameNameLabel, gbc);
        addGameFrame.add(gameNameField, gbc);
        addGameFrame.add(gameTypeLabel, gbc);
        addGameFrame.add(soloRadio, gbc);
        addGameFrame.add(teamRadio, gbc);
        addGameFrame.add(numberOfPlayers, gbc);
        addGameFrame.add(numberOfPlayersTextField, gbc);
        addGameFrame.add(gameTypeLabel, gbc);
        addGameFrame.add(deathMatchRadio, gbc);
        addGameFrame.add(ligMatchRadio, gbc);
        addGameFrame.add(soloInMapMatchRadio, gbc);
        addGameFrame.add(tankHealthTextLabel, gbc);
        addGameFrame.add(tankHealthSlider, gbc);
        addGameFrame.add(bulletDamageTextLabel, gbc);
        addGameFrame.add(bulletDamageSlider, gbc);
        addGameFrame.add(wallHealthTextLabel, gbc);
        addGameFrame.add(wallHealthSlider, gbc);
        addGameFrame.add(createGame, gbc);
        addGameFrame.setVisible(true);

    }


    /**
     * This method do action of setting button.
     * make new frame for show some detail and also this frame has 2 buttons 'change tank' and 'back'
     *
     * @param mainGameFrame is min frame of game and must be closed
     */
    private void settingButtonAction(@NotNull FrameWithBackGround mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        level = new JLabel(" " + user.getLevel());
        timePlayLabel = new JLabel(" " + user.getTimePlay());
        winVSComputerLabel = new JLabel(" " + user.getWinInBotMatch());
        loseVSComputerLabel = new JLabel(" " + user.getLoseInBotMatch());
        winInOnlineModeLabel = new JLabel(" " + user.getWinInNetworkMatch());
        loseInOnlineModeLabel = new JLabel(" " + user.getLoseInNetworkMatch());
        shapeOfTankLabel = new JLabel(new ImageIcon("kit/smallTanks/" + user.getColor() + "/normal.png"));
        JFrame settingFrame = new JFrame("Setting");
        settingFrame.setLocationRelativeTo(null);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.setLocation(200, 200);
        settingFrame.setSize(600, 680);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 2, 0, 10));
        settingFrame.setContentPane(panel);

        JPanel centerPanel = new JPanel(new GridLayout(10, 2));

        JLabel levelTextLabel = new JLabel(" Level:");
        levelTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(levelTextLabel);
        centerPanel.add(level);

        JLabel TimePlayTextLabel = new JLabel(" Time play :");
        TimePlayTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(TimePlayTextLabel);
        centerPanel.add(timePlayLabel);

        JLabel winVSComputerTextLabel = new JLabel(" Win vs computer :");
        winVSComputerTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        winVSComputerTextLabel.setForeground(new Color(10, 100, 50));
        centerPanel.add(winVSComputerTextLabel);
        centerPanel.add(winVSComputerLabel);

        JLabel loseVSComputerTextLabel = new JLabel(" Lose vs computer :");
        loseVSComputerTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        loseVSComputerTextLabel.setForeground(new Color(100, 40, 40));
        centerPanel.add(loseVSComputerTextLabel);
        centerPanel.add(loseVSComputerLabel);

        JLabel winInOnlineModeTextLabel = new JLabel(" Win in online mode :");
        winInOnlineModeTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        winInOnlineModeTextLabel.setForeground(new Color(10, 100, 50));
        centerPanel.add(winInOnlineModeTextLabel);
        centerPanel.add(winInOnlineModeLabel);

        JLabel loseInOnlineModeTextLabel = new JLabel(" Lose in online mode :");
        loseInOnlineModeTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        loseInOnlineModeTextLabel.setForeground(new Color(100, 40, 40));
        centerPanel.add(loseInOnlineModeTextLabel);
        centerPanel.add(loseInOnlineModeLabel);

        JLabel shapeOfTankTextLabel = new JLabel(" Shape of tank :");
        shapeOfTankTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(shapeOfTankTextLabel);
        centerPanel.add(shapeOfTankLabel);

        JLabel tankHealthTextLabel = new JLabel(" Health of tank : ");
        tankHealthTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(tankHealthTextLabel);

        JSlider tankHealthSlider = new JSlider(50, 500, user.getUserTank().getHealth());
        tankHealthSlider.setMajorTickSpacing(50);
        tankHealthSlider.setMinorTickSpacing(10);
        tankHealthSlider.setPaintTrack(true);
        tankHealthSlider.setPaintTicks(true);
        tankHealthSlider.setPaintLabels(true);
        tankHealthSlider.addChangeListener(changeEvent -> changeActionTankHealthSlider(tankHealthSlider.getValue()));
        centerPanel.add(tankHealthSlider);

        JLabel bulletDamageTextLabel = new JLabel(" Damage of bullet : ");
        bulletDamageTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(bulletDamageTextLabel);

        JSlider bulletDamageSlider = new JSlider(5, 55, user.getUserTank().getBulletDamage());
        bulletDamageSlider.setMajorTickSpacing(10);
        bulletDamageSlider.setMinorTickSpacing(2);
        bulletDamageSlider.setPaintTrack(true);
        bulletDamageSlider.setPaintTicks(true);
        bulletDamageSlider.setPaintLabels(true);
        bulletDamageSlider.addChangeListener(changeEvent -> changeActionBulletDamageSlider(bulletDamageSlider.getValue()));
        centerPanel.add(bulletDamageSlider);

        JLabel wallHealthTextLabel = new JLabel(" health of destructible wall : ");
        wallHealthTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(wallHealthTextLabel);

        JSlider wallHealthSlider = new JSlider(30, 100, user.getWallHealth());
        wallHealthSlider.setMajorTickSpacing(10);
        wallHealthSlider.setMinorTickSpacing(2);
        wallHealthSlider.setPaintTrack(true);
        wallHealthSlider.setPaintTicks(true);
        wallHealthSlider.setPaintLabels(true);
        wallHealthSlider.addChangeListener(changeEvent -> changeActionWallHealthSlider(wallHealthSlider.getValue()));
        centerPanel.add(wallHealthSlider);

        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        JButton changeTankButton = new JButton("Change tank");
        changeTankButton.addActionListener(changeTankEvent -> changeTankButtonAction(settingFrame));
        JButton networkButton = new JButton("Network");
        networkButton.addActionListener(networkEvent -> networkButtonAction(settingFrame));
        JButton backToMainMenuButton = new JButton("Back to main menu");
        backToMainMenuButton.addActionListener(backEvent -> backToMainMenuButtonAction(settingFrame, mainGameFrame));
        southPanel.add(networkButton);
        southPanel.add(changeTankButton);
        southPanel.add(backToMainMenuButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        settingFrame.setVisible(true);
    }

    /**
     * This method is action of slider of tank health.
     *
     * @param tankHealth is health of tank that player choose with slider
     */
    private void changeActionTankHealthSlider(int tankHealth) {
        user.getUserTank().setHealth(tankHealth);
        saveAUser(user, "dataBase/" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase/rememberMe/rememberMe.src");
        }
    }

    /**
     * This method is action of slider of bullet damage.
     *
     * @param bulletDamage is damage of bullet player you choose with slider
     */
    private void changeActionBulletDamageSlider(int bulletDamage) {
        user.getUserTank().setBulletDamage(bulletDamage);
        saveAUser(user, "dataBase/" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase/rememberMe/rememberMe.src");
        }
    }

    /**
     * This method is action of slider of wall health.
     *
     * @param wallHealth is health of wall player you choose with slider
     */
    private void changeActionWallHealthSlider(int wallHealth) {
        user.setWallHealth(wallHealth);
        saveAUser(user, "dataBase/" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase/rememberMe/rememberMe.src");
        }
    }

    /**
     * This method save a user in input path of file
     *
     * @param userPlayer is player that we wanna save
     * @param path       is path of file that you wanna save user in.
     */
    private void saveAUser(UserPlayer userPlayer, String path) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeObject(userPlayer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method do action of "backToMainMenu" button.
     *
     * @param settingFrame  is setting frame
     * @param mainGameFrame is main frame of game
     */
    private void backToMainMenuButtonAction(@NotNull JFrame settingFrame, @NotNull FrameWithBackGround
            mainGameFrame) {
        settingFrame.dispatchEvent(new WindowEvent(settingFrame, WindowEvent.WINDOW_CLOSING));
        mainGameFrame.setVisible(true);
    }

    /**
     * This method is action of net work button in setting frame and close setting frame and show list of server.
     *
     * @param settingFrame is setting frame of game and must be closed
     */
    private void networkButtonAction(@NotNull JFrame settingFrame) {
        settingFrame.dispatchEvent(new WindowEvent(settingFrame, WindowEvent.WINDOW_CLOSING));
        JFrame networkSettingFrame = new JFrame("Network");
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            networkSettingFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        networkSettingFrame.setLayout(new BorderLayout());
        networkSettingFrame.setLocationRelativeTo(null);
        networkSettingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        networkSettingFrame.setSize(520, 500);

        JLabel guide = new JLabel("server name      server IP");
        guide.setFont(new Font("TimesRoman", Font.BOLD, 15));
        networkSettingFrame.add(guide, BorderLayout.NORTH);

        JList<String> serverList = new JList<>(serverListModel);
        networkSettingFrame.add(serverList, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));

        JButton addServer = new JButton("Add server");
        addServer.addActionListener(addServerEvent -> addServerButtonAction(networkSettingFrame));

        JButton backToSettingButton = new JButton("Back to setting");
        backToSettingButton.addActionListener(backEvent -> backToSettingButtonAction(settingFrame, networkSettingFrame));

        southPanel.add(addServer);
        southPanel.add(backToSettingButton);

        networkSettingFrame.add(southPanel, BorderLayout.SOUTH);

        networkSettingFrame.setVisible(true);
    }

    /**
     * This method do action of add server button and show a frame for enter server then add server based on input parameter to the list.
     *
     * @param networkSettingFrame is network setting frame of game and must be closed
     */
    private void addServerButtonAction(JFrame networkSettingFrame) {
        JFrame addServerFrame = new JFrame("Add server");
        try {
            Image logo = ImageIO.read(new File("kit/logo.png"));
            addServerFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addServerFrame.setSize(250, 150);
        addServerFrame.setResizable(false);
        addServerFrame.setLocationRelativeTo(null);
        addServerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        addServerFrame.setContentPane(panel);

        JLabel serverNameLabel = new JLabel(" Server name : ");
        JPTextField serverNameTextField = new JPTextField("Server name...");
        JLabel serverIPLabel = new JLabel(" Server IP : ");
        JPTextField serverIPTextField = new JPTextField("Server IP...");

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        centerPanel.add(serverNameLabel);
        centerPanel.add(serverNameTextField);
        centerPanel.add(serverIPLabel);
        centerPanel.add(serverIPTextField);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        JButton add = new JButton("Add");
        add.addActionListener(addEvent -> addButtonAction(addServerFrame, serverNameTextField, serverIPTextField, networkSettingFrame));

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(cancelEvent -> addServerFrame.dispatchEvent(new WindowEvent(addServerFrame, WindowEvent.WINDOW_CLOSING)));

        southPanel.add(add);
        southPanel.add(cancel);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        addServerFrame.setVisible(true);
    }

    /**
     * This method add server to the server list.
     *
     * @param addServerFrame      is server frame and muse be shown
     * @param serverNameTextField is textfield that has name of server
     * @param serverIPTextField   is textfield that has IP of server
     * @param networkSettingFrame is network frame of setting and must be shown
     */
    private void addButtonAction(@NotNull JFrame addServerFrame, @NotNull JPTextField serverNameTextField, @NotNull JPTextField
            serverIPTextField, @NotNull JFrame networkSettingFrame) {
        addServerFrame.dispatchEvent(new WindowEvent(addServerFrame, WindowEvent.WINDOW_CLOSING));
        serverListModel.addElement("" + serverNameTextField.getText() + "                           " + serverIPTextField.getText());
        arrayListOfGameOfServer.add(new DefaultListModel<>());
        serverConfigs.add(new ServerConfigs(serverNameTextField.getText(), serverIPTextField.getText()));
        networkSettingFrame.setVisible(true);
    }

    /**
     * this method do action of "changeTank" button.
     *
     * @param settingFrame is setting frame
     */
    private void changeTankButtonAction(@NotNull JFrame settingFrame) {
        settingFrame.dispatchEvent(new WindowEvent(settingFrame, WindowEvent.WINDOW_CLOSING));
        JFrame chooseTankFrame = new JFrame("Change Tank");
        chooseTankFrame.setLayout(new BorderLayout());
        chooseTankFrame.setLocationRelativeTo(null);
        chooseTankFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chooseTankFrame.setSize(520, 500);

        JLabel guide = new JLabel("Choose your tank:");
        guide.setFont(new Font("TimesRoman", Font.BOLD, 15));
        chooseTankFrame.add(guide, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Brown");
        listModel.addElement("Green");
        listModel.addElement("BlueLight");
        listModel.addElement("Blue");
        listModel.addElement("Gold");
        listModel.addElement("Pink");
        listModel.addElement("Red");
        JList<String> tanksList = new JList<>(listModel);
        tanksList.setSelectedValue(user.getColor(), false);
        centerPanel.add(tanksList, BorderLayout.NORTH);
        chooseTankFrame.add(centerPanel, BorderLayout.CENTER);

        JPanel centerOfCenterPanel = new JPanel();
        centerOfCenterPanel.setLayout(new GridLayout(1, 2));
        JLabel normalLabel = new JLabel("                           Normal form");
        normalLabel.setFont(new Font("TimesRoman", Font.BOLD, 12));
        JLabel laserLabel = new JLabel("                             Laser form");
        laserLabel.setFont(new Font("TimesRoman", Font.BOLD, 12));
        centerOfCenterPanel.add(normalLabel);
        centerOfCenterPanel.add(laserLabel);
        centerPanel.add(centerOfCenterPanel, BorderLayout.CENTER);

        JPanel southOfCenterPanel = new JPanel();
        southOfCenterPanel.setLayout(new GridLayout(1, 2));
        JLabel showNormalTank = new JLabel(new ImageIcon("kit/tanks/" + user.getColor() + "/normal.png"));
        JLabel showLaserTank = new JLabel(new ImageIcon("kit/tanks/" + user.getColor() + "/laser.png"));
        southOfCenterPanel.add(showNormalTank);
        southOfCenterPanel.add(showLaserTank);
        centerPanel.add(southOfCenterPanel, BorderLayout.SOUTH);

        tanksList.addListSelectionListener(tankBoxItemEvent -> selectActionForTanksList(showNormalTank, showLaserTank, tanksList.getSelectedValue())); //bag.......???

        JButton backToSettingButton = new JButton("Back to setting");
        backToSettingButton.addActionListener(backEvent -> backToSettingButtonAction(settingFrame, chooseTankFrame));
        chooseTankFrame.add(backToSettingButton, BorderLayout.SOUTH);

        chooseTankFrame.setVisible(true);
    }

    /**
     * this method is action for list in the change tank frame.
     *
     * @param showNormalTank is image of normal form of tank in a label
     * @param showLaserTank  is image of normal laser of tank in a label
     * @param selectedValue  show selected item of list
     */
    private void selectActionForTanksList(@NotNull JLabel showNormalTank, @NotNull JLabel showLaserTank, String
            selectedValue) {
        showNormalTank.setIcon(new ImageIcon("kit/tanks/" + selectedValue + "/normal.png"));
        showLaserTank.setIcon(new ImageIcon("kit/tanks/" + selectedValue + "/laser.png"));
        shapeOfTankLabel.setIcon(new ImageIcon("kit/smallTanks/" + selectedValue + "/normal.png"));
        user.setColor(selectedValue);
        saveAUser(user, "dataBase/" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase/rememberMe/rememberMe.src");
        }
    }

    /**
     * this method do action of "backToMainMenu" button.
     *
     * @param settingFrame    is setting frame
     * @param chooseTankFrame is choose tank frame
     */
    private void backToSettingButtonAction(@NotNull JFrame settingFrame, @NotNull JFrame chooseTankFrame) {
        chooseTankFrame.dispatchEvent(new WindowEvent(chooseTankFrame, WindowEvent.WINDOW_CLOSING));
        settingFrame.setVisible(true);
    }

    /**
     * This public method run use for running interface and just call "rememberMe" method.
     */
    public void runAndShow() {
        rememberMe();
    }

}
