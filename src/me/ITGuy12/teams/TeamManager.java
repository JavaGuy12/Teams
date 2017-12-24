package me.ITGuy12.teams;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class TeamManager {

	
	private static TeamManager tm = new TeamManager();
	
	public static TeamManager getTeamManager() {
		return tm;
	}
	
	private ArrayList<Team> teams;
	
	private TeamManager() {
		this.teams = new ArrayList<Team>();
	}
	
	public Team createTeam(Player leader) {
		Team t = new Team(leader);
		teams.add(t);
		return t;
	}
	
	public void removeTeam(Team t) {
		teams.remove(t);
		return;
	}
	
	public boolean isInATeam(Player p) {
		for(Team t : teams) {
			if(t.contains(p)) {
				return true;
			}
		}
		return false;
	}
	
	public Team getTeam(Player leader) {
		for (Team t : teams) {
			if(t.getLeader().getName() == leader.getName()) {
				return t;
			}
		}
		return null;
	}
	
	public Team getTeamForMember(Player p) {
		for(Team t : teams) {
			if(t.contains(p)) {
				return t;
			}
		}
		return null;
	}
	
}
