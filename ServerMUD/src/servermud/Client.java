
package servermud;

/**
 *
 * @author Dean
 */
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client extends Applet implements Runnable, KeyListener{
    
    static Socket socket;
    static DataInputStream in;
    static DataOutputStream out;
    
    int playerid;
    int[] x = new int[10];
    int[] y = new int[10];
    
    boolean left,down,right,up;     //variables for player key movement
    
    int playerx;
    int playery; //local coord
    
    public void init(){
        setSize(400,400);                                   //set seize of client window
        addKeyListener(this);                               //adds the key listener for user to input
        try{
        System.out.println("Connecting..");
        socket = new Socket("localhost",7777);              //to go over internet give other person your ip address to replace localhost
        System.out.println("Connection succesful..");
        in = new DataInputStream(socket.getInputStream());
        playerid = in.readInt();
        out = new DataOutputStream(socket.getOutputStream());
        //getting the player to a thread and giving them an id
        Input input = new Input(in,this);
        Thread thread = new Thread(input);
        thread.start();    
        Thread thread2 = new Thread(this);
        thread2.start();
        }catch(Exception e){
            System.out.println("unable to start client");
        }
    }
    //update users coordinates with there id and X and Y coords
    public void updateCoordinates(int pid, int x2, int y2){
        this.x[pid] = x2;
        this.y[pid] = y2;
    }
    //draw ovals on for each player
    public void paint(Graphics g){
        for (int i=0; i<10;i++){
        g.drawOval(x[i],y[i],5,5);
        }
    }

   
    
    public void run() {
       while (true){
           //player controll changes coord
           if(right==true){
           playerx+=10;
           }
           if(left==true){
           playerx-=10;
           }
           if(up==true){
           playery-=10;
           }
           if(down==true){
           playery+=10;
           }
           
           if(right || left || up || down){
               try{
           out.writeInt(playerid);
           out.writeInt(playerx);
           out.writeInt(playery);
           }catch(Exception e){System.out.println("Errors in coordinates");}
           }
           repaint();
        try{
           Thread.sleep(400);//400ms           
       }catch (InterruptedException e){
       e.printStackTrace();
        }
       }    
    }
    
    public void keyPressed(KeyEvent ke) {
      if (ke.getKeyCode()== 37){
         left = true;
     }
    if (ke.getKeyCode()== 38){
        up = true;
     }
      
    if (ke.getKeyCode()== 39){
        right = true;
     }
      
    if (ke.getKeyCode()== 40){
        down = true;
     }
    }


    public void keyReleased(KeyEvent ke) {
      if (ke.getKeyCode()== 37){
         left = false;
     }
    if (ke.getKeyCode()== 38){
        up = false;
     }
      
    if (ke.getKeyCode()== 39){
        right = false;
     }
      
    if (ke.getKeyCode()== 40){
        down = false;
     }
      
    }

    public void keyTyped(KeyEvent ke) {
    
    }

class Input implements Runnable{
    
    DataInputStream in;
    Client client;
    public Input(DataInputStream in,Client c){
        this.in =in;
        this.client = c;
    
    }  
    //allows messages to be sent.
    public void run(){
        while(true){
            
          try{
           int playerid = in.readInt();
           int x = in.readInt();
           int y = in.readInt();
           client.updateCoordinates(playerid,x,y);
          }catch (IOException e) {
                e.printStackTrace();
            }
        }    
    }
}}