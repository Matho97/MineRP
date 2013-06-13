package io.github.matho97.minerp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MineRP extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable(){
		
		loadJobsConfig();
		loadConfiguration();
		getCommand("minerp").setExecutor(new Commands(this));
		getCommand("kit").setExecutor(new Commands(this));
		getCommand("test").setExecutor(new Commands(this));
		getCommand("jobs").setExecutor(new Commands(this));
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info(getDataFolder().getName());
	}
	
	public void loadJobsConfig(){
		//Jobs
		getJobsConfig().addDefault("Jobs.Default", "Citizen");
		
		//Citizen
		getJobsConfig().addDefault("Jobs.Citizen.Prefix", "§6[§aCitizen§6]");
		getJobsConfig().addDefault("Jobs.Citizen.Description", "Basic member of society, low paycheck. This is also the default job for any player joining or demoted from position.");
		getJobsConfig().addDefault("Jobs.Citizen.Salary", 60);
		getJobsConfig().addDefault("Jobs.Citizen.Max", 0);
		getJobsConfig().addDefault("Jobs.Citizen.Current", 0);
		List<String> kitCitizen = Arrays.asList("0 1", "0 1");
		getJobsConfig().addDefault("Jobs.Citizen.Kit", kitCitizen);
		getJobsConfig().addDefault("Jobs.Citizen.Vote", false);
		
		//Mayor
		getJobsConfig().addDefault("Jobs.Mayor.Prefix", "§6[§9Mayor§6]");
		getJobsConfig().addDefault("Jobs.Mayor.Description", "Sets the city's laws and tax rates while running and maintaining the entire city.");
		getJobsConfig().addDefault("Jobs.Mayor.Salary", 200);
		getJobsConfig().addDefault("Jobs.Mayor.Max", 1);
		getJobsConfig().addDefault("Jobs.Mayor.Current", 0);
		List<String> kitMayor = Arrays.asList("267 1", "50 15");
		getJobsConfig().addDefault("Jobs.Mayor.Kit", kitMayor);
		getJobsConfig().addDefault("Jobs.Mayor.Vote", true);
		
		getJobsConfig().options().copyDefaults(true);
		saveJobsConfig();
	}
	
	public FileConfiguration getJobsConfig() {
	    if (JobsConfig == null) {
	        reloadJobsConfig();
	    }
	    return JobsConfig;
	}
	
	public void reloadJobsConfig() {
	    if (JobsConfigFile == null) {
	    JobsConfigFile = new File(getDataFolder(), "jobs.yml");
	    }
	    JobsConfig = YamlConfiguration.loadConfiguration(JobsConfigFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = getResource("jobs.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        JobsConfig.setDefaults(defConfig);
	    }
	}
	
	public void saveJobsConfig() {
		if (JobsConfig == null || JobsConfigFile == null) {
			return;
		}
		try {
			getJobsConfig().save(JobsConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save config to " + JobsConfigFile, ex);
		}
	}
	
	public void saveDefaultConfig() {
	    if (JobsConfigFile == null) {
	        JobsConfigFile = new File(getDataFolder(), "jobs.yml");
	    }
	    if (!configFile.exists()) {
	         saveResource("jobs.yml", false);
	     }
	}
	
	public void loadConfiguration(){
		getConfig().addDefault("join-message", "Welcome to MineRP");
		getConfig().addDefault("Users", "");
		/*//Jobs
		getConfig().addDefault("Jobs.Default", "Citizen");
		
		//Citizen
		getConfig().addDefault("Jobs.Citizen.Prefix", "§6[§aCitizen§6]");
		getConfig().addDefault("Jobs.Citizen.Description", "Basic member of society, low paycheck. This is also the default job for any player joining or demoted from position.");
		getConfig().addDefault("Jobs.Citizen.Salary", 60);
		getConfig().addDefault("Jobs.Citizen.Max", 0);
		getConfig().addDefault("Jobs.Citizen.Current", 0);
		List<String> kitCitizen = Arrays.asList("0 1", "0 1");
		getConfig().addDefault("Jobs.Citizen.Kit", kitCitizen);
		getConfig().addDefault("Jobs.Citizen.Vote", false);
		
		//Mayor
		getConfig().addDefault("Jobs.Mayor.Prefix", "§6[§9Mayor§6]");
		getConfig().addDefault("Jobs.Mayor.Description", "Sets the city's laws and tax rates while running and maintaining the entire city.");
		getConfig().addDefault("Jobs.Mayor.Salary", 200);
		getConfig().addDefault("Jobs.Mayor.Max", 1);
		getConfig().addDefault("Jobs.Mayor.Current", 0);
		List<String> kitMayor = Arrays.asList("267 1", "50 15");
		getConfig().addDefault("Jobs.Mayor.Kit", kitMayor);
		getConfig().addDefault("Jobs.Mayor.Vote", true);*/
		
		
	    getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		//if (getJobsConfig().getString("Users." + player.getName()) == null){
		String defaultJob = getJobsConfig().getString("Jobs.Default");
		setJob(player, player.getName(), defaultJob);
		//} else {
		
		String name = player.getName();
		String prefix = getPrefix(player, player.getName());
		player.setDisplayName(prefix + "§r " + name);
		//}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		
		List<String> kitslist = getJobsConfig().getStringList("Jobs.Mayor.Kit");
		for (String s : kitslist){
            //player.sendMessage(s.split(" ")[0]);
            int kit1 = Integer.parseInt(s.split(" ")[0]);
            int kit2 = Integer.parseInt(s.split(" ")[1]);
            player.getInventory().addItem(new ItemStack(kit1, kit2));
        }
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event){
		for(Player player : Bukkit.getOnlinePlayers()){
			if (getJobsConfig().getString("Users." + player.getName()) != null){
				Bukkit.broadcastMessage(player.getName());
				event.getDrops().clear();
			}
		}
		
		//String user = getJobsConfig().getString("Users");
		/*if (user != null){
			Player player = Bukkit.getPlayer(user);
			player.getInventory().clear();
			getJobsConfig().set("Users." + user, 2);
			saveConfig();
		}*/
	}
	
	public void setJob(Player sender, String player, String job){
		if (getJobsConfig().get("Jobs." + job) != null){
			//String prefix = getJobsConfig().getString("Jobs." + job + ".Prefix");
			//getJobsConfig().set("Users." + target + ".Prefix", prefix);
			
			getConfig().set("Users." + player + ".Job", job);
			saveConfig();
			
			String name = player;
			String prefix = getPrefix(sender, player);
			Player players = Bukkit.getPlayer(player);
			
			players.setDisplayName(prefix + "§r " + name);
		} else {
			if (sender instanceof Player){
			sender.sendMessage(minerp + "Job does not exist!");
			} else {
				getLogger().info("Job does not exist!");
			}
		}
		
	}
	
	public String getJob(String player){
		//for(Player player : Bukkit.getOnlinePlayers()){
		//}
		
		String job = getConfig().getString("Users." + player + ".Job");

		return job;
		
	}
	
	public void giveKit(Player player){
		String job =  getJob(player.getName());
		List<String> kitslist = getJobsConfig().getStringList("Jobs." + job + ".Kit");
        player.getInventory().clear();
		for (String s : kitslist){
			player.sendMessage(s.split(" ")[0]);
			player.sendMessage(s.split(" ")[1]);
            int kit1 = Integer.parseInt(s.split(" ")[0]);
            int kit2 = Integer.parseInt(s.split(" ")[1]);
            player.getInventory().addItem(new ItemStack(kit1, kit2));
            //sender.sendMessage(.toString());
        }
	}
	
	/**
	 * TODO Add more getter and setters
	 * TODO Implement getters and setters
	 * 
	 * @param sender
	 * @param target
	 * @param job
	 * @return 
	 */
	
	public String getPrefix(Player sender, String player){
		
		String job = getConfig().getString("Users." + player + ".Job");
		String prefix = getJobsConfig().getString("Jobs." + job + ".Prefix");
		//sender.sendMessage(prefix);
		return prefix;
	}
	
	@Override
	public void onDisable(){
		
	}
	
	private FileConfiguration JobsConfig = null;
	private File configFile = null;
	private File JobsConfigFile = null;
	
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
	
	public String minerp = gold + "[" + blue + "MineRP" + gold + "] " + white;
}
