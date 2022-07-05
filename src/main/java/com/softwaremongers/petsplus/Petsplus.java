package com.softwaremongers.petsplus;

import org.bukkit.plugin.java.JavaPlugin;
import com.softwaremongers.petsplus.utils.UpdateChecker;

import java.util.logging.Logger;

public final class Petsplus extends JavaPlugin {
    static final int RESOURCE_ID = 123;
    static final String RESOURCE_NAME = "Pets+";
    private final Logger logger = this.getLogger();

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Check For Updates
        new UpdateChecker(this, RESOURCE_ID).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("New version of "+ RESOURCE_NAME +" is available! New version: " + version + ", Installed version: " + this.getDescription().getVersion());
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
