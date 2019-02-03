package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Sets up the bash script for you.
 * 
 * @author dpgra
 *
 */
public class Installer {

	public static void main(String[] args) throws IOException {
		File file = new File("/.bash_profile");
		File script = new File("bin/");
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String fileContents = "";
		while(line != null) {
			fileContents += line + "\n";
			line = reader.readLine();
		}
		reader.close();
		fileContents += "export PATH=\"$PATH:\"" + script.getAbsolutePath() + "\n";
	}
}
