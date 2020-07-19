package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;

public class Interface {
    JFrame welcomeFrame;
    JPTextField pTextField;
    JPPasswordField pPasswordField;
    JFrame mainFrame;
    JButton vsComputer, online, setting;

    public Interface(){

        //set look and fill[
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch (ClassNotFoundException e){
            System.out.println("Exception: "+e.getException());
        }catch (UnsupportedLookAndFeelException e){
            System.out.println("This system does not support this look and feel.");
        }catch (InstantiationException e){
            System.out.println("Instantiation exception.");
        }catch (IllegalAccessException e){
            System.out.println("IllegalAccess Exception");
        }
        //set look and fill]

        //initialization[
        welcomeFrame=new JFrame("login page"); //change name
        pTextField=new JPTextField("User name");
        pPasswordField=new JPPasswordField("Password");
        mainFrame=new JFrame("game name........");

        //vsComputer=new JButton(new ImageIcon("images\\vsComputer.png"));
        //online=new JButton(new ImageIcon("images\\web.png"));
        //setting=new JButton(new ImageIcon("images\\setting.png"));
        vsComputer=new JButton("vsComputer");
        online=new JButton("online");
        setting=new JButton("setting");
        //initialization]
    }

    private void graphicHandler(){
        loginPage();
        //gameFrame();

    }

    public void loginPage(){

        welcomeFrame.setLocationRelativeTo(null);
        welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel loginBackgroundLabel=new JLabel(new ImageIcon("images\\login.jpg"));
        welcomeFrame.setContentPane(loginBackgroundLabel);

        JButton login=new JButton("Login"); // find image for this one....
        login.addActionListener(loginEvent->{
            //welcomeFrame.dispatchEvent(new WindowEvent(welcomeFrame, WindowEvent.WINDOW_CLOSING)); //?????????? bug.....
            JFrame loginFrame=new JFrame("Login");
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            loginFrame.setContentPane(panel);

            JLabel unameLabel = new JLabel(" Username : ");
            JLabel psswdLabel = new JLabel(" Password : ");

            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            fieldsPanel.add(unameLabel);
            fieldsPanel.add(pTextField);
            fieldsPanel.add(psswdLabel);
            fieldsPanel.add(pPasswordField);

            JButton loginButton = new JButton("Login");
            int buttonWidth = loginButton.getPreferredSize().width;
            int buttonHeight = loginButton.getPreferredSize().height + 20;
            loginButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

            panel.add(fieldsPanel, BorderLayout.CENTER);
            panel.add(loginButton, BorderLayout.SOUTH);

            JButton check=new JButton("login check");
            loginFrame.setVisible(true);
            check.addActionListener(loginCheckEvent->{
                //TODO check with server
            }); //end check button listener

        }); //end login button listener
        JButton signUp=new JButton("SingUp"); // find image for this one....
        signUp.addActionListener(signUpEvent->{
            //welcomeFrame.dispatchEvent(new WindowEvent(welcomeFrame, WindowEvent.WINDOW_CLOSING)); //?????????? bug.....
            JFrame loginFrame=new JFrame("Login");
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            loginFrame.setContentPane(panel);

            JLabel unameLabel = new JLabel(" Username : ");
            JLabel psswdLabel = new JLabel(" Password : ");

            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            fieldsPanel.add(unameLabel);
            fieldsPanel.add(pTextField);
            fieldsPanel.add(psswdLabel);
            fieldsPanel.add(pPasswordField);

            JButton createAccount=new JButton("Create account"); // find image for this one....
            int buttonWidth = createAccount.getPreferredSize().width;
            int buttonHeight = createAccount.getPreferredSize().height + 20;
            createAccount.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

            panel.add(fieldsPanel, BorderLayout.CENTER);
            panel.add(createAccount, BorderLayout.SOUTH);

            JButton check=new JButton("login check");
            loginFrame.setVisible(true);
            check.addActionListener(loginCheckEvent->{
                //TODO check with server
            }); //end check button listener
        });

        welcomeFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        welcomeFrame.add(login,gbc);
        welcomeFrame.add(signUp,gbc);
        welcomeFrame.setVisible(true);
    }

    private void gameFrame(){
        // frame[
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600); // pack() used...
        //mainFrame.setLayout(new CardLayout());
        // frame]
        JLabel mainBackgroundLabel=new JLabel(new ImageIcon("images\\mainMenu.jpg"));
        mainFrame.setContentPane(mainBackgroundLabel);
        mainFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        mainFrame.add(vsComputer,gbc);
        mainFrame.add(online,gbc);
        mainFrame.add(setting,gbc);
    }

    private void vsComputer(){

    }

    private void online(){

    }

    private void setting(){

    }

    public void runAndShow(){
        //graphicHandler();
        //mainFrame.pack();
        //mainFrame.setVisible(true);
    }
}

/**
 * This class extend "JTextField" class.
 * Constructor of this class get a string as prompt text.
 * @author Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 6/10/2020
 */
class JPTextField extends JTextField {
    /**
     * This is constructor of this class and get prompt text and set a focusListener to it.
     * @param promptText is a string as prompt text
     */
    public JPTextField(final String promptText) {
        super(promptText);
        addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                if(getText().isEmpty()) {
                    setText(promptText);
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
                if(getText().equals(promptText)) {
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
     * @param promptText is a string as prompt text
     */
    public JPPasswordField(final String promptText) {
        super(promptText);
        addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                if(getText().isEmpty()) {
                    setText(promptText);
                }
            }
            @Override
            public void focusGained(FocusEvent e) {
                if(getText().equals(promptText)) {
                    setText("");
                }
            }
        });

    }
}

