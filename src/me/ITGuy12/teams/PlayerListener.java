package me.ITGuy12.teams;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerListener implements Listener{

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent e) {
		if((!(e.getDamager() instanceof Player)) && (!(e.getEntity() instanceof Player))) {
			Player damager = (Player) e.getDamager();
			Player damaged = (Player) e.getEntity();
			
			if(TeamManager.getTeamManager().isInATeam(damaged) && TeamManager.getTeamManager().isInATeam(damager)) {
				if(TeamManager.getTeamManager().getTeamForMember(damaged) == TeamManager.getTeamManager().getTeamForMember(damager)) {
					e.setCancelled(true);
				}
			}
		}
	}
}
