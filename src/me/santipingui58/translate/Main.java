package me.santipingui58.translate;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Plugin pl;

	
	public static Plugin get() {
	    return pl;
	  }	
	
	public Configuration config;

	@Override
	public void onEnable() {
		pl = this;

		config = new Configuration("config.yml",this);
		
		
		ConfigurationSection sec = config.getConfig().getConfigurationSection("codes");
		for(String key : sec.getKeys(false))  TranslateAPI.getAPI().urlList.add(config.getConfig().getString("codes."+key));
	}

	
}
