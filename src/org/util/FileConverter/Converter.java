package org.util.FileConverter;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import me.Aubli.IPlayHub.IPlayHub;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class Converter {
    
    public static enum FileType {
	world_config(IPlayHub.getHub().getWorldFile()),
	plugin_config(new File(IPlayHub.getHub().getDataFolder(), "config.yml"));
	
	private File file;
	
	private FileType(File file) {
	    this.file = file;
	}
	
	public File getFile() {
	    return this.file;
	}
    }
    
    public boolean convertPaths(FileType type, String newVersion, boolean force, Map<String, String> changeMap) {
	
	if (!needsConvertion(type, newVersion) && !force) {
	    return false;
	}
	
	switch (type) {
	
	    case world_config:
		Map<String, Object> convertedMap = new LinkedHashMap<String, Object>();
		
		for (String worldName : getSavedWorlds()) {
		    for (Entry<String, Object> origin : getWorldContent(worldName).entrySet()) {
			for (Entry<String, String> change : changeMap.entrySet()) {
			    
			    if (!(origin.getValue() instanceof MemorySection)) {
				if (change.getKey().equals(origin.getKey())) {
				    convertedMap.put("worlds." + worldName + "." + change.getValue(), origin.getValue());
				    IPlayHub.getPluginLogger().log(getClass(), Level.FINEST, "Convert: " + origin.getKey() + " moved to " + change.getValue() + " in " + worldName, true, true);
				    break;
				} else {
				    if (!changeMap.containsKey(origin.getKey())) {
					convertedMap.put("worlds." + worldName + "." + origin.getKey(), origin.getValue());
					IPlayHub.getPluginLogger().log(getClass(), Level.FINEST, "Include: " + origin.getKey() + ":" + origin.getValue() + " from " + worldName, true, true);
				    }
				}
			    }
			}
		    }
		}
		
		convertedMap.put("fileVersion", newVersion);
		
		try {
		    saveContent(type, convertedMap);
		} catch (Exception e) {
		    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Failed saving world-config.yml!", true, false, e);
		}
		return true;
		
	    case plugin_config:
		break;
	}
	
	return false;
    }
    
    public boolean addValues(FileType type, String newVersion, boolean force, Map<String, Object> valueMap) {
	
	if (!needsConvertion(type, newVersion) && !force) {
	    return false;
	}
	
	switch (type) {
	
	    case world_config:
		Map<String, Object> updatedMap = new LinkedHashMap<String, Object>();
		
		for (String worldName : getSavedWorlds()) {
		    for (Entry<String, Object> origin : getWorldContent(worldName).entrySet()) {
			
			if (!(origin.getValue() instanceof MemorySection)) {
			    updatedMap.put("worlds." + worldName + "." + origin.getKey(), origin.getValue());
			    IPlayHub.getPluginLogger().log(getClass(), Level.FINEST, "Include: " + origin.getKey() + " : " + origin.getValue() + " from " + worldName, true, true);
			}
		    }
		    
		    for (Entry<String, Object> value : valueMap.entrySet()) {
			if (!updatedMap.containsKey("worlds." + worldName + "." + value.getKey())) {
			    updatedMap.put("worlds." + worldName + "." + value.getKey(), value.getValue());
			    IPlayHub.getPluginLogger().log(getClass(), Level.FINEST, "Insert: " + value.getKey() + " : " + value.getValue() + " into " + worldName, true, true);
			}
		    }
		}
		
		updatedMap.put("fileVersion", newVersion);
		
		try {
		    saveContent(type, updatedMap);
		} catch (Exception e) {
		    IPlayHub.getPluginLogger().log(getClass(), Level.WARNING, "Failed saving world-config.yml!", true, false, e);
		}
		return true;
		
	    case plugin_config:
		break;
	}
	
	return false;
    }
    
    public boolean needsConvertion(FileType type, String requiredVersion) {
	
	double checkVersion = parseVersion(requiredVersion);
	double fileVersion = getFileVersion(type);
	
	if (checkVersion > fileVersion) {
	    return true;
	}
	return false;
    }
    
    private Set<String> getSavedWorlds() {
	FileConfiguration conf = YamlConfiguration.loadConfiguration(FileType.world_config.getFile());
	return conf.getConfigurationSection("worlds").getKeys(false);
    }
    
    private Map<String, Object> getWorldContent(String worldName) {
	FileConfiguration conf = YamlConfiguration.loadConfiguration(FileType.world_config.getFile());
	return conf.getConfigurationSection("worlds." + worldName).getValues(true);
    }
    
    private Map<String, Object> getFileContent(FileType type) {
	FileConfiguration conf = YamlConfiguration.loadConfiguration(type.getFile());
	return conf.getValues(true);
    }
    
    private void saveContent(FileType type, Map<String, Object> content) throws Exception {
	
	type.getFile().delete();
	type.getFile().createNewFile();
	
	FileConfiguration config = YamlConfiguration.loadConfiguration(type.getFile());
	
	for (Entry<String, Object> entry : content.entrySet()) {
	    config.set(entry.getKey(), entry.getValue());
	}
	
	config.save(type.getFile());
    }
    
    private double getFileVersion(FileType type) {
	FileConfiguration conf = YamlConfiguration.loadConfiguration(type.getFile());
	
	if (conf.get("fileVersion") != null) {
	    return parseVersion(conf.getString("fileVersion"));
	}
	return -1;
    }
    
    private double parseVersion(String versionString) throws NumberFormatException {
	// Possibilities: IPlayHub v0.0.20-1_DEV
	if (versionString.contains("v")) {
	    return parseVersion(versionString.split("v")[1]);
	} else if (versionString.contains("_")) {
	    return parseVersion(versionString.split("_")[0]);
	} else {
	    return Double.parseDouble(versionString.replace(".", "").replace("-", "."));
	}
    }
}
