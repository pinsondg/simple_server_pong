package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Sets up the bash script for you.
 * 
 * @author dpgra
 *
 */
public class Installer {

	public static void main(String[] args) throws IOException {
		File file = new File(System.getProperty("user.home") + File.separator + ".profile");
		File script = new File("bin/");
		if (!file.exists()) {
			if (file.createNewFile()) {
				System.out.println("Created new file: " + file.getAbsolutePath());
			} else {
				System.out.println("Could not create file. Installation stopped.");
			}
		}
		if (script.isDirectory() && script.exists()) {
			File[] scripts = script.listFiles();
			for (File bashScript : scripts) {
				if (!bashScript.isDirectory() && bashScript.setExecutable(true, false)) {
					System.out.println("Set " + script.getAbsolutePath() + " to exicutible.");
				} else if (!bashScript.isDirectory()){
					System.out.println("Could not make " + script.getAbsolutePath() + " to exicutable. Use chmod 777 to set manually.");
				}
			}
		} else {
			System.out.println("Couldn't find basch script");
		}
		System.out.println("Modifying file: " + file.getAbsolutePath());
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String fileContents = "";
		while (line != null) {
			fileContents += line + "\n";
			line = reader.readLine();
		}
		reader.close();
		fileContents += "export PATH=\"$PATH:\"" + script.getAbsolutePath() + "\n";
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(fileContents);
		writer.close();
	}
}
