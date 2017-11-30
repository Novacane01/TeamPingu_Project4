package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.*;

import java.util.List;

public final class StudentController implements DefenderController
{
    public boolean isHungry(Game game, Attacker attacker) {
        Maze bruh = game.getCurMaze();
        List<Node> powerPills = bruh.getPowerPillNodes();

        for (int i = 0; i < powerPills.size(); i++) {
            double dist = Math.sqrt(Math.pow((attacker.getLocation().getX() - powerPills.get(i).getX()), 2) + Math.pow((attacker.getLocation().getY() - powerPills.get(i).getY()), 2));
            if (dist <= 8) {
                return true;
            }
        }
        return false;
    }

    public enum isHungry {EATEN, NOTEATEN, ONPILL};

	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		Attacker pacman = game.getAttacker();

		for(int i = 0; i < actions.length; i++)
		{
			Defender defender = enemies.get(i);
			List<Integer> possibleDirs = defender.getPossibleDirs();
            int pacx = pacman.getLocation().getX();
            int pacy = pacman.getLocation().getY();
            int ghostx = defender.getLocation().getX();
            int ghosty = defender.getLocation().getY();
            int xdistance = Math.abs(pacx-ghostx);
            int ydistance = Math.abs(pacy-ghosty);

            if (possibleDirs.size() != 0) {
                //actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
                if(pacx>ghostx&&possibleDirs.contains(1)&&(xdistance>ydistance)) {
                    actions[i] = 1;
                }
                if(pacx<ghostx&&possibleDirs.contains(3)&&(xdistance>ydistance)){
                    actions[i] = 3;
                }
                if(pacy>ghosty&&possibleDirs.contains(2)&&(ydistance>xdistance)){
                    actions[i] = 2;
                }
                if(pacy<ghosty&&possibleDirs.contains(0)&&(ydistance>xdistance)){
                    actions[i] = 0;
                }
            }
			else
				actions[i] = -1;
		}
		System.out.println(actions[1]);
		return actions;
	}
}