package mazeGenerator;


//------IMPORTS---------
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	static int balls = 0;
	// Random class object for use in all of Main
	private static Random rand = new Random();
	
	//private static Scanner scanner = new Scanner().;
	private static Scanner scanner = new Scanner(System.in);
	
	// global maze object that holds data that makes the maze
	private static Cell[][] maze;
	
	public static void main(String[] args) {
		System.out.print("Enter size: ");
		int size = Integer.parseInt(scanner.nextLine());
		
		GenerateMaze(size);
		
	}
	
 	//------------METHODS--------------
	
	//========================================MAIN GENERATE MAZE METHOD=========================================
	// generate maze of certain size 
	//default values for GenerateMaze()
	public static void GenerateMaze() {GenerateMaze(10, 10);}
	public static void GenerateMaze(int size) {GenerateMaze(size, size);}
 	public static void GenerateMaze(int sizeX, int sizeY)
	{
 		if(sizeX < 3) { sizeX = 3; }
 		if(sizeY < 3) { sizeY = 3; }
 		maze = new Cell[sizeX][sizeY];
 		
 		// initializes each element of the maze, default value is a wall
		for(int y = 0; y < maze[0].length; y++) {
			for(int x = 0; x < maze.length; x++) {
				maze[x][y] = new Cell(/*isWall*/ true);
			}
		}
		
		// initializes starting conditions, the start is always on the top and the next path is always one beneath
		boolean hasEndedCorrectPathCreation = false;
		
		int startPos = rand.nextInt((maze.length-2) - 1 + 1) + 1;
		
		maze[startPos][0].SetCellState("start");
		maze[startPos][1].SetCellState("path");
		
		ArrayList<int[]> pathCoords = new ArrayList<int[]>();
		
		pathCoords.add(new int[] {startPos, 0});
		pathCoords.add(new int[] {startPos, 1});
		
		
		// loops until the path reaches the bottom of the maze 
		do {
			
			// adds random direction to the last position of the path
			pathCoords.add(GetRandomDirection(pathCoords, pathCoords.size()-1, /*isCorrectPath*/true));
			maze[pathCoords.getLast()[0]][pathCoords.getLast()[1]].SetCellState("path");
			
			// if the path has gotten stuck, re-call the method to re-roll the path
			if(pathCoords.getLast()[0] == 0 && pathCoords.getLast()[1] == 0) {
				GenerateMaze(sizeX, sizeY);
				return;
				
			} else if(pathCoords.getLast()[1] == maze[0].length - 1) {
				
				hasEndedCorrectPathCreation = true;
				maze[pathCoords.getLast()[0]][pathCoords.getLast()[1]].SetCellState("finish");
				
			}
				
		} while(hasEndedCorrectPathCreation == false);
		
		// calls specified generation method
		System.out.print("Enter generation method: ");
		String generateMethod = scanner.nextLine();
		
		switch(generateMethod) {
		case "branch":
			BranchPaths(pathCoords);
			break;
		case "grow":
			GrowPaths(pathCoords);
			break;
		default:
			System.out.println("generation method doesn't exist");
		}
		
		
		PrintMazeResult(maze);
	}
 	
 	// places paths around existing paths, evenly spreads out from correct path, creating branching paths automatically
 	private static void GrowPaths(ArrayList<int[]> pathPoints)
 	{
 		// array list to be updated as more cells become paths
 		ArrayList<int[]> allPathPoints = pathPoints;
 		
 		// loops until every path cell has no valid direction
 		boolean growAllPossiblePaths = false;
 		do {
 			
 			// sets new size of points index after a loop of growing
 			int pathPointsIndex = allPathPoints.size();
 			int[] possiblePos;
 			int failedAttempts = 0;
 			
 			// loops for every current path cell, not counting the ones created in this loop
 			for(int i = 0; i < pathPointsIndex; i++) {
 				
 				possiblePos = GetRandomDirection(allPathPoints, i, false);
 				if(possiblePos[0] != 0 && possiblePos[1] != 0) {
 					allPathPoints.add(possiblePos);
 					maze[possiblePos[0]][possiblePos[1]].SetCellState("path");
 				} else {
 					failedAttempts++;
 				}
 				
 				if(failedAttempts == pathPointsIndex) {
 					growAllPossiblePaths = true;
 				}
 				
 			}
 			
 		} while(growAllPossiblePaths == false);
 	}
 	
 	// recursive branching method where it goes through each point in a branch to call itself to make more branches. TODO make more efficient
 	private static void BranchPaths(ArrayList<int[]> pathPoints)
 	{
 		
 		// goes through each point in a path to look for valid points to branch to
 		for(int point = 0; point < pathPoints.size(); point++) {
 			
 			boolean hasCreatedBranch = false;
 			
 			// array list for the current path positions to be saved to
 			ArrayList<int[]> currentPathPoints = new ArrayList<int[]>();
 			currentPathPoints.add(new int[]{pathPoints.get(point)[0], pathPoints.get(point)[1]});
 			
 			// tries to make branch from the path point it's on until it reaches a dead-end
 			do {
 		 		
 	 			// a single set of points to be saved onto to be applied to currentPathPoints
				int[] positionChange = new int[0];
				
 	 			// finds point to place tile from current path point
 				positionChange = GetRandomDirection(currentPathPoints, currentPathPoints.size()-1, false);
 	 			
 	 			// checks if new point was found, applies changes if so, ends path if not
 	 			if(positionChange[0] == 0 && positionChange[1] == 0) {
 	 				
 	 				
 	 				if(currentPathPoints.size() > 1) {
 	 					BranchPaths(currentPathPoints);
 	 				}
 	 				
 	 				hasCreatedBranch = true;
 	 			} else {
 	 				
 	 				maze[positionChange[0]][positionChange[1]].SetCellState("path");
 	 				currentPathPoints.add(positionChange);
 	 				
 	 			}
 	 			
 	 			
 			} while(hasCreatedBranch == false);
 		}
 	}
 	
 	// checks and returns a value change applied on the inputed path points in a valid random direction, returns {0, 0} if no directions are valid
 	private static int[] GetRandomDirection(ArrayList<int[]> currentPathPoints, int index, boolean isCorrectPath)
 	{
 		// creates array list for all possible directions, meant to be changed as directions become invalid
		ArrayList<Integer> directions = new ArrayList<Integer>();
		for(int i = 1; i < 5; i++) { directions.add(i); }
			
		// finds point to place tile from current path point
 		do {
 			
	 			// picks direction and removes it from available directions.
				int direction;
				if(directions.size() > 0) {
					int dirIndex = rand.nextInt(directions.size() - 1 + 1) + 1;
					direction = directions.get(dirIndex-1);
					directions.remove(dirIndex-1);
				} else {
					return new int[] {0, 0};
				}
				
				// 
				// picks direction, checks if it is valid, returns suggested position change
				switch(direction) {
				// up
				case 1:
					if(IsCellNearPath(currentPathPoints.get(index)[0], currentPathPoints.get(index)[1]-1, direction, maze, isCorrectPath) == false) {
						return new int[]{currentPathPoints.get(index)[0], currentPathPoints.get(index)[1]-1};
					} 
					break;
				// right
				case 2:
					if(IsCellNearPath(currentPathPoints.get(index)[0]+1, currentPathPoints.get(index)[1], direction, maze, isCorrectPath) == false) {
						return new int[]{currentPathPoints.get(index)[0]+1, currentPathPoints.get(index)[1]};
					} 
					break;
				// down
				case 3:
					if(IsCellNearPath(currentPathPoints.get(index)[0], currentPathPoints.get(index)[1]+1, direction, maze, isCorrectPath) == false) {
						return new int[]{currentPathPoints.get(index)[0], currentPathPoints.get(index)[1]+1};
					} 
					break;
				// left
				case 4:
					if(IsCellNearPath(currentPathPoints.get(index)[0]-1, currentPathPoints.get(index)[1], direction, maze, isCorrectPath) == false) {
						return new int[]{currentPathPoints.get(index)[0]-1, currentPathPoints.get(index)[1]};
					} 
					break;
				default:
					System.out.println("rand funcion error for GetRandomDirection");
					break;
				}
			
			} while(directions.size() > 0);
 		
 		return new int[] {0, 0};
 	}
 	
	// checks if specified cell is near a path excludes path it came from
 	private static boolean IsCellNearPath(int x, int y, int incomingDirection, Cell[][] maze) { return IsCellNearPath(x, y, incomingDirection, maze, false); } // default value of correct path is false
 	private static boolean IsCellNearPath(int x, int y, int incomingDirection, Cell[][] maze, boolean isCorrectPath)
 	{
 		// checks if any surrounding cells are outside maze bounds, excluding the bottom cells if it's supposed to be the finish line
 		if(isCorrectPath == true) {
 			
 			if(x-1 < 0 || x+1 > maze.length-1 || y-1 < 0 || maze[x][y].GetIsWall() == false) {
 	 			return true;
 	 		} else {
 	 			if(incomingDirection == 3 && y == maze[0].length-1) {
 	 				return false;
 	 			} 
 	 		}
 		// if it's not the correct path, checks if all surrounding cells and this position is out of bounds/invalid
 		} else {
 			
 			if(x-1 < 0 || x+1 > maze.length-1 || y-1 < 0 || y+1 > maze[0].length-1 || maze[x][y].GetIsWall() == false) {
 	 			return true;
 	 		}
 			
 		}
 		
 		// checks each direction if there is a path, excluding the path cell it came from
 		// 1 = up | 2 = right | 3 = down | 4 = left
 		int score = 0;
 		if(incomingDirection == 3 || maze[x][y - 1].GetIsWall() == true) { // checks up
 			score++;
 		}  
 		if(incomingDirection == 4 || maze[x + 1][y].GetIsWall() == true) { // checks right
 			score++;
 		} 
 		if(incomingDirection == 1 || maze[x][y + 1].GetIsWall() == true) { // checks down
 			score++;
 		} 
 		if(incomingDirection == 2 || maze[x - 1][y].GetIsWall() == true) { // checks left
 			score++;
 		}
 		
 		// if all surrounding cells are don't have paths, allow the placement of a path in this position
 		if(score == 4) {
 			return false;
 		} else {
 			return true;
 		}
 	}
 	
 	// prints the results of GenerateMaze by printing the string value of each cell
 	private static void PrintMazeResult(Cell[][] maze)
 	{
 		System.out.println();
 		for(int y = 0; y < maze[0].length; y++) {
 			for(int x = 0; x < maze.length; x++) {
 				System.out.print(maze[x][y].GetCellIcon());
 			}
 			System.out.println();
 		}
 	}
 }
