import javax.swing.JFrame;


public class run {
	
	public static void main(String[] args)
	{
		client server = new client("localhost");
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.startRunning();
	}

}
