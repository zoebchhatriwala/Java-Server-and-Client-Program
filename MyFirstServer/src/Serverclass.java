import java.io.*;
import java.net.*;
import java.awt.*;
//import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



public class Serverclass extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usertext;
	private JTextArea chatwindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
 	
	public Serverclass()
	{
		super("Server");
		usertext = new JTextField();
		usertext.setEditable(false);
		usertext.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					usertext.setText("");
					}
				});
		add(usertext,BorderLayout.NORTH);
		chatwindow =  new JTextArea();
		add(new JScrollPane(chatwindow));
		setSize(300,200);
		setVisible(true);
		
	}
	
	public void startRunning()
	{
		try{
		server = new ServerSocket(5253,50);
		while(true)
		{
		try{
			waitForUser();
			EstConnection();
			whilechatting();
		}catch(EOFException e)
		{
			showMessage("Connection Ended!!!!");
		}
		finally
		{
		    CloseAll();
		}
		}}
		catch(IOException io)
		{
			io.getStackTrace();
		}
		}
    
   public void waitForUser() throws IOException
	{
		showMessage("Waiting for User...");
		connection = server.accept();
		showMessage("Connected to "+connection.getInetAddress().getHostName());
	}
   
   public void EstConnection() throws IOException
   {
	    output = new ObjectOutputStream(connection.getOutputStream());
	    output.flush();
	    input = new ObjectInputStream(connection.getInputStream());
	    showMessage("Connection Established");
   }
   
   public void whilechatting() throws IOException
   {
	  String Message = "You Are Now Connected";
	  sendMessage(Message);
	  ableToType(true);
	  do{
		  try{
			  Message = (String) input.readObject();
			  showMessage("\n"+Message);
		  }catch(ClassNotFoundException e){showMessage(e.toString());}
		  
	  }while(!Message.equals("Client - END"));
   }
   
   public void showMessage(final String x)
   {
	   SwingUtilities.invokeLater(
			   new Runnable()
			   {
				   public void run()
				   {
					   chatwindow.append("\n");
					   chatwindow.append(x);
				   }
			   }
			   );
   }
   
   public void CloseAll()
   {
	   showMessage("\nClosing Connections");
	   ableToType(false);
	   try{
		   output.close();
		   input.close();
		   connection.close();
	   }catch(IOException e)
	   {
		   e.printStackTrace();
	   }
   }
   
   public void sendMessage(String x)
   {
	   try
	   {
		   output.writeObject("Server - " + x);
		   output.flush();
		   showMessage("Server - "+x);
	   }catch(IOException e){chatwindow.append("ERROR");}
   }
   
   public void ableToType(final Boolean yeh)
   {
	   SwingUtilities.invokeLater(
			   new Runnable()
			   {
				   public void run()
				   {
					  usertext.setEditable(yeh);
				   }
			   }
			   );
	   
   }
}
