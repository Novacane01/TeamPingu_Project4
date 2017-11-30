package ufl.cs1.controllers;

import game.controllers.AttackerController;
import game.controllers.DefenderController;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;

import java.util.List;

public final class StudentController implements DefenderController
{
	static int pillCount = 4;
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		Attacker pacman = game.getAttacker();
		Defender defender = enemies.get(0);
		int pacx = pacman.getLocation().getX();
		int pacy = pacman.getLocation().getY();
		int ghostx = defender.getLocation().getX();
		int ghosty = defender.getLocation().getY();
		int xdistance = Math.abs(pacx-ghostx);
		int ydistance = Math.abs(pacy-ghosty);
		List<Integer> possibleDirs = defender.getPossibleDirs();
		if (possibleDirs.size() != 0) {
			//actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
			if(pacx>ghostx&&possibleDirs.contains(1)&&(xdistance>ydistance)) {
				actions[0] = 1;
			}
			if(pacx<ghostx&&possibleDirs.contains(3)&&(xdistance>ydistance)){
				actions[0] = 3;
			}
			if(pacy>ghosty&&possibleDirs.contains(2)&&(ydistance>xdistance)){
				actions[0] = 2;
			}
			if(pacy<ghosty&&possibleDirs.contains(0)&&(ydistance>xdistance)){
				actions[0] = 0;
			}
		}
		else {
			actions[0] = -1;
		}
		if(game.getPowerPillList().size()<pillCount){
			System.out.println("Pill was eaten");
			pillCount--;
		}
		return actions;
	}
}