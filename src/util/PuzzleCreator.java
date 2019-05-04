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

public class PuzzleCreator {
	
	public ArrayList<GridCell> getPuzzle(Grid grid) {
		
		ArrayList<GridCell> initialFlowPointers = new ArrayList<GridCell>();
		
		List<List<String>> puzzlesArray = new ArrayList<List<String>>();				
		String[] values = null;
		
		try (BufferedReader reader = new BufferedReader(new FileReader("/home/irixa/git/AI-FreeFlowPuzzle/src/util/puzzles.csv"));) {
			String row;
			while ((row = reader.readLine()) != null) {
				System.out.println(row);
				values = row.split(",");
		        puzzlesArray.add(Arrays.asList(values));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Random rand = new Random();
		List<String> colorCellsArray = puzzlesArray.get(rand.nextInt(puzzlesArray.size()));
		System.out.println(colorCellsArray);
		
		for (String cell : colorCellsArray) {
			String[] attr = cell.split(" ");
			int rowPos = Integer.valueOf(attr[1]);
			int colPos = Integer.valueOf(attr[2]);
			Pos pos = new Pos(rowPos, colPos);
			Color color = this.getColor(attr[0]);
			
			initialFlowPointers.add(new GridCell(grid, pos, color));
			
		}
		
		System.out.print(initialFlowPointers);
				
		return initialFlowPointers;
	}
	
	private Color getColor(String c) {
		switch(c.toLowerCase()) {
		case("r"):
			return Color.red;
		case("y"):
			return Color.yellow;
		case("b"):
			return Color.blue;
		case("o"):
			return Color.orange;
		case("g"):
			return Color.green;
		default:
			return null;
		}
	}

}
