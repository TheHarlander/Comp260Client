//Followed turoial on https://www.youtube.com/watch?v=1a3TtPr_yvI 
package servermud;

/**
 *
 * @author Dean
 */
import java.io.*;
import java.net.*;

public class Server {
 
    
    static ServerSocket serverSocket;
    static Socket socket; 
    static DataOutputStream out;
    
    
    public static void main(String[]args)throws Exception{
        System.out.println("Start up server...");
        serverSocket = new ServerSocket(7777);//creating a new server sockete(Connect to self, also could use 127.0.0.1)
        System.out.println("Server started up..");
        socket  = serverSocket.accept(); //our client will send request and server will decided to accpet and send it through socket.
        System.out.println("Connection from"+ socket.getInetAddress());
        out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("This is a test socket");
        System.out.println("Data has been sent");
        
    }
    
    
}
