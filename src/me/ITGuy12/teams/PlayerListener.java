package me.ITGuy12.teams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener{

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e) {
		if((!(e.getDamager() instanceof Player)) && (!(e.getEntity() instanceof Player))) {
			Player damager = (Player) e.getDamager();
			Player damaged = (Player) e.getEntity();
			
			if(TeamManager.getTeamManager().isInATeam(damaged) && TeamManager.getTeamManager().isInATeam(damager)) {
				if(TeamManager.getTeamManager().getTeamForMember(damaged) == TeamManager.getTeamManager().getTeamForMember(damager)) {
					e.setCancelled(true);
					damager.sendMessage(Main.prefix + ChatColor.RED + "ERROR: You cannot attack teammates!");
				}
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(TeamManager.getTeamManager().isInATeam(e.getPlayer())) {
			if(TeamManager.getTeamManager().getTeamForMember(e.getPlayer()).getLeader().getName() == e.getPlayer().getName()){
				TeamManager.getTeamManager().removeTeam(TeamManager.getTeamManager().getTeamForMember(e.getPlayer()));
				for (Player p : TeamManager.getTeamManager().getTeamForMember(e.getPlayer()).getTeamMembersArray()) {
					p.sendMessage(Main.prefix + ChatColor.RED + e.getPlayer().getDisplayName() + ChatColor.RED + " has left, and therefore your team has been disbanded.");
				}
				return;
			}
			
			Team t = TeamManager.getTeamManager().getTeamForMember(e.getPlayer());
			
			t.removePlayer(e.getPlayer());
			
		}
	}
	
	public void onPlayerKicked(PlayerKickEvent e) {
		if(TeamManager.getTeamManager().isInATeam(e.getPlayer())) {
			if(TeamManager.getTeamManager().getTeamForMember(e.getPlayer()).getLeader().getName() == e.getPlayer().getName()){
				TeamManager.getTeamManager().removeTeam(TeamManager.getTeamManager().getTeamForMember(e.getPlayer()));
				for (Player p : TeamManager.getTeamManager().getTeamForMember(e.getPlayer()).getTeamMembersArray()) {
					p.sendMessage(Main.prefix + ChatColor.RED + e.getPlayer().getDisplayName() + ChatColor.RED + " has left, and therefore your team has been disbanded.");
				}
				return;
			}
			
			Team t = TeamManager.getTeamManager().getTeamForMember(e.getPlayer());
			
			t.removePlayer(e.getPlayer());
			
		}
	}
}
