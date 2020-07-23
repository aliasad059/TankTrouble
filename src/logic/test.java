package logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class test{
    public static void main(String[] args){
        Coordinate coordinate=new Coordinate(2,1);
        ArrayList<Coordinate> arrayList=findRectangleFromStartingPointAndAngle(1,3,coordinate,45);
    }
    private static ArrayList<Coordinate> findRectangleFromStartingPointAndAngle(int width, int high,Coordinate p, int angle){
        ArrayList<Coordinate> coordinates=new ArrayList<>();
        Coordinate p2=new Coordinate();
        Coordinate p3=new Coordinate();
        Coordinate p4=new Coordinate();
        // angle positive
        if(angle>=0) {
            while (angle>=360){
                angle-=360;
            }
            if (angle <= 90) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle <= 180) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle <= 270) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
        }

        // angle negative
        else{
            while (angle<=-360){
                angle+=360;
            }
            if (angle >= -90) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle>= -180) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle >= -270) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
        }

        //p4
        p4.setYCoordinate(p3.getYCoordinate()+p2.getYCoordinate()-p.getYCoordinate());
        p4.setXCoordinate(p3.getXCoordinate()+p2.getXCoordinate()-p.getXCoordinate());

        coordinates.add(p);
        coordinates.add(p2);
        coordinates.add(p4);
        coordinates.add(p3);

        return coordinates;
    }
}