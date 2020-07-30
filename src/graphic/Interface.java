package graphic;

import logic.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.*;
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
    private String userFileName;
    private boolean isNetWork, isSolo, isDeathMatch;

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
        level=new JLabel(" 0");
        timePlayLabel = new JLabel(" 0");
        winVSComputerLabel = new JLabel(" 0");
        loseVSComputerLabel = new JLabel(" 0");
        winInOnlineModeLabel = new JLabel(" 0");
        loseInOnlineModeLabel = new JLabel(" 0");
        shapeOfTankLabel = new JLabel(new ImageIcon("kit\\smallTanks\\Brown\\normal.png")); //32x32 or 16x16 p ?????
        userFileName="";
        isDeathMatch=true;
        //initialization]
    }

    private void graphicHandler() { //just for test
        logInAndSignUpPage();
        //gameFrame();
        //secondGameFrame();
    }

    /**
     * this method create login page of game that include sign in and sign up buttons.
     */
    private void logInAndSignUpPage() {
        logInAndSignUpPageFrame = new JFrame("login page");
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

        JPanel southPanel=new JPanel();
        southPanel.setLayout(new FlowLayout());

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(loginCheckEvent -> loginCheckButtonAction(loginFrame));

        JButton backToLogInAndSignUpPageButton= new JButton("Back");
        backToLogInAndSignUpPageButton.addActionListener(actionEvent ->backToLogInAndSignUpPageButtonAction(loginFrame));

        southPanel.add(loginButton);
        southPanel.add(backToLogInAndSignUpPageButton);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        loginFrame.setVisible(true);

    }

    private void backToLogInAndSignUpPageButtonAction(@NotNull JFrame loginFrame) {
        loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
        pTextField.setText("");
        pPasswordField=new JPPasswordField("password");
        confirmPPasswordField=new JPPasswordField("password");
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
                StringBuilder stringBuilder=new StringBuilder();
                for(int i=0; i<pPasswordField.getPassword().length; i++){
                    stringBuilder.append(pPasswordField.getPassword()[i]);
                }
                if (userPlayer.getName().equals(pTextField.getText()) && userPlayer.getPassword().equals(stringBuilder.toString())) {
                    falseInformation = false;
                    userFileName=file.getName();
                    loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
                    int dialogButton = JOptionPane.showConfirmDialog (null, "Do you want active remember me option?","Remember me?",JOptionPane.YES_NO_OPTION);
                    if(dialogButton==0){

                    }
                    //set config based on user
                    user=userPlayer;
                    level=new JLabel(" "+user.getLevel());
                    timePlayLabel = new JLabel(" "+user.getTimePlay());
                    winVSComputerLabel = new JLabel(" "+user.getWinInBotMatch());
                    loseVSComputerLabel = new JLabel(" "+user.getLoseInBotMatch());
                    winInOnlineModeLabel = new JLabel(" "+user.getWinInServerMatch());
                    loseInOnlineModeLabel = new JLabel(" "+user.getLoseInServerMatch());
                    shapeOfTankLabel = new JLabel(new ImageIcon("kit\\smallTanks\\"+user.getColor()+"\\normal.png"));

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

        JPanel southPanel=new JPanel();
        southPanel.setLayout(new FlowLayout());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(registerEvent -> registerButtonAction(signUpFrame));

        JButton backToLogInAndSignUpPageButton= new JButton("Back");
        backToLogInAndSignUpPageButton.addActionListener(actionEvent ->backToLogInAndSignUpPageButtonAction(signUpFrame));

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
        boolean isNewUserName=true;
        for (File file : files) {
            UserPlayer userPlayer;
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.getPath()))) {
                userPlayer = (UserPlayer) objectInputStream.readObject();
                if(userPlayer.getName().equals(pTextField.getText())) {
                    JOptionPane.showMessageDialog(loginFrame, "This user name already exist in the server please write another one.", "Alert", JOptionPane.INFORMATION_MESSAGE);
                    isNewUserName=false;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(isNewUserName) {
            if(Arrays.toString(pPasswordField.getPassword()).equals(Arrays.toString(confirmPPasswordField.getPassword()))) {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\" + (files.length + 1) + ".src"))) {
                    userFileName=""+(files.length+1)+".src";
                    StringBuilder stringBuilder=new StringBuilder();
                    for(int i=0; i<pPasswordField.getPassword().length; i++){
                        stringBuilder.append(pPasswordField.getPassword()[i]);
                    }
                    user = new UserPlayer(pTextField.getText(), stringBuilder.toString(), "Brown");
                    objectOutputStream.writeObject(user);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
                int dialogButton = JOptionPane.showConfirmDialog (null, "Do you want active remember me option?","Remember me?",JOptionPane.YES_NO_OPTION);
                if(dialogButton==0){

                }
                gameFrame();
            }
            else {
                JOptionPane.showMessageDialog(null, "Password and confirm password are not the same.", "Alert", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * this method create main frame of game.
     * this frame show 3 button for user "setting", "vsComputer" and "netWork"
     */
    private void gameFrame() {
        // frame[
        FrameWithBackGround mainGameFrame = new FrameWithBackGround("kit\\backGround\\3.jpg");
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

    private void logOutButtonAction(JFrame mainGameFrame){
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\" + userFileName))) {
            objectOutputStream.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        pTextField.setText("");
        pPasswordField=new JPPasswordField("password");
        confirmPPasswordField=new JPPasswordField("password");
        logInAndSignUpPageFrame.setVisible(true);
    }

    /**
     * this method run a game that user fight with bot
     *
     * @param mainGameFrame is main frame of game
     */
    private void vsComputerButtonAction(FrameWithBackGround mainGameFrame) {
        isNetWork=false;
        secondGameFrame(mainGameFrame);
    }

    /**
     * this method show second frame, frame that user choose lig or death
     *
//     * @param mainGameFrame is main frame of game
     */
    private void secondGameFrame(@NotNull FrameWithBackGround mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        FrameWithBackGround gameTypeMenuFrame = new FrameWithBackGround("kit\\backGround\\2.jpg");
        gameTypeMenuFrame.setLocationRelativeTo(null);
        gameTypeMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameTypeMenuFrame.setSize(1000, 600);

        gameTypeMenuFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton solo = new JButton(new ImageIcon("kit\\button\\solo.png"));
        solo.addActionListener(soloEvent->soloButtonAction(gameTypeMenuFrame));

        JButton team = new JButton(new ImageIcon("kit\\button\\team.png"));
        team.addActionListener(teamEvent -> teamButtonAction(gameTypeMenuFrame));

        JLabel guide=new JLabel("Choose Match Type:");
        guide.setFont(new Font("TimesRoman", Font.BOLD, 17));
        guide.setForeground(new Color(60,5,3));
        //guide.setForeground(Color.WHITE);
        guide.setBorder(BorderFactory.createLineBorder(new Color(100,10,20), 5));

        JRadioButton radioButton1=new JRadioButton(new ImageIcon("kit\\button\\deathMatch.png"));
        radioButton1.setSelected(true);
        radioButton1.addActionListener(actionEvent -> isDeathMatch=true);
        JRadioButton radioButton2=new JRadioButton(new ImageIcon("kit\\button\\ligMatch.png"));
        radioButton2.addActionListener(actionEvent -> isDeathMatch= false);
        ButtonGroup buttonGroup=new ButtonGroup();
        buttonGroup.add(radioButton1); buttonGroup.add(radioButton2);

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
        gameTypeMenuFrame.dispatchEvent(new WindowEvent(gameTypeMenuFrame, WindowEvent.WINDOW_CLOSING));
        if(isNetWork){

        }
        else {
            TankTroubleMap.getUserTanks().add(new UserTank(user.getTankHealth(),user.getBulletDamage(),"kit\\tanks\\"+user.getColor()+"\\normal.png",0)); // Note................
            File dir = new File("kit\\tanks");
            File[] allTanks = dir.listFiles();
            Random rand = new Random();
            File randomTank = allTanks[rand.nextInt(allTanks.length)];
            TankTroubleMap.getAITanks().add(new AITank(user.getTankHealth(),user.getBulletDamage(),"kit\\tanks\\"+randomTank.getName()+"\\normal.png",1)); // Note................
            RunGame runGame=new RunGame();
            runGame.run();
        }
    }

    /**
     * this method run tank trouble game death mode
     */
    private void teamButtonAction(@NotNull JFrame gameTypeMenuFrame) {
        gameTypeMenuFrame.dispatchEvent(new WindowEvent(gameTypeMenuFrame, WindowEvent.WINDOW_CLOSING));
        if(isNetWork){

        }
        else {
            JFrame teamFrame = new JFrame("Team Players");
            teamFrame.setSize(250, 150);
            teamFrame.setResizable(false);
            teamFrame.setLocationRelativeTo(null);
            teamFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            teamFrame.setContentPane(panel);

            JPanel centerPanel = new JPanel(new GridLayout(1, 2, 5, 5));

            JLabel teamPlayersLabel = new JLabel(" Number of team players : ");
            JPTextField teamPlayersTextField=new JPTextField("Enter an integer...");

            centerPanel.add(teamPlayersLabel);
            centerPanel.add(teamPlayersTextField);


            JButton runTeamGameButton = new JButton("Run game");
            runTeamGameButton.addActionListener(runTeamGameEvent -> runTeamGameButtonAction(teamFrame, teamPlayersTextField));


            panel.add(centerPanel, BorderLayout.CENTER);
            panel.add(runTeamGameButton, BorderLayout.SOUTH);

            teamFrame.setVisible(true);


        }
    }

    private void runTeamGameButtonAction(JFrame teamFrame, @NotNull JPTextField teamPlayersTextField){
        if(isInteger(teamPlayersTextField.getText())){
            teamFrame.dispatchEvent(new WindowEvent(teamFrame, WindowEvent.WINDOW_CLOSING));
            int numberOfTeamPlayers=Integer.parseInt(teamPlayersTextField.getText());
            TankTroubleMap.getUserTanks().add(new UserTank(user.getTankHealth(),user.getBulletDamage(),"kit\\tanks\\"+user.getColor()+"\\normal.png",0)); // Note................
            //user Team
            for(int i=0; i<numberOfTeamPlayers-1; i++){ // -1 in "numberOfTeamPlayers-1" is 'cause of user player
                File dir = new File("kit\\tanks");
                File[] allTanks = dir.listFiles();
                Random rand = new Random();
                File randomTank = allTanks[rand.nextInt(allTanks.length)];
                TankTroubleMap.getAITanks().add(new AITank(user.getTankHealth(),user.getBulletDamage(),"kit\\tanks\\"+randomTank.getName()+"\\normal.png",0)); // Note................
            }
            // another team
            for(int i=0; i<numberOfTeamPlayers; i++){
                File dir = new File("kit\\tanks");
                File[] allTanks = dir.listFiles();
                Random rand = new Random();
                File randomTank = allTanks[rand.nextInt(allTanks.length)];
                TankTroubleMap.getAITanks().add(new AITank(user.getTankHealth(),user.getBulletDamage(),"kit\\tanks\\"+randomTank.getName()+"\\normal.png",0)); // Note................
            }
            RunGame runGame=new RunGame();
            runGame.run();
        }
        else {
            JOptionPane.showMessageDialog(null, "Invalid input for number of team players.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    private void networkButtonAction(FrameWithBackGround mainGameFrame) {
        //showSecondMenu(mainGameFrame);
        // logic part
    }

    /**
     * This method do action of setting button.
     * make new frame for show some detail and also this frame has 2 buttons 'change tank' and 'back'
     *
     * @param mainGameFrame is min frame of game
     */
    private void settingButtonAction(@NotNull FrameWithBackGround mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        JFrame settingFrame = new JFrame("Setting");
        settingFrame.setLocationRelativeTo(null);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.setSize(600, 680);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 2, 0, 10));
        settingFrame.setContentPane(panel);

        JPanel centerPanel = new JPanel(new GridLayout(10, 2));

        JLabel levelTextLabel=new JLabel(" Level:");
        levelTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(levelTextLabel);
        centerPanel.add(level);

        JLabel TimePlayTextLabel=new JLabel(" Time play :");
        TimePlayTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(TimePlayTextLabel);
        centerPanel.add(timePlayLabel);

        JLabel winVSComputerTextLabel=new JLabel(" Win vs computer :");
        winVSComputerTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        winVSComputerTextLabel.setForeground(new Color(10,100,50));
        centerPanel.add(winVSComputerTextLabel);
        centerPanel.add(winVSComputerLabel);

        JLabel loseVSComputerTextLabel=new JLabel(" Lose vs computer :");
        loseVSComputerTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        loseVSComputerTextLabel.setForeground(new Color(100,40,40));
        centerPanel.add(loseVSComputerTextLabel);
        centerPanel.add(loseVSComputerLabel);

        JLabel winInOnlineModeTextLabel=new JLabel(" Win in online mode :");
        winInOnlineModeTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        winInOnlineModeTextLabel.setForeground(new Color(10,100,50));
        centerPanel.add(winInOnlineModeTextLabel);
        centerPanel.add(winInOnlineModeLabel);

        JLabel loseInOnlineModeTextLabel=new JLabel(" Lose in online mode :");
        loseInOnlineModeTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        loseInOnlineModeTextLabel.setForeground(new Color(100,40,40));
        centerPanel.add(loseInOnlineModeTextLabel);
        centerPanel.add(loseInOnlineModeLabel);

        JLabel shapeOfTankTextLabel=new JLabel(" Shape of tank :");
        shapeOfTankTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(shapeOfTankTextLabel);
        centerPanel.add(shapeOfTankLabel);

        JLabel tankHealthTextLabel=new JLabel(" Health of tank : ");
        tankHealthTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(tankHealthTextLabel);

        JSlider tankHealthSlider=new JSlider(50,500,user.getTankHealth());
        tankHealthSlider.setMajorTickSpacing(50);
        tankHealthSlider.setMinorTickSpacing(10);
        tankHealthSlider.setPaintTrack(true);
        tankHealthSlider.setPaintTicks(true);
        tankHealthSlider.setPaintLabels(true);
        tankHealthSlider.addChangeListener(changeEvent -> changeActionTankHealthSlider(tankHealthSlider.getValue()));
        centerPanel.add(tankHealthSlider);

        JLabel bulletDamageTextLabel=new JLabel(" Damage of bullet : ");
        bulletDamageTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(bulletDamageTextLabel);

        JSlider bulletDamageSlider=new JSlider(5,55,user.getBulletDamage());
        bulletDamageSlider.setMajorTickSpacing(10);
        bulletDamageSlider.setMinorTickSpacing(2);
        bulletDamageSlider.setPaintTrack(true);
        bulletDamageSlider.setPaintTicks(true);
        bulletDamageSlider.setPaintLabels(true);
        bulletDamageSlider.addChangeListener(changeEvent -> changeActionBulletDamageSlider(bulletDamageSlider.getValue()));
        centerPanel.add(bulletDamageSlider);

        JLabel wallHealthTextLabel=new JLabel(" health of destructible wall : ");
        wallHealthTextLabel.setFont(new Font("TimesRoman", Font.BOLD, 17));
        centerPanel.add(wallHealthTextLabel);

        JSlider wallHealthSlider=new JSlider(30,100,user.getWallHealth());
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
        backToMainMenuButton.addActionListener(backEvent -> backToMainMenuButtonAction(settingFrame,mainGameFrame));
        southPanel.add(changeTankButton);
        southPanel.add(backToMainMenuButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        settingFrame.setVisible(true);
    }

    private void changeActionTankHealthSlider(int tankHealth){
        user.setTankHealth(tankHealth);
    }

    private void changeActionBulletDamageSlider(int bulletDamage){
        user.setBulletDamage(bulletDamage);
    }

    private void changeActionWallHealthSlider(int wallHealth){
        user.setWallHealth(wallHealth);
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
        tanksList.setSelectedValue(user.getColor(),false);
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
        centerPanel.add(centerOfCenterPanel,BorderLayout.CENTER);

        JPanel southOfCenterPanel = new JPanel();
        southOfCenterPanel.setLayout(new GridLayout(1, 2));
        JLabel showNormalTank = new JLabel(new ImageIcon("kit\\tanks\\"+user.getColor()+"\\normal.png"));
        JLabel showLaserTank = new JLabel(new ImageIcon("kit\\tanks\\"+user.getColor()+"\\laser.png"));
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

/**
 * This class extend "JTextField" class.
 * Constructor of this class get a string as prompt text.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
class JPTextField extends JTextField {
    /**
     * This is constructor of this class and get prompt text and set a focusListener to it.
     *
     * @param promptText is a string as prompt text
     */
    public JPTextField(final String promptText) {
        super(promptText);
        addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(promptText);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(promptText)) {
                    setText("");
                }
            }
        });

    }
}


/**
 * This class extend "JPasswordField" class.
 * Constructor of this class get a string as prompt text.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
class JPPasswordField extends JPasswordField {
    /**
     * This is constructor of this class and get prompt text and set a focusListener to it.
     *
     * @param promptText is a string as prompt text
     */
    public JPPasswordField(final String promptText) {
        super(promptText);
        addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(promptText);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(promptText)) {
                    setText("");
                }
            }
        });

    }
}

/**
 * This class extend "JFrame" class.
 * Constructor of this class get a string as path of backGround.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
class FrameWithBackGround extends JFrame {
    Image backGround;

    public FrameWithBackGround(String backGroundPath) {
        backGround = Toolkit.getDefaultToolkit().getImage(backGroundPath);
        this.setContentPane(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backGround, 0, 0, null);
            }
        });
    }
}
