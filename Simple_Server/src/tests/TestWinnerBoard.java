package tests;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import model.Winner;
import model.WinnerBoard;

class TestWinnerBoard {

	@Test
	void testSaveToFile() {
		String fileName = "..\\test.dat";
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		WinnerBoard board = new WinnerBoard();
		board.add(new Winner("Daniel", 10, 10));
		board.add(new Winner("Megan", 5, 5));
		board.saveToFile(fileName);
		assertTrue(file.exists());
		assertFalse(file.length() == 0);
		file.delete();
	}
	
	@Test
	void testReadFile() throws IOException {
		String fileName = "..\\test.dat";
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		WinnerBoard board = new WinnerBoard();
		String fileContents = "Daniel,10,10;\nMegan,10,10;";
		board.readFile(fileName);
		assertTrue(file.exists());
		assertTrue(file.length() == 0);
		assertTrue(board.isEmpty());
		BufferedWriter write = new BufferedWriter(new FileWriter(file));
		write.write(fileContents);
		write.close();
		board.readFile(fileName);
		assertFalse(board.isEmpty());
		assertEquals(2, board.size());
		String actString = "";
		for(int n = 0; n < board.size(); n++) {
			Winner winner = board.get(n);
			actString += winner.toString();
			if( n < board.size() - 1) {
				actString += "\n";
			}
		}
		actString.trim();
		assertEquals(fileContents, actString);
		file.delete();
	}

}
