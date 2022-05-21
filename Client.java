import java.io.*;
import java.net.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame
{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Components declarations

    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.BOLD,22);





    public Client()
    {
        try {
            
            System.out.println("Sending request to server");
            socket=new Socket("10.50.104.43",7777);
            System.out.println("connection done");

            br=new BufferedReader(new InputStreamReader(socket.
            getInputStream())); // reading
            
            out=new PrintWriter(socket.getOutputStream()); //writing getting-> pipe

            createGUI();
            handleEvents();
            startReading();
            //startWriting();

        } catch (Exception e) {
           
        }   
    }

    private void handleEvents() 
    {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub\

                //System.out.println("KEY RELEASED "+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    // System.out.println("You Pressed 'ENTER' button! ");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }
            
        });
    }

    private void createGUI() 
    {
        // GUI code goes here
        this.setTitle("Client Messager[END]");
        this.setSize(650,650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // this.setBackground( Color.RED);

        // component coding

        heading.setFont(font);
     
        messageArea.setFont(font);
        messageInput.setFont(font);
        // headings
        heading.setIcon(new ImageIcon("clogo.png"));
        heading.setBackground(Color.red);
        //heading.setSize(50,50);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // layout for frame

        this.setLayout(new BorderLayout());

        // components to frame 

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        
        

        this.setVisible(true);
    }

    // start reading
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
                       System.out.println("The Server terminated the chat");
                       JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                       messageInput.setEnabled(false);
                       socket.close();

                       break;
                   }
   
                //    System.out.println("Server : "+msg);
                messageArea.append("Server : "+ msg+"\n");
                
            }
        }catch(Exception e)
        {
            System.out.println("Connection is terminated and closed! ");
        }

        };
        
        new Thread(r1).start();
    }

    // start writing
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

            //System.out.println("Connection is terminated and closed! ");


        }catch(Exception e)
        {
           e.printStackTrace();
        }

        };

        new Thread(r2).start();
    }





    public static void main(String[] args)
    {

        System.out.println("This is the client");
        new Client();
    }
}
