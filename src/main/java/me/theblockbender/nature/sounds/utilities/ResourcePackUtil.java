package me.theblockbender.nature.sounds.utilities;

import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.Sound;

import java.io.File;

public class ResourcePackUtil {
    private NatureSounds main;

    public ResourcePackUtil(NatureSounds main) {
        this.main = main;
    }

    public void addAllFilesToPack() {
        createPackBase();
        for (Sound sound : main.getSounds()) {
            addFileToPack(sound);
        }
        zipFolder();
        deleteFolder();
    }

    private void deleteFolder() {
        // TODO deletes original folder.
    }

    private void zipFolder() {
        // TODO zips the folder into plugins/NatureSounds/web/rp.zip
    }

    private void createPackBase() {
        // TODO creates basic folders & meta etc.
    }

    private void addFileToPack(Sound sound) {
        File file = new File(main.getDataFolder() + File.separator + "sounds" + sound.getSoundName() + ".ogg");
        if (!file.exists()) {
            main.outputError("Sound file " + file.getName() + "cannot be found.");
            return;
        }
        addSoundToJson(sound);
        copySoundIntoPack(file);
    }

    private void copySoundIntoPack(File file) {
        // TODO copies sound from plugins/NatureSounds/sounds/sound.ogg to plugins/NatureSounds/web/rp/.../sound.ogg
    }

    private void addSoundToJson(Sound sound) {
        // TODO adds the sound to the sounds.json file in plugins/NatureSounds/web/rp/.../
    }
}
