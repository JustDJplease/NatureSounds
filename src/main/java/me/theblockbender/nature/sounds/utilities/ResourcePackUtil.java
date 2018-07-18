package me.theblockbender.nature.sounds.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.theblockbender.nature.sounds.NatureSounds;
import me.theblockbender.nature.sounds.Sound;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.channels.FileLock;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
public class ResourcePackUtil {
    private NatureSounds main;

    public ResourcePackUtil(NatureSounds main) {
        this.main = main;
    }

    public void addAllFilesToPack() {
        long timeStart = System.currentTimeMillis();
        main.debug("Task started: Generating the resource pack");
        deleteOldFolders();
        main.debug("[DONE] Old folder deleted");
        createPackBase();
        main.debug("[DONE] Pack template created in /web/rp");
        main.debug("Adding all loaded sounds to the resource pack...");
        for (Sound sound : main.getSounds()) {
            main.debug("Adding sound " + sound.getFileName() + "...");
            addFileToPack(sound);
        }
        main.debug("[DONE] All sounds are added to /web/rp");
        try {
            zipFolder();
        } catch (Exception e) {
            main.outputError("Unable to zip the resource pack!");
            e.printStackTrace();
        }
        main.debug("[DONE] Zipping completed!");
        main.debug("Deleting /web/rp folder...");
        deleteDir(main.webServerHandler.getUnzippedFileLocation());
        main.debug("[DONE] RESOURCE PACK SAVED!");
        Long timeTaken = System.currentTimeMillis() - timeStart;
        main.debug("(took " + timeTaken + " ms!)");
    }

    private void deleteOldFolders() {
        main.debug("Deleting old /web/rp folder...");
        deleteDir(main.webServerHandler.getUnzippedFileLocation());
        main.debug("Deleting old /web/rp.zip folder...");
        deleteZip(main.webServerHandler.getFileLocation());
    }

    private void deleteZip(String path) {
        File file = new File(path);
        if (!file.exists()) {
            main.debug("Folder did not exist, skipping...");
            return;
        }
        file.delete();
        main.debug("Folder deleted");
    }

    private void deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            main.debug("Folder did not exist, skipping...");
            return;
        }
        try {
            FileUtils.forceDelete(file);
            main.debug("Folder deleted");
        } catch (IOException e) {
            main.debug("Could not delete folder");
            e.printStackTrace();
        }
    }

    private void zipFolder() {
        main.debug("Zipping /web/rp to /web/rp.zip ...");
        try {
            ZipDirectoryUtil.zipFolder(main.webServerHandler.getUnzippedFileLocation(), main.webServerHandler.getFileLocation());
            main.debug("Zipped!");
        } catch (Exception e) {
            main.debug("Failed to zip file!");
            e.printStackTrace();
        }
    }

    private void createPackBase() {
        main.debug("Creating new /web/template.zip from /resource/template_pack.zip ...");
        File templateZip = new File(main.webServerHandler.getWebDirectory(), "template.zip");
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
                main.debug("/web/template.zip created");
            } catch (IOException ex) {
                main.debug("Could not save /web/template.zip!");
                ex.printStackTrace();
            }
        } else {
            main.debug("Folder already contained a template.zip, skipping...");
        }
        try {
            main.debug("Unzipping /web/template.zip to /web/rp ...");
            ZipDirectoryUtil.unzip(templateZip, main.webServerHandler.getUnzippedFileLocation());
            main.debug("Unzipped!");
        } catch (IOException e) {
            main.outputError("Unable to unzip template resource pack!");
            e.printStackTrace();
        }
        main.debug("Deleting /web/template.zip ...");
        templateZip.delete();
        main.debug("File deleted");
    }

    private void addFileToPack(Sound sound) {
        File file = new File(main.getDataFolder() + File.separator + "sounds" + File.separator + sound.getSoundName() + ".ogg");
        if (!file.exists()) {
            main.debug("Could not find associated " + file.getName() + " file!");
            return;
        }
        addSoundToJson(sound);
        copySoundIntoPack(file);
    }

    private void copySoundIntoPack(File file) {
        main.debug(" - copying sound into the pack ...");
        if (!file.exists()) {
            main.debug("Sound does not exist!");
            return;
        }
        File packLocation = new File(main.webServerHandler.getUnzippedFileLocation() + File.separator + "assets" + File.separator + "minecraft" + File.separator + "sounds", file.getName());
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
                main.debug(" - copied sound to the pack.");
            } catch (IOException ex) {
                main.outputError("Unable to save " + file.getName() + " into the resource pack!");
                ex.printStackTrace();
            }
        } else {
            main.debug(" - pack already contained this sound file.");
        }
    }

    private void addSoundToJson(Sound sound) {
        main.debug(" - adding sound to sounds.json ...");
        File soundsJSON = new File(main.webServerHandler.getUnzippedFileLocation() + File.separator + "assets" + File.separator + "minecraft" + File.separator, "sounds.json");
        if (!soundsJSON.exists()) {
            main.debug("sounds.json did not exist!");
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
        soundProperties.put("name", sound.getSoundName());
        soundProperties.put("stream", true);
        JSONArray array = new JSONArray();
        array.add(soundProperties);
        JSONObject soundGroup = new JSONObject();
        soundGroup.put("sounds", array);
        file.put(sound.getSoundName(), soundGroup);
        main.debug(" - added sound to json file.");
        main.debug(" - saving json file ...");
        writeJsonFile(soundsJSON, file.toString());
    }

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
            main.debug("failed to save sounds.json!");
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                    main.debug(" - sounds.json saved.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
