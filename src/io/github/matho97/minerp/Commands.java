package io.github.matho97.minerp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{
	
	public Commands(MineRP plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		Player player = (Player)sender;
		
		if(cmd.getName().equalsIgnoreCase("minerp")){
			
			plugin.reloadConfig();
			sender.sendMessage(minerp + "Succelsful reload is succesful!");
			
			return true;
		} else if (cmd.getName().equalsIgnoreCase("kit")){
			//int item = plugin.getJobsConfig().getInt("");
			
			plugin.giveKit(player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("test")){
			if(args[0].equalsIgnoreCase("prefix")){
				
				plugin.getPrefix(player, args[1]);
				return true;
				
			} else if (args[0].equalsIgnoreCase(null)){
				String defaultJob = plugin.getJobsConfig().getString("Jobs.Default");
				sender.sendMessage(defaultJob);
				return true;
			}
		} else if(cmd.getName().equalsIgnoreCase("jobs")){

			String job = args[0];
			int maxUsers = plugin.getJobsConfig().getInt("Jobs." + job + ".Max");
			int currentUsers = plugin.getJobsConfig().getInt("Jobs." + job + ".Current");
			String currentJob = plugin.getJob(player.getName());
			//sender.sendMessage(currentJob);
			int existingUsers = plugin.getJobsConfig().getInt("Jobs." + currentJob + ".Current");
			
			if (!(currentJob.equalsIgnoreCase(job))){
				if (maxUsers == 0){
					plugin.getJobsConfig().set("Jobs." + job + ".Current", currentUsers + 1);
					
					plugin.getJobsConfig().set("Jobs." + currentJob + ".Current", existingUsers -1);
					
					plugin.saveConfig();
					
					plugin.setJob(player, player.getName(), job);
					sender.sendMessage(plugin.getJob(player.getName()));
					plugin.giveKit(player);
					return true;
				}
				if (currentUsers != maxUsers){
					plugin.getJobsConfig().set("Jobs." + job + ".Current", currentUsers + 1);

					plugin.getJobsConfig().set("Jobs." + currentJob + ".Current", existingUsers -1);
					
					sender.sendMessage("heello");
					plugin.setJob(player, player.getName(), job);
					plugin.giveKit(player);
					return true;
				} else {
					sender.sendMessage("The job " + job + "is fully occupied");
				}
			} else {
				player.sendMessage("You're already " + job);
				return true;
			}
			return true;
		}
		
		return false;
	}
	
	// Easier chat coloring during string broadcasts and such. Seeing as we do it so much in here ;)
	public ChatColor
	aqua = ChatColor.AQUA,
	black = ChatColor.BLACK,
	blue = ChatColor.BLUE,
	bold = ChatColor.BOLD,
	darkaqua = ChatColor.DARK_AQUA,
	darkblue = ChatColor.DARK_BLUE,
	darkgray = ChatColor.DARK_GRAY,
	darkgreen = ChatColor.DARK_GREEN,
	darkpurple = ChatColor.DARK_PURPLE,
	darkred = ChatColor.DARK_RED,
	gold = ChatColor.GOLD,
	gray = ChatColor.GRAY,
	green = ChatColor.GREEN,
	italic = ChatColor.ITALIC,
	magic = ChatColor.MAGIC,
	purple = ChatColor.LIGHT_PURPLE,
	red = ChatColor.RED,
	reset = ChatColor.RESET,
	strike = ChatColor.STRIKETHROUGH,
	underline = ChatColor.UNDERLINE,
	white = ChatColor.WHITE,
	yellow = ChatColor.YELLOW
	;
	
	private MineRP plugin;
	
	public String minerp = gold + "[" + blue + "MineRP" + gold + "] " + white;
	

}
