import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;

public class Interface {
    private JFrame welcomeFrame, mainGameFrame;
    private JPTextField pTextField;
    private JPPasswordField pPasswordField, confirmPPasswordField;

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
        welcomeFrame = new JFrame("login page"); //change name
        pTextField = new JPTextField("User name");
        pPasswordField = new JPPasswordField("Password");
        confirmPPasswordField = new JPPasswordField("Confirm");
        mainGameFrame = new JFrame("game name........");

        //vsComputer=new JButton(new ImageIcon("images\\vsComputer.png"));
        //online=new JButton(new ImageIcon("images\\web.png"));
        //setting=new JButton(new ImageIcon("images\\setting.png"));

        //initialization]
    }

    private void graphicHandler() {
        //loginPage();
        gameFrame();

    }

    private void loginPage() {
        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel loginBackgroundLabel = new JLabel(new ImageIcon(".\\images\\Fish.gif"));
        welcomeFrame.setContentPane(loginBackgroundLabel);

        JButton login = new JButton("Login"); // find image for this one....
        login.addActionListener(loginEvent -> loginButtonAction());

        JButton signUp = new JButton("SingUp"); // find image for this one....
        signUp.addActionListener(signUpEvent -> signUpButtonAction());

        welcomeFrame.setLayout(new BorderLayout());
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(login);
        southPanel.add(signUp);
        welcomeFrame.add(southPanel, BorderLayout.SOUTH);

        JLabel northLabel = new JLabel("Welcome to TANK TROUBLE");
        northLabel.setForeground(new Color(60, 40, 20));
        northLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeFrame.add(northLabel, BorderLayout.NORTH);

        welcomeFrame.pack();
        welcomeFrame.setVisible(true);

    }

    /**
     * This method is action of login button in login page.
     */
    private void loginButtonAction() {
        welcomeFrame.dispatchEvent(new WindowEvent(welcomeFrame, WindowEvent.WINDOW_CLOSING));
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

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(loginButton, BorderLayout.SOUTH);

        JButton check = new JButton("login check");

        loginFrame.setVisible(true);
        check.addActionListener(loginCheckEvent -> {
            //TODO check with server
        }); //end check button listener
    }

    /**
     * This method is action of sign up button in login page.
     */
    private void signUpButtonAction() {
        welcomeFrame.dispatchEvent(new WindowEvent(welcomeFrame, WindowEvent.WINDOW_CLOSING));
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

        JButton loginButton = new JButton("Register");

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(loginButton, BorderLayout.SOUTH);

        JButton check = new JButton("login check");

        loginFrame.setVisible(true);
        check.addActionListener(loginCheckEvent -> {
            //TODO check with server
        }); //end check button listener
    }

    private void gameFrame() {
        // frame[
        mainGameFrame.setLocationRelativeTo(null);
        mainGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainGameFrame.setSize(1000, 600);
        //mainFrame.setLayout(new CardLayout());
        // frame]

        JLabel mainBackgroundLabel = new JLabel(new ImageIcon("images\\mainMenu.jpg"));
        mainGameFrame.setContentPane(mainBackgroundLabel);
        mainGameFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton vsComputer = new JButton(new ImageIcon("images\\vsComputer.jpg"));
        vsComputer.addActionListener(vsComputerEvent -> vsComputerButtonAction());

        JButton online = new JButton(new ImageIcon("images\\online.jpg"));
        online.addActionListener(onlineEvent -> onlineButtonAction());

        JButton setting = new JButton(new ImageIcon("images\\setting.png"));
        setting.addActionListener(settingEvent -> settingButtonAction());

        mainGameFrame.add(vsComputer, gbc);
        mainGameFrame.add(online, gbc);
        mainGameFrame.add(setting, gbc);
        mainGameFrame.pack();
        mainGameFrame.setVisible(true);
    }

    private void vsComputerButtonAction() {

    }

    private void onlineButtonAction() {

    }

    private void settingButtonAction() {
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
        backToMainMenuButton.addActionListener(backEvent -> backToMainMenuButtonAction(settingFrame));
        southPanel.add(changeTankButton);
        southPanel.add(backToMainMenuButton);
        panel.add(southPanel, BorderLayout.SOUTH);

        settingFrame.setVisible(true);
    }

    private void backToMainMenuButtonAction(@NotNull JFrame settingFrame) {
        settingFrame.dispatchEvent(new WindowEvent(settingFrame, WindowEvent.WINDOW_CLOSING));
        mainGameFrame.setVisible(true);
    }

    private void changeTankButtonAction(@NotNull JFrame settingFrame) {
        settingFrame.dispatchEvent(new WindowEvent(settingFrame, WindowEvent.WINDOW_CLOSING));
        JFrame chooseTankFrame = new JFrame("Choose Tank");
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
        JList<String> tanksList = new JList<>(listModel);
        tanksList.setSelectedIndex(0);
        centerPanel.add(tanksList, BorderLayout.NORTH);
        chooseTankFrame.add(centerPanel, BorderLayout.CENTER);

        JPanel innerCenterPanel = new JPanel();
        innerCenterPanel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel normalLabel = new JLabel("                           Normal Form");
        JLabel laserLabel = new JLabel("                           Laser Form");
        JLabel showNormalTank = new JLabel(new ImageIcon("kit\\tanks\\Brown\\normal.png")); //set in player's tank....
        JLabel showLaserTank = new JLabel(new ImageIcon("kit\\tanks\\Brown\\normal.png")); //set in player's tank.....

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

    private void selectActionForTanksList(@NotNull JLabel showNormalTank, @NotNull JLabel showLaserTank, String selectedValue) {
        showNormalTank.setIcon(new ImageIcon("kit\\tanks\\" + selectedValue + "\\normal.png"));
        showLaserTank.setIcon(new ImageIcon("kit\\tanks\\" + selectedValue + "\\laser.png"));
    }

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

    private void saveAUserPlayer() {

    }

    private void loadAUserPlayer() {

    }
}

/**
 * This class extend "JTextField" class.
 * Constructor of this class get a string as prompt text.
 *
 * @author Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 6/10/2020
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
 * ?????????
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

