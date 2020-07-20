import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Map {
    private int height;
    private int width;
    private int[][] map;

    /**
     *
     */
    public Map(String address){
        setHeightAndWidth(address);
        map=new int[height][width];
    }

    /**
     * This method set size of map (x axis and y axis size) based on it's text file.
     * @param address is address of the text file
     */
    private void setHeightAndWidth(String address){
        try(Scanner scanner=new Scanner(new File(address))){
            String s="";
            int column=0;
            int row=0;
            while (scanner.hasNext()){
                s=scanner.next();
                if(s.equals("\n")){
                    row++;
                }
                else {
                    column++;
                }
            }
            width=column;
            height=row;
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    /**
     * This method find text file in folder "notes".
     * @return these file as array
     */
    private File[] textFileFinder(String dir){
        File file = new File(dir);
        return file.listFiles((dir1, filename) -> filename.endsWith(".txt"));
    }

    /**
     * This method show list of map in file of game maps.
     */
    private void showListOfMaps(){ //this method need graphic graphic

    }

    /**
     * This method read a text file as map of game.
     * @param address is address of the text file
     */
    private void readMap(String address)  {
        try(Scanner scanner=new Scanner(new File(address))){
            String s="";
            int column=0;
            int row=0;
            while (scanner.hasNext()){
                s=scanner.next();
                if(s.equals("\n")){
                    row++;
                }
                else {
                    map[row][column]=Integer.parseInt(s);
                    column++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    /**
     * This method set a random prize in a random coordinate of map.
     */
    private void PrizeSetter(){
        Random random=new Random();
        int prize=random.nextInt(5)+4;
        do{
            Coordinate coordinate=new Coordinate();
            coordinate.setXCoordinate(random.nextInt(height));
            coordinate.setYCoordinate(random.nextInt(width));
        }while (map[height][width]!=0);
        map[height][width]=prize;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[][] getArrayMap() {
        return map;
    }
}
