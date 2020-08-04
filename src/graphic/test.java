package graphic;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;

public class test {
    public static void main(String[] args) {
//        FrameWithBackGround frameWithBackGround=new FrameWithBackGround("1.jpg");
//        frameWithBackGround.setLayout(new BorderLayout());
//        JButton button=new JButton("newwwww");
//        frameWithBackGround.add(button,BorderLayout.SOUTH);
        //frameWithBackGround.setVisible(true);

        Interface in = new Interface();
        in.runAndShow();


    }
    private static void changeListener(JSlider jSlider){
        System.out.println(jSlider.getValue());
    }

}
