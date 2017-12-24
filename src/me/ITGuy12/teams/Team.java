package me.ITGuy12.teams;

import java.util.ArrayList; 

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class Team {

	private Player leader;
	private ArrayList<Player> members;
	
	public Team(Player leader) {
		this.leader = leader;
		this.members = new ArrayList<Player>();
		this.members.add(leader);
	}
	
	public Player getLeader() {
		return leader;
	}
	
	public void addPlayer(Player p) {
		if(members.contains(p)) {
			return;
		}
		members.add(p);
		for (Player pl : members) {
			pl.sendMessage(Main.prefix + p.getDisplayName() + ChatColor.GREEN + " has joined the Team!");
		}
	}
	
	public void removePlayer(Player p) {
		if(!members.contains(p)) {
			return;
		}
		members.remove(p);
		
		if(p.getName() == leader.getName()) {
			for (Player pl : members) {
				pl.sendMessage(Main.prefix + ChatColor.RED + "The Team leader left, therefore this team is disbanded.");
				TeamManager.getTeamManager().removeTeam(this);
			}
		}
		
		for (Player pl : members) {
			pl.sendMessage(Main.prefix + p.getDisplayName() + ChatColor.RED + " has left the Team!");
		}
	}
	
	public boolean contains(Player p) {
		for (Player pl : members) {
			if(pl.getName() == p.getName()) {
				return true;
			}
		}
		
		return false;
	}
	
	  public int getTeamMembers() {
		  return members.size();
	  }
	  public ArrayList<Player> getTeamMembersArray(){
		  return members;
	  }
}
