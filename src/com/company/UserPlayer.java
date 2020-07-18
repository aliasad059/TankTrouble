package com.company;

/**
 *
 */
public class UserPlayer extends Player{
    private String password;
    private int level;
    private int loseInBotMatch;
    private int winInBotMatch;
    private int loseInServerMatch;
    private int winInServerMatch;
    private float gamePlay; //time

    /**
     *
     * @param name
     * @param color
     */
    public UserPlayer(String name, String color) {
        super(name, color);
        loseInBotMatch=0;
        winInBotMatch=0;
        loseInServerMatch=0;
        winInServerMatch=0;
    }

}
