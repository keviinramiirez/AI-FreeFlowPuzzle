package flowPointer;

import java.awt.Color;

public class FlowPointer 
{
	//	finished: boolean (maybe)
	//	visited: hashset (maybe)
	public int heuristic = 0;
	public String position = "";
	public Color color;
	public FlowPointer parent, pairFlowPointer;
	public boolean wasMoveForced;
	
	public FlowPointer() {
//		color = ColorManager.DEFAULT_COLOR;
		color = new Color(240, 240, 240);
	}
}
