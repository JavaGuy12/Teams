package me.ITGuy12.teams;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main plugin;
	int maxPerTeam;
	public static String prefix;
  //HashMap Invited Inviter
	HashMap<Player, Player> teamInvites = new HashMap<Player, Player>();
	
	public void onEnable() {
		Bukkit.getLogger().info(ChatColor.YELLOW + "Teams by ITGuy12 enabled. Dedicated to: beanii on the Bukkit Forums");
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		maxPerTeam = getConfig().getInt("teams.max-per-team");
		prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("teams.prefix"));
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
		if(!(s instanceof Player)) {
			s.sendMessage(prefix + ChatColor.RED + "Only players can team up, silly!");
			return true;
		}
		
		Player p = (Player) s;
		
		if(cmd.getName().equalsIgnoreCase("teams")) {
			if(args.length == 0) {
				p.sendMessage(prefix + ChatColor.GREEN + "Version 1.0.0 by ITGuy12");
				p.sendMessage(prefix + ChatColor.AQUA + "Run " + ChatColor.GOLD + "/teams help " + ChatColor.AQUA + "for help!");
				return true;
			}
			
			//No args besides sub-command required
			if(args[0].equalsIgnoreCase("help")) {
				p.sendMessage(prefix + ChatColor.GREEN + "Version 1.0.0 by ITGuy12");
				p.sendMessage(ChatColor.GREEN + "/teams help - Show this message.");
				p.sendMessage(ChatColor.GREEN + "/teams invite <player> - Invite a player to a team (Create one if you don't have one already.)");
				p.sendMessage(ChatColor.GREEN + "/teams kick <player> - Kick a player from your team. (Team leader must run)");
				p.sendMessage(ChatColor.GREEN + "/teams leave - Leave your current team.");
				p.sendMessage(ChatColor.GREEN + "/teams accept <player> - Accept a team request.");
				p.sendMessage(ChatColor.GREEN + "/teams deny <player> - Deny a team request.");
				p.sendMessage(ChatColor.GREEN + "/teams list - List your current team.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("leave")) {
				TeamManager.getTeamManager().getTeamForMember(p).removePlayer(p);
				p.sendMessage(prefix + ChatColor.GREEN + "You left your current team.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("list")) {
				if(TeamManager.getTeamManager().isInATeam(p)) {
					for(Player pl : TeamManager.getTeamManager().getTeamForMember(p).getTeamMembersArray()) {
						p.sendMessage(prefix + ChatColor.GOLD + pl.getDisplayName());
					}
				}
				return true;
			}
			
			//<player> arg required
			if(args[0].equalsIgnoreCase("invite")) {
				if(args.length == 1) {
					p.sendMessage(prefix + ChatColor.RED + "ERROR: No player specified. Usage: /teams invite <Player>");
					return true;
				}
				//Check if player is real
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					p.sendMessage(prefix + ChatColor.RED + "ERROR: " + args[1] + " is not online or does not exist.");
					return true;
				}
				
				if(teamInvites.containsValue(p)) {
						p.sendMessage(prefix + ChatColor.RED + "ERROR: You may not have more than one team request at a time.");
						return true;
				}
				
				if(TeamManager.getTeamManager().isInATeam(p)) {
					if(TeamManager.getTeamManager().getTeamForMember(p).getLeader().getName() == p.getName()) {
						//They are the leader! Working with a current team.
						if(TeamManager.getTeamManager().getTeam(p).getTeamMembers() == maxPerTeam) {
							p.sendMessage(prefix + ChatColor.RED + "ERROR: You may not have more than " + maxPerTeam + " team members at a time.");
							return true;
						}
						teamInvites.put(target, p);
						target.sendMessage(prefix + ChatColor.AQUA + p.getDisplayName() + " has invited you to a team. Run " + ChatColor.GREEN + "/teams accept " + p.getName() + ChatColor.AQUA + " to accept or " + ChatColor.GREEN + "/teams deny " + p.getName() + ChatColor.AQUA + " to deny.");
						p.sendMessage(prefix + ChatColor.GREEN + "Invitation sent.");
					}else {
						p.sendMessage(prefix + ChatColor.RED + "ERROR: You must be a team leader or not in a team to invite players.");
						return true;
					}
				}
				
				TeamManager.getTeamManager().createTeam(p);
				teamInvites.put(target, p);
				target.sendMessage(prefix + ChatColor.AQUA + p.getDisplayName() + " has invited you to a team. Run " + ChatColor.GREEN + "/teams accept " + p.getName() + ChatColor.AQUA + " to accept or " + ChatColor.GREEN + "/teams deny " + p.getName() + ChatColor.AQUA + " to deny.");
				p.sendMessage(prefix + ChatColor.GREEN + "Invitation sent.");
			}
			
			if(args[0].equalsIgnoreCase("accept")) {
				if(args.length == 1) {
					p.sendMessage(prefix + ChatColor.RED + "ERROR: No player specified. Usage: /teams accept <Player>");
					return true;
				}
				//You know the drill.		
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					p.sendMessage(prefix + ChatColor.RED + "ERROR: " + args[1] + " is not online or does not exist.");
					return true;
				}
				
				if(teamInvites.containsKey(p)) {
					if(teamInvites.get(p).getName() == target.getName()) {
						teamInvites.remove(p);
						TeamManager.getTeamManager().getTeam(target).addPlayer(p);
						return true;
					}
				}else {	
			    p.sendMessage(prefix + ChatColor.RED + "ERROR: you do not have any invites.");
				return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("deny")) {
				if(args.length == 1) {
					p.sendMessage(prefix + ChatColor.RED + "ERROR: No player specified. Usage: /teams accept <Player>");
					return true;
				}
				//You know the drill.		
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					p.sendMessage(prefix + ChatColor.RED + "ERROR: " + args[1] + " is not online or does not exist.");
					return true;
				}
				
				if(teamInvites.containsKey(p)) {
					if(teamInvites.get(p).getName() == target.getName()) {
						teamInvites.remove(p);
						target.sendMessage(prefix + ChatColor.RED + p.getDisplayName() + " denied the request.");
						return true;
					}
				}else {	
			    p.sendMessage(prefix + ChatColor.RED + "ERROR: you do not have any invites.");
				return true;
				}
			}
			
		}
		return false;
	}
}
