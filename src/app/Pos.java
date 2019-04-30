package app;

public class Pos 
{
	int row, col;
	public Pos(int row, int col) {
		this.col = col;
		this.row = row;
	}
	
	public String toString() {
		return "("+ row +","+ col +")";
	}
}
