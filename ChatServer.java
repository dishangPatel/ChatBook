import java.util.ArrayList;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.io.BufferedInputStream;

class ServerHandler implements Runnable
{
public static ArrayList<Socket> socketList=new ArrayList<Socket>();
public static ArrayList<ServerHandler> threadList=new ArrayList<ServerHandler>(); 
static int count=1;   
Socket s = null;
Thread thread=null;
DataInputStream dis=null;
DataOutputStream dos=null;

public ServerHandler(Socket s)
{
    this.s = s;
    thread = new Thread(this,"Client"+count++);
    try{
        dis=new DataInputStream(new BufferedInputStream(s.getInputStream()));
        dos=new DataOutputStream(s.getOutputStream());}
    catch(IOException io){}
    socketList.add(s);
    threadList.add(this);
    thread.start();    
}

        public void run()
        {
            try
            {
            String msg="";
            while(!msg.equals("exit"))
            {
                msg=dis.readUTF();
                //byte []bmsg = dis.read();
                // msg=new String(bmsg);
                if(!msg.equals("exit"))
                {for(int i=0;i<socketList.size();i++)
                {
                        try{if(threadList.get(i)!=this) {threadList.get(i).dos.writeUTF(msg);} }catch(IOException io){}
                }}
                else
                this.dos.writeUTF("");     
            }
            
            int tempcnt=0;
            for(Socket temp : socketList)
            {
                    if(temp==this.s)
                    {
                        socketList.remove(temp);
                        threadList.remove(tempcnt);
                        tempcnt=0;
                        break;
                    }
                    tempcnt++;
            }   
            s.close();
            //dis.close();
            //dos.close();
        }
        catch(IOException io){System.out.print(io);}
        }
}

public class ChatServer
{
    
public static void main(String []args) throws IOException
{

    ServerSocket server = new ServerSocket(5000);
    System.out.println("Server Started...");
    // SocketAddress SocketAddress = null;
    Socket socket=null;
    while(true)
    {
        try
        {
            System.out.println("Waiting for Client...");
            socket = server.accept();
            System.out.println("New Client Connected...");
            new ServerHandler(socket);    
        }
        catch(UnknownHostException uh) 
        {
            System.out.println(uh);
        } 
    }     
}
}