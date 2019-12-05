import java.util.*;
import java.io.*;
import java.net.*;

class Reader implements Runnable
{
    DataInputStream dis=null;
    Thread t=null;
    public Reader(DataInputStream dis)
    {
            this.dis=dis;
            t=new Thread(this);
            t.start();
    }
    public void run()
    {   
        while(!Client.suspend)
        {
            try{
                System.out.println(dis.readUTF());
            }
            catch(IOException io){System.out.println(io);}
        }
    }
}
public class Client
{
    static DataInputStream dis=null;
    static DataOutputStream dos=null;
    static Socket socket=null;
    static boolean suspend=false;
    public static void main(String[] args) throws IOException{
        
        socket=new Socket("192.168.43.221",5000); 
        System.out.println("Connected Successfully...");
        dis=new DataInputStream(socket.getInputStream()); //DataIpStream(InputStream)
        dos=new DataOutputStream(socket.getOutputStream());
        Reader r=new Reader(dis);
        Scanner s=new Scanner(System.in);
        String msg="";
        while(!msg.equals("exit"))
        {
            msg=s.nextLine();
            if(msg.equals("exit")) suspend=true;
            dos.writeUTF(msg);
        }
        s.close();
        try{r.t.join();}catch(InterruptedException ie){}
        socket.close();
        dos.close();
        dis.close();
    }
}