import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;



class Server
{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server() // This is the constructor
    {
        try {
            server=new ServerSocket(7777);// giving the specific port to activate only that server.
            System.out.println("The server is ready to accept the connection");
            System.out.println("Waiting the connection...");
            socket=server.accept();

            br=new BufferedReader(new InputStreamReader(socket.
            getInputStream())); // reading
            
            out=new PrintWriter(socket.getOutputStream()); //writing getting-> pipe

            startReading();
            startWriting();



        } catch (Exception e) {
            
            e.printStackTrace();
        } 
    }

    public void startReading()
    {

        // thread - reads after fetching it

        Runnable r1=()->{

            System.out.println("Reading started");

            try{


            while(true)
            {
                
                    String msg = br.readLine();
                    if(msg.equals("exit"))
                   {
                       System.out.println("Client wants to stop and terminated the chat");
                       
                       socket.close();
                       
                       break;
                   }
   
                   System.out.println("Client : "+msg);
                
            }

        }catch(Exception e)
        {
           // e.printStackTrace();
           System.out.println("Connection is terminated and closed! ");
        }

        };
        
        new Thread(r1).start();
    }

    public void startWriting()
    {
        // thread - 

        Runnable r2=()->{

            System.out.println("writing  started...");
            try{
            while(!socket.isClosed())
            {
                

                    BufferedReader br1=new BufferedReader(new
                    InputStreamReader(System.in));



                    String content=br1.readLine();

                   
                    out.println(content);
                    out.flush();

                    if (content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                
            }
        }catch(Exception e)
        {
           // e.printStackTrace();
           System.out.println("Connection is terminated and closed! ");
        }

        //System.out.println("Connection is terminated and closed! ");

        };

        new Thread(r2).start();
    }

    public static void main(String[] args)
    {
        System.out.println("this is a server.. and is starting");
        new Server(); // object being called
    }

}