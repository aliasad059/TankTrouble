package graphic;

import logic.UserPlayer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * This class represent interface of "Tank Trouble" game.
 * Ont the other word this class is main part of graphic.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class Interface {

    private JPTextField pTextField;
    private JPPasswordField pPasswordField, confirmPPasswordField;

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

        //vsComputer=new JButton(new ImageIcon("images\\vsComputer.png"));
        //online=new JButton(new ImageIcon("images\\web.png"));
        //setting=new JButton(new ImageIcon("images\\setting.png"));

        //initialization]
    }

    private void graphicHandler() { //just for test
        //loginPage();
        gameFrame();

    }

    /**
     * this method create login page of game that include sign in and sign up buttons.
     */
    private void loginPage() {
        JFrame loginPage = new JFrame("login page");
        loginPage.setLocationRelativeTo(null);
        loginPage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel loginBackgroundLabel = new JLabel(new ImageIcon("kit\\backGround\\Fish.gif"));
        loginPage.setContentPane(loginBackgroundLabel);

        JButton login = new JButton("Login"); // find image for this one....
        login.addActionListener(loginEvent -> loginButtonAction(loginPage));

        JButton signUp = new JButton("SingUp"); // find image for this one....
        signUp.addActionListener(signUpEvent -> signUpButtonAction(loginPage));

        loginPage.setLayout(new BorderLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(login);
        southPanel.add(signUp);
        loginPage.add(southPanel, BorderLayout.SOUTH);

        JLabel northLabel = new JLabel("Welcome to TANK TROUBLE");
        northLabel.setForeground(new Color(60, 40, 20));
        northLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginPage.add(northLabel, BorderLayout.NORTH);

        loginPage.pack();
        loginPage.setVisible(true);

    }

    /**
     * This method is action of login button in login page.
     */
    private void loginButtonAction(JFrame loginPage) {
        loginPage.dispatchEvent(new WindowEvent(loginPage, WindowEvent.WINDOW_CLOSING));
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

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(loginCheckEvent -> loginCheckButtonAction(loginFrame));

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(loginButton, BorderLayout.SOUTH);

        loginFrame.setVisible(true);

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
        if (files != null) {
            boolean falseInformation = true;
            for (File file : files) {
                UserPlayer userPlayer = new UserPlayer("", "");
                try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file.getPath()))) {
                    userPlayer = (UserPlayer) objectInputStream.readObject();
                    if (userPlayer.getName().equals(pTextField.getText()) && userPlayer.getPassword().equals(
                            pPasswordField.getText())) {//bag...........??????
                        falseInformation = false;
                        loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
                        gameFrame();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (falseInformation) {
                JOptionPane.showMessageDialog(loginFrame, "unmatched information.", "Alert", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(loginFrame, "unmatched information.", "Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is action of sign up button in login page.
     */
    private void signUpButtonAction(JFrame loginPage) {
        loginPage.dispatchEvent(new WindowEvent(loginPage, WindowEvent.WINDOW_CLOSING));
        JFrame loginFrame = new JFrame("Sign Up");
        loginFrame.setSize(250, 180);
        loginFrame.setResizable(false);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        loginFrame.setContentPane(panel);

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

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(registerEvent -> registerButtonAction(loginFrame));

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(registerButton, BorderLayout.SOUTH);

        loginFrame.setVisible(true);
    }

    /**
     * This method do action of register button and make a new file in data base based on input information
     *
     * @param loginFrame
     */
    private void registerButtonAction(JFrame loginFrame) {
        File[] files = srcFileFinder("dataBase\\");
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("dataBase\\" + (files.length + 1) + ".src"))) {
            UserPlayer userPlayer = new UserPlayer(pTextField.getText(), pPasswordField.getText());
            objectOutputStream.writeObject(userPlayer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginFrame.dispatchEvent(new WindowEvent(loginFrame, WindowEvent.WINDOW_CLOSING));
        gameFrame();
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
        mainGameFrame.setSize(1050, 620);
        //mainFrame.setLayout(new CardLayout());
        // frame]

        //JLabel mainBackgroundLabel=new JLabel(new ImageIcon(".\\kit++\\images\\mainMenu.jpg"));
        //mainGameFrame.setContentPane(mainBackgroundLabel);
        mainGameFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton vsComputerButton = new JButton(new ImageIcon("kit\\button\\vsComputer.png"));
        vsComputerButton.addActionListener(vsComputerEvent -> vsComputerButtonAction(mainGameFrame));

        JButton network = new JButton(new ImageIcon("kit\\button\\netWork.png"));
        network.addActionListener(onlineEvent -> onlineButtonAction(mainGameFrame));

        JButton settingButton = new JButton(new ImageIcon("kit\\button\\setting.png"));
        settingButton.addActionListener(settingEvent -> settingButtonAction(mainGameFrame));

        mainGameFrame.add(vsComputerButton, gbc);
        mainGameFrame.add(network, gbc);
        mainGameFrame.add(settingButton, gbc);
        mainGameFrame.setVisible(true);
    }

    /**
     * this method run a game that user fight with bot
     *
     * @param mainGameFrame is main frame of game
     */
    private void vsComputerButtonAction(FrameWithBackGround mainGameFrame) {
        showSecondMenu(mainGameFrame);
        // logic part
    }

    /**
     * this method show second frame, frame that user choose lig or death
     *
     * @param mainGameFrame is main frame of game
     */
    private void showSecondMenu(FrameWithBackGround mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        FrameWithBackGround gameTypeMenuFrame = new FrameWithBackGround("kit\\backGround\\2.jpg");
        gameTypeMenuFrame.setLocationRelativeTo(null);
        gameTypeMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameTypeMenuFrame.setSize(1000, 600);

        //JLabel mainBackgroundLabel=new JLabel(new ImageIcon(".\\kit++\\images\\secondMenu.jpg"));
        //gameTypeMenuFrame.setContentPane(mainBackgroundLabel);
        gameTypeMenuFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton ligButton = new JButton(new ImageIcon("kit\\button\\lig.png"));
        ligButton.addActionListener(ligEvent -> ligButtonAction());

        JButton deathButton = new JButton(new ImageIcon("kit\\button\\death.png"));
        deathButton.addActionListener(deathEvent -> deathButtonAction());

        gameTypeMenuFrame.add(ligButton, gbc);
        gameTypeMenuFrame.add(deathButton, gbc);
        gameTypeMenuFrame.setVisible(true);
    }

    /**
     * this method run tank trouble game lig mode
     */
    private void ligButtonAction() {
        // logic part
    }

    /**
     * this method run tank trouble game death mode
     */
    private void deathButtonAction() {
        //logic part
    }

    private void onlineButtonAction(FrameWithBackGround mainGameFrame) {
        showSecondMenu(mainGameFrame);
        // logic part
    }

    /**
     * This method do action of setting button.
     * make new frame for show some detail and also this frame has 2 buttons 'change tank' and 'back'
     *
     * @param mainGameFrame is min frame of game
     */
    private void settingButtonAction(FrameWithBackGround mainGameFrame) {
        mainGameFrame.dispatchEvent(new WindowEvent(mainGameFrame, WindowEvent.WINDOW_CLOSING));
        JFrame settingFrame = new JFrame("Setting");
        settingFrame.setLocationRelativeTo(null);
        settingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingFrame.setSize(400, 500);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        settingFrame.setContentPane(panel);

        JLabel timePlayLabel = new JLabel(" Time play : ");
        JLabel AtimePlayLabel = new JLabel(" ???????? "); // Answer --> A
        JLabel winVSComputerLabel = new JLabel(" Win vs computer : ");
        JLabel AWinVSComputerLabel = new JLabel(" ??????????? ");
        JLabel loseVSComputerLabel = new JLabel(" Lose vs computer : ");
        JLabel ALoseVSComputerLabel = new JLabel(" ?????????? ");
        JLabel winInOnlineModeLabel = new JLabel(" Win in online mode : ");
        JLabel AWinInOnlineModeLabel = new JLabel(" ???????????? ");
        JLabel loseInOnlineModeLabel = new JLabel(" lose in online mode : ");
        JLabel ALoseInOnlineModeLabel = new JLabel(" ?????????????? ");
        JLabel shapeOfTankLabel = new JLabel(" shape of tank :");
        JLabel AShapeOfTankLabel = new JLabel(new ImageIcon("??????")); //32x32 or 16x16 p

        JPanel centerPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        centerPanel.add(timePlayLabel);
        centerPanel.add(AtimePlayLabel);
        centerPanel.add(winVSComputerLabel);
        centerPanel.add(AWinVSComputerLabel);
        centerPanel.add(loseVSComputerLabel);
        centerPanel.add(ALoseVSComputerLabel);
        centerPanel.add(winInOnlineModeLabel);
        centerPanel.add(AWinInOnlineModeLabel);
        centerPanel.add(loseInOnlineModeLabel);
        centerPanel.add(ALoseInOnlineModeLabel);
        centerPanel.add(shapeOfTankLabel);
        centerPanel.add(AShapeOfTankLabel);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        JButton changeTankButton = new JButton("Change tank");
        changeTankButton.addActionListener(changeTankEvent -> changeTankButtonAction(settingFrame));
        JButton backToMainMenuButton = new JButton("Back to main menu"); // find image for this one...
//        backToMainMenuButton.addActionListener(backEvent -> backToMainMenuButtonAction(settingFrame));
        southPanel.add(changeTankButton);
        southPanel.add(backToMainMenuButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        settingFrame.setVisible(true);
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
        chooseTankFrame.setSize(520, 700);

        JLabel guide = new JLabel("Choose your tank");
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
        tanksList.setSelectedIndex(0);
        centerPanel.add(tanksList, BorderLayout.NORTH);
        chooseTankFrame.add(centerPanel, BorderLayout.CENTER);

        JPanel innerCenterPanel = new JPanel();
        innerCenterPanel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel normalLabel = new JLabel("                           Normal Form");
        JLabel laserLabel = new JLabel("                             Laser Form");
        JLabel showNormalTank = new JLabel(new ImageIcon("kit\\tanks\\Brown\\normal.png")); //set in player's tank....
        JLabel showLaserTank = new JLabel(new ImageIcon("kit\\tanks\\Brown\\laser.png")); //set in player's tank.....

        innerCenterPanel.add(normalLabel);
        innerCenterPanel.add(laserLabel);
        innerCenterPanel.add(showNormalTank);
        innerCenterPanel.add(showLaserTank);

        centerPanel.add(innerCenterPanel, BorderLayout.CENTER);

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
