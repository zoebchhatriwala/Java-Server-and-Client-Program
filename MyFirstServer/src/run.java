import javax.swing.JFrame;


public class run {
	
	public static void main(String[] args)
	{
		Serverclass server = new Serverclass();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		server.startRunning();
	}

}
