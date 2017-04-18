//Followed turoial on https://www.youtube.com/watch?v=1a3TtPr_yvI 
package servermud;

/**
 *
 * @author Dean
 */
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
    
    public static void main(String[]args)throws Exception{
        System.out.println("Starting up server...");
        serverSocket = new ServerSocket(7777);              //creating a new server sockete(Connect to self, also could use 127.0.0.1)
        System.out.println("Server started up..");
       
        while(true){                                        //alows multiple clients to connect
        socket  = serverSocket.accept();                    //our client will send request and server will decided to accpet and send it through socket.
                                                            //we want to create a new thread everytime we get a connection
        for(int i=0;i<10;i++){
        System.out.println("Connection from"+ socket.getInetAddress());
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        
        if (user[i]==null){                                 //if the first one is null create a new thread if not move on
                user[i] = new Users(out,in,user);
            Thread thread = new Thread(user[i]);
            thread.start();
            break;
        
        
                           }
        }
        }
    }
}

class Users implements Runnable{
    DataOutputStream out;
    DataInputStream in;
    Users[] user = new Users[10];
    String name;
//everytime we get an input we wanna send the output to every client
    public Users(DataOutputStream out, DataInputStream in,Users[] user){
        
        this.out = out;
        this.in = in;
        this.user = user;
        
    }
   
    
    public void run(){
        
        try {
            name = in.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true){
            try {
                //if we get a message form the client we want to send that message to all the users.
                String message = in.readUTF();
                for(int i=0; i <10; i++){
                if (user[i] != null){                      //if user i exists send user i message
                    user[i].out.writeUTF(name +": " +message);
                
                }       
                
                }
                
            } catch (IOException e) {
                this.out = null;
                this.in = null;
            }
            
            
            }   
    
        }
}
