package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import controller.Server;

public class WinnerBoard extends LinkedList<Winner> implements OnPostEventListener {

	public final static int MAX_SIZE = 15;
	
	@Override
	public void onPostEvent(PostEvent event) {
		if (!event.getMessage().equals("Initial")) {
			try {
				this.add(new Winner(event.getMessage()));
				System.out.println("Winner added!");
				this.writeHTMLToFile();
			} catch (MessageFormatException | IOException e1) {
				
			}
		}
		try {
			String table = this.toHTMLTable();
			//System.out.println(table);
			OutputStream out = event.getSocket().getOutputStream();
			out.write("HTTP/1.1 200 OK\nContent-Type: text/html\r\n\r\n".getBytes());
			out.write(table.getBytes());
			out.close();
			this.saveToFile(Server.SAVE_FILE_LEADERBOARD);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Override the add method to make a stack.
	 * 
	 * @param winner the winner to add
	 */
	@Override
	public boolean add(Winner winner) {
		if (winner != null) {
			if(this.size() >= 15) {
				while(this.size() >= 15) {
					this.remove(this.size() - 1);
				}
			}
			this.add(0, winner);
		} else {
			return false;
		}
		return true;
	}
	
	
	public String toHTMLTable() {
		String retString = "<table style=\"width:25%\">\n<tr>\n<th>Name</th>\n<th>Score</th>\n</tr>\n";

		for(Winner winner : this) {
			retString += "<tr>\n <td> " + winner.getName() + "</td>\n<td> " + winner.getScore() + "</td>\n</tr>\n";
		}
		return retString;
	}
	
	public void writeHTMLToFile() throws IOException {
		File file = new File("..\\table.html");
		BufferedWriter write = new BufferedWriter(new FileWriter(file));
		write.write("<!DOCTYPE html>\r\n" + 
				"<html lang=\"en\" dir=\"ltr\">\r\n" + 
				"  <head>\r\n" + 
				"    <meta charset=\"utf-8\">\r\n" + 
				"    <title>LeaderBoards</title>\r\n" + 
				"  </head>\r\n" + 
				"  <body>");
		write.write(toHTMLTable());
		write.write("</body>\r\n" + 
				"</html>");
		write.close();
	}
	
	
	public boolean saveToFile(String fileName) {
		File file = new File(fileName);
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(file));
			for(Winner winner : this) {
				write.write(winner.toString() + "\n");
			}
			write.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public boolean readFile(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File( fileName)));
			String line = reader.readLine();
			while(line != null) {
				int index = line.indexOf(',');
				String name = line.substring(0, index);
				int index2 = line.indexOf(',', index + 1);
				int leftScore = new Integer(line.substring(index + 1, index2)).intValue();
				int index3 = line.indexOf(';');
				int rightScore = new Integer(line.substring(index2 + 1, index3));
				this.addLast(new Winner(name, leftScore, rightScore));
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File doesn't exist!\n Creating new file...\n");
			File file = new File(fileName);
			try {
				file.createNewFile();
				System.out.println("File created! (" + file.getAbsolutePath() + ")");
				readFile(fileName);
			} catch (IOException e1) {
				System.out.print("Unknown Error!");
				System.exit(0);
			}
		} catch (IOException e) {
			System.out.println("Unknown problem reading file!");
			System.exit(0);
		}
		return true;
	}
}
