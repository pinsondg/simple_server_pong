package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import model.SimpleServer;
import model.WinnerBoard;

public class Server {

	public static String SAVE_FILE_LEADERBOARD;
	public static String HTML_FILE;
	public static String JAVASCRIPT_FILE;

	public static void main(String[] args) throws IOException {
		determineOS();
		String projectRoot = findProjectPath();
		System.out.println(projectRoot);
		SAVE_FILE_LEADERBOARD = projectRoot + "leaderboard.dat";
		HTML_FILE = projectRoot + "src/front_end/layout.html";
		JAVASCRIPT_FILE = projectRoot + "src/front_end/script.js";
		//System.out.println(System.getProperty("user.dir"));
		System.out.println("Use IPv4 address " + new String(findPublicIP()) + " to connect.");
		SimpleServer server1 = new SimpleServer(8080);
		SimpleServer server2 = new SimpleServer(80);
		modifyHTMLIP();
		WinnerBoard winners = setUpWinnerBoard();
		server1.addPostEventListener(winners);
		server1.startServer(HTML_FILE);
		server2.startServer(JAVASCRIPT_FILE);
	}
	
	
	private static WinnerBoard setUpWinnerBoard() {
		WinnerBoard retWinners = new WinnerBoard();
		if (!retWinners.readFile(SAVE_FILE_LEADERBOARD)) {
			File file = new File(SAVE_FILE_LEADERBOARD);
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retWinners;
	}


	private static void modifyHTMLIP() throws IOException {
		File inFile = new File(HTML_FILE);
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		String file = "";
		String line = reader.readLine();
		while (line != null) {
			file += line + "\n";
			line = reader.readLine();
		}
		reader.close();
	    int beginIndex = file.indexOf("src=") + 4;
	    int endIndex = file.indexOf("\"", beginIndex + 3) + 1;
	    byte[] bytes = findPublicIP();
	    String ip = "\"http://" + new String(bytes) + ":80\"";
	    String retString = file.substring(0, beginIndex);
	    retString += ip;
	    retString += file.substring(endIndex);
	    BufferedWriter write = new BufferedWriter(new FileWriter(inFile));
	    if (inFile.canWrite()) {
	    	write.write(retString);
	    } else {
	    	System.out.println("Can't write to HTML doc.");
	    }
	    write.close();
	}


	public static byte[] findPublicIP() throws MalformedURLException, IOException {
		URL publicIP = new URL("http://bot.whatismyipaddress.com");
	    byte[] bytes = new byte[225];
	    int n = publicIP.openStream().read(bytes);
	    return Arrays.copyOf(bytes, n);
	}
	
	private static void determineOS() {
		String osName = System.getProperty("os.name");
		if (osName.contains("Windows")) {
			SAVE_FILE_LEADERBOARD.replace('\\', '/');
			HTML_FILE.replace('\\', '/');
			JAVASCRIPT_FILE.replace('\\', '/');
		}
	}

	private static String findProjectPath() {
		File file = new File(".");
		String currDirName = file.getAbsolutePath();
		String[] dirs = currDirName.split(File.pathSeparator);
		String retPath = "";
		for (String dir : dirs) {
			retPath += dir + File.pathSeparator;
			if (dir.contains("Simple_Server")) {
				return retPath;
			}
		}
		return retPath;
	}
}
