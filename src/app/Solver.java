package app;

public class Solver 
{
	Grid grid;
	Validation validation = new Validation();


	public Solver(Grid grid) {
		this.grid = grid;
	}
	
	public boolean validate(GridCell cell) {
		validation.isForceMove(cell);
		return true;
	}
}
