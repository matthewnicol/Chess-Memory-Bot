package chessFICSConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public abstract class ServerPipes {
	Socket socket;
	BufferedReader r;
	PrintWriter w;
	boolean printInput;
	boolean printOutput;
		
	/**
	 * @param serverIP
	 * @param verbalIn
	 * @param verbalOut
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ServerPipes(String serverIP, boolean verbalIn, boolean verbalOut) throws UnknownHostException, IOException {
		socket = new Socket(serverIP, 5000);
		socket.setKeepAlive(true);
		r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		w = new PrintWriter(socket.getOutputStream(),true);
		printOutput = verbalOut;
		printInput = verbalIn;
	}
	
	protected final static void debug(boolean b, String s) {
		if (b) { System.out.println(s); }
	}
	
	/**
	 * Send commmand to server.
	 * 
	 * Write a command to the server and send. Print it if debugging.
	 * @param s the command to send to the server.
	 */
	public final void write(String s) {
		debug(printOutput, "WRITING TO SERVER: " + s);
		w.write(s);
		w.write("\r\n");
		w.flush();
	}
	
	protected final void tellHuman(String h, String s) {
		write("tell " + h + " " + s);
	}
	
	/**
	 * Read command from server.
	 * 
	 * Wait until the server has information to send us and then return it.
	 * @return the next line of information from the server.
	 * @throws IOException
	 */
	protected final String getLine() throws IOException {
		while (!r.ready()) {}
		String nextLine = r.readLine();
		debug(printInput, "SERVER MESSAGE: " + nextLine);
		return nextLine;
		
	}
	
}
