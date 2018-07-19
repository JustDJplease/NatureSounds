package me.theblockbender.nature.sounds.listeners;

import me.theblockbender.nature.sounds.utilities.UtilToken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    // -------------------------------------------- //
    // EVENT
    // -------------------------------------------- //
    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        UtilToken.removeToken(event.getPlayer().getUniqueId());
    }
}
