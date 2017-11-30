package ufl.cs1.controllers;

import game.controllers.AttackerController;
import game.controllers.DefenderController;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;
import game.models.Maze;
import game.models.Node;

import java.util.List;

public final class StudentController implements DefenderController
{
	//PowerPill Pacman is closest to
	private static int PPnum=0;
	public static int followObject(List<Integer> directions,int ghostX,int ghostY, int objectX, int objectY,int xdis,int ydis){
		if (objectX > ghostX && directions.contains(1) && (xdis >= ydis)) {
			return 1;
		}
		if (objectX < ghostX && directions.contains(3) && (xdis >= ydis)) {
			return 3;
		}
		if (objectY > ghostY && directions.contains(2)&& (xdis >= ydis)) {
			return 2;
		}
		if (objectY < ghostY && directions.contains(0)&& (xdis >= ydis)) {
			return 0;
		}
		if (objectY > ghostY && directions.contains(2)&& (ydis >= xdis)) {
			return 2;
		}
		if (objectY < ghostY && directions.contains(0)&& (ydis >= xdis)) {
			return 0;
		}
		if (objectX > ghostX && directions.contains(1) && (ydis >= xdis)) {
			return 1;
		}
		if (objectX < ghostX && directions.contains(3) && (ydis >= xdis)) {
			return 3;
		}
		return -1;
	}
	public static double distance(int x1, int x2, int y1, int y2){
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	public enum isHungry{EATEN,NOTEATEN,NEARPILL};
	private static int pillCount = 4;
	public void init(Game game) { }

	public void shutdown(Game game) { }

	//Continuously called to update game
	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];

		//Sets Pacman state to not eaten
		isHungry ishungry = isHungry.NOTEATEN;

		List<Defender> enemies = game.getDefenders();
		Attacker pacman = game.getAttacker();
		Maze maze = game.getCurMaze();
		List<Node> Ppills= maze.getPowerPillNodes();
		double pacToPillDist = 0, ghostToGhostDist = 0;
		int pacx = pacman.getLocation().getX();
		int pacy = pacman.getLocation().getY();

		//Location of all Power Pills
		Node PP1 = Ppills.get(0), PP2 = Ppills.get(1), PP3 = Ppills.get(2), PP4 = Ppills.get(3);

