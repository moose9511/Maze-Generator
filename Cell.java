package mazeGenerator;

public class Cell {
	
	private boolean isWall;
	private boolean isFinish;
	
	private String cellIcon;
	
	//----------CONSTRUCTOR-----------
	public Cell(boolean isWall) {
		this.isWall = isWall;
		isFinish = false;
		
		// default value is wall mode
		if(this.isWall == false) {
			this.cellIcon = "  ";
		} else {
			this.cellIcon = "██";
		}
	}
	
	//-----------METHODS--------------
	
	//----GET METHODS-----
	public String GetCellIcon() { return cellIcon; }
	public boolean GetIsWall() { return isWall; }
	
	//----SET METHODS-----
	// sets the state of the cell, changing the icon and isWall state
	public void SetCellState(String state) {
		switch(state.toLowerCase()) {
		case "wall":
			cellIcon = "██";
			isWall = true;
			isFinish = false;
			break;
		case "path":
			cellIcon = "  ";
			isWall = false;
			isFinish = false;
			break;
		case "start":
			cellIcon = "SS";
			isWall = false;
			isFinish = false;
			break;
		case "finish":
			cellIcon = "FF";
			isWall = false;
			isFinish = true;
			break;
		default:
			System.out.println("Incorrect call of SetCellState");
		}
	}
	
}
