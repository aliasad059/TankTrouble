package graphic;

import Network.Server;
import logic.*;
import logic.Engine.MapFrame;
import logic.Player.BotPlayer;
import logic.Player.UserPlayer;
import logic.Wall.DestructibleWall;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class represent interface of "Tank Trouble" game.
 * Ont the other word this class is main part of graphic.
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
    private boolean isNetWork;
    private String gameMode;
    private SoundsOfGame mainMenuMusic;
    private SoundsOfGame loginPageMusic;
    private boolean isRememberMeActive;

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
        //set look and fill]

        //initialization[
        pTextField = new JPTextField("User name");
        pPasswordField = new JPPasswordField("Password");
        confirmPPasswordField = new JPPasswordField("Confirm");
        level = new JLabel(" 0");
        timePlayLabel = new JLabel(" 0");
        winVSComputerLabel = new JLabel(" 0");
        loseVSComputerLabel = new JLabel(" 0");
        winInOnlineModeLabel = new JLabel(" 0");
        loseInOnlineModeLabel = new JLabel(" 0");
        shapeOfTankLabel = new JLabel(new ImageIcon("kit\\smallTanks\\Brown\\normal.png"));
        gameMode = "deathMatch";
        loginPageMusic = new SoundsOfGame("loginPage", true);
        mainMenuMusic = new SoundsOfGame("mainMenu", true);
        isRememberMeActive = false;
        //initialization]
    }

    private void graphicHandler() { //just for test
        rememberME();
    }

    private void rememberME() {
        // is there remember me?
        File dir = new File("dataBase\\rememberMe");
        File[] allFile = dir.listFiles();
        if (allFile.length != 0) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("dataBase\\rememberMe\\rememberMe.src"))) {
                user = (UserPlayer) objectInputStream.readObject();
                isRememberMeActive = true;
                gameFrame();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
            Image logo = ImageIO.read(new File("kit\\logo.png"));
            logInAndSignUpPageFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logInAndSignUpPageFrame.setLocationRelativeTo(null);
        logInAndSignUpPageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel loginBackgroundLabel = new JLabel(new ImageIcon("kit\\backGround\\Fish.gif"));
        logInAndSignUpPageFrame.setContentPane(loginBackgroundLabel);

        JButton login = new JButton("Login");
        //JButton login = new JButton(new ImageIcon("kit\\button\\logIn.png"));
        login.addActionListener(loginEvent -> loginButtonAction());

        JButton signUp = new JButton("SingUp");
        //JButton signUp = new JButton(new ImageIcon("kit\\button\\signUp.png"));
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
     * This method is action of login button in login page.
     */
    private void loginButtonAction() {
        logInAndSignUpPageFrame.dispatchEvent(new WindowEvent(logInAndSignUpPageFrame, WindowEvent.WINDOW_CLOSING));
        JFrame loginFrame = new JFrame("Login");
        try {
            Image logo = ImageIO.read(new File("kit\\logo.png"));
            loginFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        File[] files = srcFileFinder("dataBase\\");
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
                        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\rememberMe\\rememberMe.src"))) {
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
                    shapeOfTankLabel = new JLabel(new ImageIcon("kit\\smallTanks\\" + user.getColor() + "\\normal.png"));

                    loginPageMusic.pause();
                    gameFrame();
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
            Image logo = ImageIO.read(new File("kit\\logo.png"));
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
     * This method do action of register button and make a new file in data base based on input information
     *
     * @param loginFrame
     */
    private void registerButtonAction(JFrame loginFrame) {
        File[] files = srcFileFinder("dataBase\\");
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
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\" + (files.length + 1) + ".src"))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < pPasswordField.getPassword().length; i++) {
                        stringBuilder.append(pPasswordField.getPassword()[i]);
                    }
                    user = new UserPlayer(pTextField.getText(), stringBuilder.toString(), "Brown", new TankTroubleMap("./maps/map3.txt", false, LocalDateTime.now(), new RunGameHandler(1, "deathMatch", 100)), "" + (files.length + 1) + ".src");
                    objectOutputStream.writeObject(user);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
                int dialogButton = JOptionPane.showConfirmDialog(null, "Do you want active remember me option?", "Remember me?", JOptionPane.YES_NO_OPTION);
                if (dialogButton == 0) {
                    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\rememberMe\\rememberMe.src"))) {
                        isRememberMeActive = true;
                        objectOutputStream.writeObject(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                loginPageMusic.pause();
                gameFrame();
            } else {
                JOptionPane.showMessageDialog(null, "Password and confirm password are not the same.", "Alert", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * this method create main frame of game.
     * this frame show 3 button for user "setting", "vsComputer" and "netWork"
     */
    private void gameFrame() {
        mainMenuMusic.playSound();
        // frame[
        FrameWithBackGround mainGameFrame = new FrameWithBackGround("kit\\backGround\\3.jpg");
        try {
            Image logo = ImageIO.read(new File("kit\\logo.png"));
            mainGameFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainGameFrame.setLocationRelativeTo(null);
        mainGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainGameFrame.setSize(1038, 538);
        mainGameFrame.setResizable(false);
        // frame]

        mainGameFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.CENTER;

        JButton vsComputerButton = new JButton(new ImageIcon("kit\\button\\vsComputer.png"));
        vsComputerButton.addActionListener(vsComputerEvent -> vsComputerButtonAction(mainGameFrame));

        JButton network = new JButton(new ImageIcon("kit\\button\\netWork.png"));
        network.addActionListener(onlineEvent -> networkButtonAction(mainGameFrame));

        JButton settingButton = new JButton(new ImageIcon("kit\\button\\setting.png"));
        settingButton.addActionListener(settingEvent -> settingButtonAction(mainGameFrame));

        JButton logOut = new JButton(new ImageIcon("kit\\button\\logOut.png"));
        logOut.addActionListener(logOutEvent -> logOutButtonAction(mainGameFrame));

        mainGameFrame.add(network, gbc);
        mainGameFrame.add(vsComputerButton, gbc);
        mainGameFrame.add(settingButton, gbc);
        mainGameFrame.add(logOut, gbc);
        mainGameFrame.setVisible(true);
    }

    private void logOutButtonAction(JFrame mainGameFrame) {
        mainMenuMusic.pause();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\" + user.getDataBaseFileName()))) {
            objectOutputStream.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File dir = new File("dataBase\\rememberMe");
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
        isNetWork = false;
        secondGameFrame(mainGameFrame);
    }

    /**
     * this method show second frame, frame that user choose lig or death
     *
     * @param mainGameFrame is main frame of game
     */
    private void secondGameFrame(@NotNull FrameWithBackGround mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        FrameWithBackGround gameTypeMenuFrame = new FrameWithBackGround("kit\\backGround\\2.jpg");
        try {
            Image logo = ImageIO.read(new File("kit\\logo.png"));
            gameTypeMenuFrame.setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameTypeMenuFrame.setLocationRelativeTo(null);
        gameTypeMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameTypeMenuFrame.setSize(1000, 600);
        gameTypeMenuFrame.setResizable(false);

        gameTypeMenuFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton solo = new JButton(new ImageIcon("kit\\button\\solo.png"));
        solo.addActionListener(soloEvent -> soloButtonAction(gameTypeMenuFrame));

        JButton team = new JButton(new ImageIcon("kit\\button\\team.png"));
        team.addActionListener(teamEvent -> teamButtonAction(gameTypeMenuFrame));

        JLabel guide = new JLabel("Choose Match Type:");
        guide.setFont(new Font("TimesRoman", Font.BOLD, 17));
        guide.setForeground(new Color(60, 5, 3));
        //guide.setForeground(Color.WHITE);
        guide.setBorder(BorderFactory.createLineBorder(new Color(100, 10, 20), 5));

        JRadioButton radioButton1 = new JRadioButton(new ImageIcon("kit\\button\\deathMatch.png"));
        radioButton1.setSelected(true);
        radioButton1.addActionListener(actionEvent -> gameMode = "deathMatch");
        JRadioButton radioButton2 = new JRadioButton(new ImageIcon("kit\\button\\ligMatch.png"));
        radioButton2.addActionListener(actionEvent -> gameMode = "ligMatch");
        JRadioButton radioButton3 = new JRadioButton(new ImageIcon("kit\\button\\soloInMap.png"));
        radioButton3.addActionListener(actionEvent -> gameMode = "soloInMap");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);

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
    private void soloButtonAction(JFrame gameTypeMenuFrame) {
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

    private void runSoloGameButtonAction(JFrame soloFrame, @NotNull JPTextField soloPlayersTextField) {
        if (isInteger(soloPlayersTextField.getText())) {
            soloFrame.dispatchEvent(new WindowEvent(soloFrame, WindowEvent.WINDOW_CLOSING));
            int numberOfPlayers = Integer.parseInt(soloPlayersTextField.getText());

            if (isNetWork) {
                if (gameMode.equals("deathMatch")) {
                    String IP = Network.Constants.IP;
                    int port = Network.Constants.port;
                    RunGameHandler runGameHandler = new RunGameHandler(1, "deathMatch", 100);

                    MapFrame mapFrame = new MapFrame("Client", true, runGameHandler);
                    mapFrame.setLocationRelativeTo(null); // put frame at center of screen
                    mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mapFrame.setVisible(true);
                    mapFrame.initBufferStrategy();
                    // create and add user
                    UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), mapFrame.getTankTroubleMap(), user.getDataBaseFileName());
                    userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
                    userPlayer.setGroupNumber(1);
                    mapFrame.getTankTroubleMap().setController(userPlayer);
                    mapFrame.getTankTroubleMap().getUsers().add(mapFrame.getTankTroubleMap().getController());

                    RunGame runGame = new RunGame(mapFrame, runGameHandler);
                    runGameHandler.getRunGameArrayList().add(runGame);
                    runGame.run(IP, port);
                } else if (gameMode.equals("ligMatch")) {

                } else {

                }
            } else { // vs computer
                if (gameMode.equals("ligMatch")) { // lig
                    RunGameHandler runGameHandler = new RunGameHandler(numberOfPlayers, "lig", user.getUserTank().getHealth());
                    MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);
                    mapFrame.setLocationRelativeTo(null); // put frame at center of screen
                    mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mapFrame.setVisible(true);
                    mapFrame.initBufferStrategy();

                    for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
                        destructibleWall.setHealth(user.getWallHealth());
                    }

                    // create and add user
                    ArrayList<UserPlayer> userPlayers = new ArrayList<>();
                    UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), mapFrame.getTankTroubleMap(), user.getDataBaseFileName());
                    userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
                    userPlayer.setGroupNumber(1);
                    mapFrame.getTankTroubleMap().setController(userPlayer);
                    userPlayers.add(mapFrame.getTankTroubleMap().getController());
                    mapFrame.getTankTroubleMap().setUsers(userPlayers);

                    // create and add bots
                    ArrayList<BotPlayer> bots = new ArrayList<>();
                    File dir = new File("kit\\tanks");
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
                    //runGame.getGame().getCanvas().addKeyListener(mapFrame.getTankTroubleMap().getController().getKeyHandler());
                    runGame.run();
                } else { // death match
                    // create game map
                    System.out.println("in deathMatch.........................................");
                    RunGameHandler runGameHandler = new RunGameHandler(numberOfPlayers, "deathMatch", user.getUserTank().getHealth());
                    MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);
                    mapFrame.setLocationRelativeTo(null); // put frame at center of screen
                    mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mapFrame.setVisible(true);
                    mapFrame.initBufferStrategy();

                    for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
                        destructibleWall.setHealth(user.getWallHealth());
                    }

                    // create and add user
                    ArrayList<UserPlayer> userPlayers = new ArrayList<>();
                    UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), mapFrame.getTankTroubleMap(), user.getDataBaseFileName());
                    userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
                    userPlayer.setGroupNumber(1);
                    //userPlayers.add(userPlayer);
                    mapFrame.getTankTroubleMap().setController(userPlayer);
                    userPlayers.add(mapFrame.getTankTroubleMap().getController());
                    mapFrame.getTankTroubleMap().setUsers(userPlayers);

                    // create and add bots
                    ArrayList<BotPlayer> bots = new ArrayList<>();

                    File dir = new File("kit\\tanks");
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
                }


            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input for number of team players.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * this method run tank trouble game death mode
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

    private void runTeamGameButtonAction(JFrame teamFrame, @NotNull JPTextField teamPlayersTextField) {
        if (isInteger(teamPlayersTextField.getText())) {

            teamFrame.dispatchEvent(new WindowEvent(teamFrame, WindowEvent.WINDOW_CLOSING));
            int numberOfTeamPlayers = Integer.parseInt(teamPlayersTextField.getText());
            if (isNetWork) {
                if (gameMode.equals("deathMatch")) {

                } else if (gameMode.equals("ligMatch")) {

                } else {

                }
            } else {
                if (gameMode.equals("deathMatch")) {
                    RunGameHandler runGameHandler = new RunGameHandler(2, "deathMatch", user.getUserTank().getHealth());
                    MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);
                    mapFrame.setLocationRelativeTo(null); // put frame at center of screen
                    mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mapFrame.setVisible(true);
                    mapFrame.initBufferStrategy();
                    for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
                        destructibleWall.setHealth(user.getWallHealth());
                    }

                    // create and add user
                    ArrayList<UserPlayer> userPlayers = new ArrayList<>();
                    UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), mapFrame.getTankTroubleMap(), user.getDataBaseFileName());
                    userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
                    userPlayer.setGroupNumber(1);
                    mapFrame.getTankTroubleMap().setController(userPlayer);
                    userPlayers.add(mapFrame.getTankTroubleMap().getController());
                    mapFrame.getTankTroubleMap().setUsers(userPlayers);

                    // bots
                    ArrayList<BotPlayer> bots = new ArrayList<>();

                    // create user's team (friends bots)
                    File dir = new File("kit\\tanks");
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
                } else if (gameMode.equals("ligMatch")) {

                } else {
                    ArrayList<MapFrame> mapFrames = new ArrayList<>();

                    RunGameHandler runGameHandler = new RunGameHandler(2, "SoloInMap", user.getUserTank().getHealth());
                    MapFrame UserMapFrame = new MapFrame("walls!", false, runGameHandler);
                    UserMapFrame.setLocationRelativeTo(null); // put frame at center of screen
                    UserMapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    UserMapFrame.setVisible(true);
                    UserMapFrame.initBufferStrategy();
                    for (DestructibleWall destructibleWall : TankTroubleMap.getDestructibleWalls()) {
                        destructibleWall.setHealth(user.getWallHealth());
                    }
                    mapFrames.add(UserMapFrame);

                    // create and add user
                    ArrayList<UserPlayer> userPlayers = new ArrayList<>();
                    UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), UserMapFrame.getTankTroubleMap(), user.getDataBaseFileName());
                    userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
                    userPlayer.setGroupNumber(1);
                    UserMapFrame.getTankTroubleMap().setController(userPlayer);
                    userPlayers.add(UserMapFrame.getTankTroubleMap().getController());
                    UserMapFrame.getTankTroubleMap().setUsers(userPlayers);

                    // create and add bot
                    File dir = new File("kit\\tanks");
                    File[] allTanks = dir.listFiles();
                    Random rand = new Random();
                    File randomTank = allTanks[rand.nextInt(allTanks.length)];

                    ArrayList<BotPlayer> bots = new ArrayList<>();
                    BotPlayer bot = new BotPlayer("BOT", randomTank.getName(), UserMapFrame.getTankTroubleMap(), 2);
                    bot.getAiTank().setBulletDamage(user.getUserTank().getBulletDamage()); //bullet damage
                    bot.getAiTank().setHealth(user.getUserTank().getHealth()); //tank health
                    bots.add(bot);

                    UserMapFrame.getTankTroubleMap().setUsers(userPlayers);
                    UserMapFrame.getTankTroubleMap().setBots(bots);


                    RunGame runGame = new RunGame(UserMapFrame, runGameHandler);
                    runGameHandler.getRunGameArrayList().add(runGame);

                    for (int i = 0; i < numberOfTeamPlayers - 1; i++) {
                        MapFrame mapFrame = new MapFrame("walls!", false, runGameHandler);
                        mapFrame.setLocationRelativeTo(null); // put frame at center of screen
                        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        mapFrame.setVisible(true);
                        mapFrame.initBufferStrategy();
                        mapFrames.add(mapFrame);


                        ArrayList<BotPlayer> bots1 = new ArrayList<>();
                        // create team bot
                        randomTank = allTanks[rand.nextInt(allTanks.length)];
                        BotPlayer friendBot = new BotPlayer("Friend BOT", randomTank.getName(), UserMapFrame.getTankTroubleMap(), 1);
                        friendBot.getAiTank().setBulletDamage(user.getUserTank().getBulletDamage()); //bullet damage
                        friendBot.getAiTank().setHealth(user.getUserTank().getHealth()); //tank health
                        bots1.add(friendBot);

                        //create another bot
                        BotPlayer enemyBot = new BotPlayer("BOT", randomTank.getName(), UserMapFrame.getTankTroubleMap(), 2);
                        enemyBot.getAiTank().setBulletDamage(user.getUserTank().getBulletDamage()); //bullet damage
                        enemyBot.getAiTank().setHealth(user.getUserTank().getHealth()); //tank health
                        bots1.add(enemyBot);

                        mapFrame.getTankTroubleMap().setBots(bots1);
                    }
                    for (MapFrame frame : mapFrames) {
                        RunGame runGame1 = new RunGame(frame, runGameHandler);
                        runGameHandler.getRunGameArrayList().add(runGame1);
                        runGame1.run();
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input for number of team players.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void networkButtonAction(FrameWithBackGround mainGameFrame) {
        isNetWork = true;
        secondGameFrame(mainGameFrame);
    }

    /**
     * This method do action of setting button.
     * make new frame for show some detail and also this frame has 2 buttons 'change tank' and 'back'
     *
     * @param mainGameFrame is min frame of game
     */
    private void settingButtonAction(@NotNull FrameWithBackGround mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        level = new JLabel(" " + user.getLevel());
        timePlayLabel = new JLabel(" " + user.getTimePlay());
        winVSComputerLabel = new JLabel(" " + user.getWinInBotMatch());
        loseVSComputerLabel = new JLabel(" " + user.getLoseInBotMatch());
        winInOnlineModeLabel = new JLabel(" " + user.getWinInNetworkMatch());
        loseInOnlineModeLabel = new JLabel(" " + user.getLoseInNetworkMatch());
        shapeOfTankLabel = new JLabel(new ImageIcon("kit\\smallTanks\\" + user.getColor() + "\\normal.png"));
        JFrame settingFrame = new JFrame("Setting");
        settingFrame.setLocationRelativeTo(null);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        JButton backToMainMenuButton = new JButton("Back to main menu"); // find image for this one...
        backToMainMenuButton.addActionListener(backEvent -> backToMainMenuButtonAction(settingFrame, mainGameFrame));
        southPanel.add(changeTankButton);
        southPanel.add(backToMainMenuButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        settingFrame.setVisible(true);
    }

    private void changeActionTankHealthSlider(int tankHealth) {
        user.getUserTank().setHealth(tankHealth);
        saveAUser(user, "dataBase\\" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase\\rememberMe\\rememberMe.src");
        }
    }

    private void changeActionBulletDamageSlider(int bulletDamage) {
        user.getUserTank().setBulletDamage(bulletDamage);
        saveAUser(user, "dataBase\\" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase\\rememberMe\\rememberMe.src");
        }
    }

    private void changeActionWallHealthSlider(int wallHealth) {
        user.setWallHealth(wallHealth);
        saveAUser(user, "dataBase\\" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase\\rememberMe\\rememberMe.src");
        }
    }

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
    private void backToMainMenuButtonAction(@NotNull JFrame settingFrame, @NotNull FrameWithBackGround mainGameFrame) {
        settingFrame.dispatchEvent(new WindowEvent(settingFrame, WindowEvent.WINDOW_CLOSING));
        mainGameFrame.setVisible(true);
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
        JLabel showNormalTank = new JLabel(new ImageIcon("kit\\tanks\\" + user.getColor() + "\\normal.png"));
        JLabel showLaserTank = new JLabel(new ImageIcon("kit\\tanks\\" + user.getColor() + "\\laser.png"));
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
    private void selectActionForTanksList(@NotNull JLabel showNormalTank, @NotNull JLabel showLaserTank, String selectedValue) {
        showNormalTank.setIcon(new ImageIcon("kit\\tanks\\" + selectedValue + "\\normal.png"));
        showLaserTank.setIcon(new ImageIcon("kit\\tanks\\" + selectedValue + "\\laser.png"));
        shapeOfTankLabel.setIcon(new ImageIcon("kit\\smallTanks\\" + selectedValue + "\\normal.png"));
        user.setColor(selectedValue);
        saveAUser(user, "dataBase\\" + user.getDataBaseFileName());
        if (isRememberMeActive) {
            saveAUser(user, "dataBase\\rememberMe\\rememberMe.src");
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
     *
     */
    public void runAndShow() {
        graphicHandler();
    }

}