		//Behaviour for all ghosts
		for(int i = 0; i<actions.length;i++) {
			//Gets current ghost
			Defender defender = enemies.get(i);

			//List of possible ghost movement directions
			List<Integer> possibleDirs = defender.getPossibleDirs();

			//Gets current defender X and Y location
			int ghostx = defender.getLocation().getX();
			int ghosty = defender.getLocation().getY();

			int pacDisX = Math.abs(pacx - ghostx);
			int pacDisY = Math.abs(pacy - ghosty);
			if (possibleDirs.size() != 0) {
				//Gets distance between Pacman and Power Pills
				for (int j = 0; j < 4; j++) {
					int Ppillx = Ppills.get(j).getX();
					int Ppilly = Ppills.get(j).getY();
					if (game.checkPowerPill(Ppills.get(j))) {
						pacToPillDist = distance(pacx, Ppillx, pacy, Ppilly);
						if (pacToPillDist <= 5) {
							//System.out.println("PACMAN IS APPROACHING A POWER PILL " + j);
							ishungry = isHungry.NEARPILL;
							PPnum = j;
							break;
						}
					}
				}
				//Checks if a pill has been eaten
				if (game.getPowerPillList().size() < pillCount) {
					ishungry = isHungry.EATEN;
					System.out.println("Pacman ate Power Pill #" +(PPnum+1));
					pillCount--;
				}

				//Behaviour for ghosts if pacman has eaten a pill
				if (ishungry == isHungry.EATEN) {
					//Do something
				}

				//Behaviour for ghosts if pacman is near pill
				if (ishungry == isHungry.NEARPILL) {
					actions[i] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
//					if (PPnum == 0) {
//						actions[0] = followObject(possibleDirs, ghostx, ghosty, PP2.getX(), PP2.getY(), Math.abs(PP2.getY() - ghostx), Math.abs(PP2.getY() - ghosty));
//					} else if (PPnum == 1) {
//						actions[0] = followObject(possibleDirs, ghostx, ghosty, PP3.getX(), PP3.getY(), Math.abs(PP3.getY() - ghostx), Math.abs(PP3.getY() - ghosty));
//					} else if (PPnum == 2) {
//						actions[0] = followObject(possibleDirs, ghostx, ghosty, PP4.getX(), PP4.getY(), Math.abs(PP4.getY() - ghostx), Math.abs(PP4.getY() - ghosty));
//					} else if (PPnum == 3) {
//						actions[0] = followObject(possibleDirs, ghostx, ghosty, PP1.getX(), PP1.getY(), Math.abs(PP1.getY() - ghostx), Math.abs(PP1.getY() - ghosty));
//					}
//					for(int i1 = 0; i1<enemies.size();i1++){
//						if(i1==i) {
//							continue;
//						}
//						ghostToGhostDist = distance(ghostx,enemies.get(i1).getLocation().getX(),ghosty,enemies.get(i1).getLocation().getY());
//						if(ghostToGhostDist>100){
//							System.out.println("PACMAN IS APPROACHING A POWER PILL RETREAT!!!!!!");
//							actions[i] = defender.getNextDir(pacman.getLocation(),false);
//						}
//						else{
//							actions[i] = defender.getNextDir(pacman.getLocation(),true);
//						}
//					}
				}

				//Behaviour for ghosts if pacman has not eaten a pill
				if (ishungry == isHungry.NOTEATEN) {
					//Moves ghost 1 to pacmans location if he is nearby
					if (distance(pacx, PP1.getX(), pacy, PP1.getY()) < 25||distance(pacx,PP3.getX(),pacy,PP3.getY())<25) {
						if(i==0) {
							actions[0] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
						}
						else if(i==2) {
							actions[2] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
						}
					}
					else {
						//Moves ghost 1 and 3 to power pill nodes if pacman is not nearby
						if (i == 0) {
							if(game.checkPowerPill(PP1)) {
								actions[0] = followObject(possibleDirs, ghostx, ghosty, PP1.getX(), PP1.getY(), Math.abs(PP1.getY() - ghostx), Math.abs(PP1.getY() - ghosty));
							}
							else if(PPnum==0&&game.checkPowerPill(PP2)){
								actions[0] = followObject(possibleDirs, ghostx, ghosty, PP2.getX(), PP2.getY(), Math.abs(PP2.getY() - ghostx), Math.abs(PP2.getY() - ghosty));
							}
							else if((PPnum==1||PPnum==2)&&game.checkPowerPill(PP4)){
								actions[0] = followObject(possibleDirs, ghostx, ghosty, PP4.getX(), PP4.getY(), Math.abs(PP4.getY() - ghostx), Math.abs(PP4.getY() - ghosty));
							}
							else if(PPnum==3&&game.checkPowerPill(PP3)){
								actions[0] = followObject(possibleDirs, ghostx, ghosty, PP3.getX(), PP3.getY(), Math.abs(PP3.getY() - ghostx), Math.abs(PP3.getY() - ghosty));
							}
							else{
								actions[0] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
							}
						}
						//Moves ghost 2 to power pill nodes if pacman is not nearby
						else if (i == 2) {
							if(game.checkPowerPill(PP3)) {
								actions[2] = followObject(possibleDirs, ghostx, ghosty, PP3.getX(), PP3.getY(), Math.abs(PP3.getY() - ghostx), Math.abs(PP3.getY() - ghosty));
							}
							else if(PPnum==2&&game.checkPowerPill(PP4)){
								actions[2] = followObject(possibleDirs, ghostx, ghosty, PP4.getX(), PP4.getY(), Math.abs(PP4.getY() - ghostx), Math.abs(PP4.getY() - ghosty));
							}
							else if(PPnum==1&&game.checkPowerPill(PP1)){
								actions[2] = followObject(possibleDirs, ghostx, ghosty, PP1.getX(), PP1.getY(), Math.abs(PP1.getY() - ghostx), Math.abs(PP1.getY() - ghosty));
							}
							else if(PPnum==3&&game.checkPowerPill(PP2)){
								actions[2] = followObject(possibleDirs, ghostx, ghosty, PP2.getX(), PP2.getY(), Math.abs(PP2.getY() - ghostx), Math.abs(PP2.getY() - ghosty));
							}
							else{
								actions[2] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
							}
						}
					}
					//Moves ghost 2 and 4 to pacmans location if he is nearby
					if (distance(pacx, PP2.getX(), pacy, PP2.getY()) < 25||distance(pacx,PP4.getX(),pacy,PP4.getY())<25) {
						if(i==1) {
							actions[1] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
						}
						else if(i==3) {
							actions[3] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
						}
					}
					else {
						//Moves ghost 2 to power pill nodes if pacman is not nearby
						if (i == 1) {
							if(game.checkPowerPill(PP2)) {
								actions[1] = followObject(possibleDirs, ghostx, ghosty, PP2.getX(), PP2.getY(), Math.abs(PP2.getY() - ghostx), Math.abs(PP2.getY() - ghosty));
							}
							else if(PPnum==2&&game.checkPowerPill(PP4)){
								actions[1] = followObject(possibleDirs, ghostx, ghosty, PP4.getX(), PP4.getY(), Math.abs(PP4.getY() - ghostx), Math.abs(PP4.getY() - ghosty));
							}
							else if(PPnum==0&&game.checkPowerPill(PP3)){
								actions[1] = followObject(possibleDirs, ghostx, ghosty, PP3.getX(), PP3.getY(), Math.abs(PP3.getY() - ghostx), Math.abs(PP3.getY() - ghosty));
							}
							else if(PPnum==3&&game.checkPowerPill(PP3)){
								actions[1] = followObject(possibleDirs, ghostx, ghosty, PP3.getX(), PP3.getY(), Math.abs(PP3.getY() - ghostx), Math.abs(PP3.getY() - ghosty));
							}
							else{
								actions[1] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
							}
						}
						//Moves ghost 4 to power pill nodes if pacman is not nearby
						else if(i==3){
							if(game.checkPowerPill(PP4)) {
								actions[3] = followObject(possibleDirs, ghostx, ghosty, PP4.getX(), PP4.getY(), Math.abs(PP4.getY() - ghostx), Math.abs(PP4.getY() - ghosty));
							}
							else if(PPnum==1&&game.checkPowerPill(PP3)){
								actions[3] = followObject(possibleDirs, ghostx, ghosty, PP3.getX(), PP3.getY(), Math.abs(PP3.getY() - ghostx), Math.abs(PP3.getY() - ghosty));
							}
							else if(PPnum==2&&game.checkPowerPill(PP1)){
								actions[3] = followObject(possibleDirs, ghostx, ghosty, PP1.getX(), PP1.getY(), Math.abs(PP1.getY() - ghostx), Math.abs(PP1.getY() - ghosty));
							}
							else if(PPnum==3&&game.checkPowerPill(PP2)){
								actions[3] = followObject(possibleDirs, ghostx, ghosty, PP2.getX(), PP2.getY(), Math.abs(PP2.getY() - ghostx), Math.abs(PP2.getY() - ghosty));
							}
							else{
								actions[3] = followObject(possibleDirs, ghostx, ghosty, pacx, pacy, pacDisX, pacDisY);
							}
						}
					}
				}
			}
			else {
				actions[i] = -1;
			}
		}
		//System.out.printf("Pacman's current location is X: %d Y: %d\n",pacx,pacy);
		return actions;
	}
}