/*
 * NatureSounds plugin - Adds ambient sounds to Minecraft.
 * Copyright (C) 2018 Floris Jolink (TheBlockBender / JustDJplease) - All Rights Reserved
 *
 * You are allowed to:
 * - Modify this code, and use it for personal projects. (Private servers, small networks)
 * - Take ideas and / or formats of this plugin and use it for personal projects. (Private servers, small networks)
 *
 * You are NOT allowed to:
 * - Resell the original plugin or a modification of it.
 * - Claim this plugin as your own.
 * - Distribute the source-code or a modification of it without prior consent of the original author.
 *
 */

package me.theblockbender.nature.sounds.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.Sound;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;

public class UtilResourcePack {
    // -------------------------------------------- //
    // INSTANCES & VARIABLES
    // -------------------------------------------- //
    private NatureSounds main;

    // -------------------------------------------- //
    // CONSTRUCTOR
    // -------------------------------------------- //
    public UtilResourcePack(NatureSounds main) {
        this.main = main;
    }

    // -------------------------------------------- //
    // MAIN METHOD
    // -------------------------------------------- //
    public void addAllFilesToPack() {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            long timeStart = System.currentTimeMillis();
            main.debug("| Generating Resource Pack:");
            deleteOldFolders();
            main.debug("| - Cleaned up old folders");
            createPackBase();
            main.debug("| - Created template");
            int soundsAdded = 0;
            for (Sound sound : main.getSounds()) {
                soundsAdded = soundsAdded + addFilesToPack(sound);
            }
            main.debug("| - Added " + soundsAdded + " sounds to template");
            try {
                zipFolder();
                main.debug("| - Zipped folder");
            } catch (Exception e) {
                main.outputError("Unable to zip the resource pack!");
                e.printStackTrace();
            }
            deleteDir(main.utilWebServer.getUnzippedFileLocation());
            main.debug("| - Cleaned up old folders");
            if (UtilZip.isValid(new File(main.utilWebServer.getFileLocation()))) {
                main.debug("| Checked pack: valid");
            } else {
                main.debug("| Checked pack: invalid");
            }
            Long timeTaken = System.currentTimeMillis() - timeStart;
            main.debug("| Time taken: " + timeTaken + " ms.");
        });
    }

    // -------------------------------------------- //
    // SUB-METHODS
    // -------------------------------------------- //
    private void deleteOldFolders() {
        deleteDir(main.utilWebServer.getUnzippedFileLocation());
        deleteZip(main.utilWebServer.getFileLocation());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteZip(String path) {
        File file = new File(path);
        if (!file.exists()) return;
        file.delete();
    }

    private void deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) return;
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            main.outputError("Could not delete folder " + path);
            e.printStackTrace();
        }
    }

    private void zipFolder() {
        try {
            UtilZip.zipFolder(main.utilWebServer.getUnzippedFileLocation(), main.utilWebServer.getFileLocation());
        } catch (Exception e) {
            main.outputError("Failed to zip files!");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createPackBase() {
        File templateZip = new File(main.utilWebServer.getWebDirectory(), "template.zip");
        if (!templateZip.exists()) {
            templateZip.getParentFile().mkdirs();
            try {
                OutputStream out = new FileOutputStream(templateZip);
                InputStream in = main.getResource("template_pack.zip");
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } catch (IOException ex) {
                main.outputError("Could not save template pack!");
                ex.printStackTrace();
            }
        }
        try {
            UtilZip.unzip(templateZip, main.utilWebServer.getUnzippedFileLocation());
        } catch (IOException e) {
            main.outputError("Unable to unzip template pack!");
            e.printStackTrace();
        }
        templateZip.delete();
    }

    private int addFilesToPack(Sound sound) {
        int soundsAdded = 0;
        for (String soundName : sound.getSoundNames()) {
            File file = new File(main.getDataFolder() + File.separator + "sounds" + File.separator + soundName + ".ogg");
            if (!file.exists()) {
                main.outputError("Could not find associated " + file.getName() + " sound file!");
            } else {
                addSoundToJson(soundName, sound.getSubtitle());
                copySoundIntoPack(file);
                soundsAdded++;
            }
        }
        return soundsAdded;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void copySoundIntoPack(File file) {
        if (!file.exists()) return;
        File packLocation = new File(main.utilWebServer.getUnzippedFileLocation() + File.separator + "assets" + File.separator + "minecraft" + File.separator + "sounds", file.getName());
        if (!packLocation.exists()) {
            packLocation.getParentFile().mkdirs();
            try {
                OutputStream out = new FileOutputStream(packLocation);
                InputStream in = new FileInputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } catch (IOException ex) {
                main.outputError("Unable to save " + file.getName() + " into the resource pack!");
                ex.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addSoundToJson(String soundName, String subtitle) {
        File soundsJSON = new File(main.utilWebServer.getUnzippedFileLocation() + File.separator + "assets" + File.separator + "minecraft" + File.separator, "sounds.json");
        if (!soundsJSON.exists()) {
            main.outputError("File sounds.json did not exist in template pack!");
            return;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(soundsJSON);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String rawJson = null;
        try {
            assert is != null;
            rawJson = IOUtils.toString(is, "UTF-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONObject file = gson.fromJson(rawJson, JSONObject.class);
        JSONObject soundProperties = new JSONObject();
        soundProperties.put("name", soundName);
        soundProperties.put("stream", true);
        JSONArray array = new JSONArray();
        array.add(soundProperties);
        JSONObject soundGroup = new JSONObject();
        soundGroup.put("sounds", array);
        soundGroup.put("subtitle", subtitle);
        file.put(soundName, soundGroup);
        writeJsonFile(soundsJSON, file.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void writeJsonFile(File file, String json) {
        BufferedWriter bufferedWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
        } catch (IOException e) {
            main.outputError("Failed to save sounds.json!");
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
