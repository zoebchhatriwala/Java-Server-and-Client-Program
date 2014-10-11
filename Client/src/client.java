import java.io.*;
import java.net.*;
import java.awt.*;
//import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class client extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usertext;
	private JTextArea chatwindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;
	private String Ip;
	
	public client(String host)
	{
		super("Client");
		Ip = host;
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
			while(true)
			{
			try{
				waitForServer();
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
	 public void EstConnection() throws IOException
	   {
		    output = new ObjectOutputStream(connection.getOutputStream());
		    output.flush();
		    input = new ObjectInputStream(connection.getInputStream());
		    showMessage("Connection Established");
	   }
	
	public void whilechatting() throws IOException
	   {
		  String Message="";
		  ableToType(true);
		  do{
			  try{
				  Message = (String) input.readObject();
				  showMessage("\n"+Message);
				  }catch(ClassNotFoundException e){showMessage(e.toString());}
			  
		  }while(!Message.equals("Server - END"));
	   }
	public void CloseAll()
	   {
		   showMessage("Closing Connection");
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
				   );}
	 public void waitForServer() throws IOException
		{
			showMessage("Waiting for Server...");
			connection = new Socket(InetAddress.getByName(Ip),5253);
			showMessage("Connected to "+connection.getInetAddress().getHostName());
		}
	
	 public void sendMessage(String x)
	   {
		   try
		   {
			   output.writeObject("Client - " + x);
			   output.flush();
			   showMessage("Client - "+x);
		   }catch(IOException e){chatwindow.append("ERROR");}
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
}
