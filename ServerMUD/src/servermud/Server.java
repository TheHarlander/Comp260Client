//Followed turoial on https://www.youtube.com/watch?v=1a3TtPr_yvI 
//Aim to have some sort of who ever finishes first gets to answer a question.
//Author Dean Harland
package servermud;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
 
    //DEFINITIONS
    static ServerSocket serverSocket;
    static Socket socket; 
    static DataOutputStream out;
    static Users[] user = new Users[10];
    static DataInputStream in;
    
    //Main server function
    public static void main(String[]args)throws Exception{
        System.out.println("Starting up server...");
        serverSocket = new ServerSocket(7777);                    //creating a new server socket(Connect to self, also could use 127.0.0.1)
        System.out.println("Server started up..");
       
        while(true){                                              //alows multiple clients to connect
        socket  = serverSocket.accept();                          //our client will send request and server will decided to accpet and send it through socket.
                                                                  //we want to create a new thread everytime we get a connection
        for(int i=0;i<10;i++){
            if (user[i]==null){                                   //if the first one is null create a new thread if not move on
             System.out.println("Connection from"+ socket.getInetAddress());
             out = new DataOutputStream(socket.getOutputStream());
             in = new DataInputStream(socket.getInputStream());
             user[i] = new Users(out,in,user, i);                 //give users new user id and then start a thread for them
             Thread thread = new Thread(user[i]);
             thread.start();
             break;
                 }
             }
        }
    }
}

//users class, variables are defined
class Users implements Runnable{
    DataOutputStream out;
    DataInputStream in;
    Users[] user = new Users[10];
    String name;
    int playerid;
    int playeridin;
    int xin;
    int yin;
//everytime we get an input we wanna send the output to every client
    public Users(DataOutputStream out, DataInputStream in,Users[] user, int pid){
        
        this.out = out;
        this.in = in;
        this.user = user;
        this.playerid = pid;
        
    }
   
    public void run(){
        
        try {
            out.writeInt(playerid);
        } catch (IOException ex) {
           System.out.println("Failed to send playerID");
        }
        while(true){
            try {
                playeridin = in.readInt();
                xin = in.readInt();
                yin = in.readInt();
                //will be able to see every player and their coords and id(will be used for painting)
                for(int i=0; i <10; i++){
                if (user[i] != null){                      //if user i exists send send coord
                    user[i].out.writeInt(playeridin);      //allows update of coords to the server
                    user[i].out.writeInt(xin);
                    user[i].out.writeInt(yin);
                        }
                    }
                
            } catch (IOException e) {
                this.user[playerid] =null;
                break;
            }
            }
        }
}
