package me.zephirenz.noirguilds.config;

import me.zephirenz.noirguilds.NoirGuilds;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

import java.io.*;

public abstract class Config {

    // Config setup borrowed from mcMMO - Thanks!

    static final NoirGuilds plugin = NoirGuilds.inst();
    String file;
    File configFile;
    FileConfiguration config;


    public Config(String path, String file) {
        this.file = file;
        configFile = new File(plugin.getDataFolder(), path + File.separator + file);
        loadFile();

    }

    Config(String file) {
        this.file = file;
        configFile = new File(plugin.getDataFolder(), file);
        loadFile();
    }

    Config(File file) {
        this.file = file.getName();
        configFile = file;
        loadFile();
    }

    void loadFile() {
        if(!configFile.exists()) {
            createFile();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveFile() {
        try{
            config.save(configFile);
        }catch(IOException e) {
            plugin.getLogger().severe("Unable to write to file " + configFile.getPath());
        }
    }

    public void deleteFile() {
        if(!configFile.delete()) {
            plugin.getLogger().warning("config file " + configFile.getPath() + "could not be deleted!");
        }
    }

    public void renameFile(String newName) {

        File oldFile = configFile;
        File newFile = new File(configFile.getParent(), newName + ".yml");
        FileUtil.copy(oldFile, newFile);
        file = newFile.getName();
        if(!oldFile.delete()) {
            plugin.getLogger().warning("config file " + oldFile.getPath() + "could not be deleted!");
        }
        configFile = newFile;
        loadFile();

    }

    void createFile() {
        configFile.getParentFile().mkdirs();

        InputStream iStream = getResource();

        if(iStream == null) {
            plugin.getLogger().severe("File missing from jar: " + file);
            return;
        }

        OutputStream oStream = null;

        try {
            oStream = new FileOutputStream(configFile);

            int read;
            byte[] bytes = new byte[1024];

            while((read = iStream.read(bytes)) != -1) {
                oStream.write(bytes, 0, read);
            }

        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }finally{

            if(oStream != null) {
                try {
                    oStream.close();
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                iStream.close();
            }catch(IOException e) {
                e.printStackTrace();
            }

        }

    }

    InputStream getResource() {

        return plugin.getResource(file);

    }



}
