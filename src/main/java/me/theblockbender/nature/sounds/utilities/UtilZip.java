package me.theblockbender.nature.sounds.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class UtilZip {

    // V.2
    static void zipFolder(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        OutputStream outputStream = Files.newOutputStream(p);
        try (ZipOutputStream zs = new ZipOutputStream(outputStream)) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        outputStream.close();
    }

    // V.1
//    static void zipFolder(String srcFolder, String destZipFile) throws Exception {
//        ZipOutputStream zip;
//        FileOutputStream fileWriter;
//
//        fileWriter = new FileOutputStream(destZipFile);
//        zip = new ZipOutputStream(fileWriter);
//
//        File packFolder = new File(srcFolder);
//        for (File file : Objects.requireNonNull(packFolder.listFiles())) {
//            if (file.isDirectory()) {
//                addFolderToZip("", file.getPath(), zip);
//            }else{
//                addFileToZip("", file.getPath(), zip);
//            }
//        }
//        zip.flush();
//        zip.close();
//        fileWriter.close();
//    }
//
//    private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
//            throws Exception {
//        File folder = new File(srcFolder);
//
//        for (String fileName : Objects.requireNonNull(folder.list())) {
//            if (path.equals("")) {
//                addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
//            } else {
//                addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
//            }
//        }
//    }
//
//    private static void addFileToZip(String path, String srcFile, ZipOutputStream zip)
//            throws Exception {
//
//        File folder = new File(srcFile);
//        if (folder.isDirectory()) {
//            addFolderToZip(path, srcFile, zip);
//        } else {
//            byte[] buf = new byte[1024];
//            int len;
//            FileInputStream in = new FileInputStream(srcFile);
//            zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
//            while ((len = in.read(buf)) > 0) {
//                zip.write(buf, 0, len);
//            }
//            in.close();
//        }
//    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void unzip(File source, String out) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                File file = new File(out, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] buffer = new byte[Math.toIntExact(entry.getSize())];
                        int location;
                        while ((location = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, location);
                        }
                    }
                }
                entry = zis.getNextEntry();
            }
        }
    }
}
