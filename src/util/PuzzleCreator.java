package util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import app.Grid;
import app.GridCell;
import app.Pos;

/**
 * This class creates a puzzle that takes as a parameter the grid to be used and dimensions of the grid
 * Parses flow pointers from a csv file and returns them as a list of GridCell objects
 *
 */
public class PuzzleCreator 
{	
	public ArrayList<GridCell> getPuzzle(Grid grid, String dimensions) {	
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();

		List<List<String>> puzzlesArray = new ArrayList<List<String>>();				

		try (BufferedReader reader = new BufferedReader(new FileReader("src/input/" + dimensions + ".csv"));) {
			// A row contains the list of initial flow pointer for a single puzzle
			String row;

			while ((row = reader.readLine()) != null) {
				// Create an array of the flow pointers				
				String[] values = row.split(",");
				// Add array to array of puzzles
		        puzzlesArray.add(Arrays.asList(values));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Select a random puzzle from the array of puzzles
		Random rand = new Random();
		List<String> colorCellsArray = puzzlesArray.get(rand.nextInt(puzzlesArray.size()));
		
		for (String cell : colorCellsArray) {
			// Each element of the array (puzzle) is an initial flow pointer in the form 'C Row Col'
			String[] attr = cell.split(" ");
			// First element is a letter that maps to a color
			Color color = this.getColor(attr[0]);
			// Second element is the row position
			int rowPos = Integer.valueOf(attr[1]);
			// Third element is the column position
			int colPos = Integer.valueOf(attr[2]);
			// Create a Pos object with row and col values
			Pos pos = new Pos(rowPos, colPos);
			
			// Create GridCell object and add to initial flow pointers array
			initialFlowPointers.add(new GridCell(grid, pos, color));
			
		}
		
		// testing purposes
//		System.out.print(initialFlowPointers);
				
		return initialFlowPointers;
	}
	
	
	/**
	 * @param c : string that maps to a color
	 * @return object from the class Color
	 */
	private Color getColor(String c) {
		switch(c.toLowerCase()) {
			case("r"): // red
				return Color.red;
			case("y"): // yellow
				return Color.yellow;
			case("b"): // blue
				return Color.blue;
			case("o"): // orange
				return Color.orange;
			case("g"): // green
				return Color.green;
			case("c"): // cyan
				return Color.cyan;
			case("p"): // purple
				return Color.pink;
			case("w"): // wine
				return Color.black;
			case("m"): // magenta
				return Color.magenta;
			default:
				return null;
		}
	}
}
