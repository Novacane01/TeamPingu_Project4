package ufl.cs1.controllers;

import game.controllers.AttackerController;
import game.controllers.DefenderController;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;
import game.models.Maze;
import game.system._Maze;

import java.util.List;

public final class StudentController implements DefenderController
{
	public enum isHungry{EATEN,NOTEATEN,NEARPILL};
	static int pillCount = 4;
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		isHungry ishungry = isHungry.NOTEATEN;
		List<Defender> enemies = game.getDefenders();
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		Attacker pacman = game.getAttacker();
		Maze maze = game.getCurMaze();
		List Ppills= game.getPowerPillList();
		double dist = 0;
		int pacx = pacman.getLocation().getX();
		int pacy = pacman.getLocation().getY();
		for(int i = 0; i<actions.length;i++) {
			Defender defender = enemies.get(i);
			List<Integer> possibleDirs = defender.getPossibleDirs();
			int ghostx = defender.getLocation().getX();
			int ghosty = defender.getLocation().getY();
			int xdistance = Math.abs(pacx - ghostx);
			int ydistance = Math.abs(pacy - ghosty);
			if (possibleDirs.size() != 0) {
				//actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));

				//Gets distance between Pacman and Power Pills
				for (int j = 0; j < Ppills.size(); j++) {
					int Ppillx = maze.getPowerPillNodes().get(j).getX();
					int Ppilly = maze.getPowerPillNodes().get(j).getY();
					dist = Math.sqrt(Math.pow(Ppillx - pacx, 2) + Math.pow(Ppilly - pacy, 2));
					if (dist < 5)
						ishungry = isHungry.NEARPILL;
					break;
				}

				//Checks if a pill has been eaten
				if (game.getPowerPillList().size() < pillCount) {
					ishungry = isHungry.EATEN;
					System.out.println("Pill was eaten");
					pillCount--;
				}

				//Behaviour for ghosts if pacman has eaten a pill
				if (ishungry == isHungry.EATEN) {
					//Do something
				}

				//Behaviour for ghosts if pacman is near pill
				if (ishungry == isHungry.NEARPILL) {
					System.out.println("PACMAN IS APPROACHING A POWER PILL!");
					actions[i] = defender.getNextDir(pacman.getLocation(), false);
				}

				//Behaviour for ghosts if pacman has not eaten a pill
				if (ishungry == isHungry.NOTEATEN) {
					if (pacx > ghostx && possibleDirs.contains(1) && (xdistance > ydistance)) {
						actions[i] = 1;
					}
					if (pacx < ghostx && possibleDirs.contains(3) && (xdistance > ydistance)) {
						actions[i] = 3;
					}
					if (pacy > ghosty && possibleDirs.contains(2) && (ydistance > xdistance)) {
						actions[i] = 2;
					}
					if (pacy < ghosty && possibleDirs.contains(0) && (ydistance > xdistance)) {
						actions[i] = 0;
					}
				}
			} else {
				actions[0] = -1;
			}
		}
		return actions;
	}
}