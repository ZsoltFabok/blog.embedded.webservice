package com.zsoltfabok.blog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;

public class JarArchive {
    private ByteArrayOutputStream content;
    private ZipOutputStream stream;
    private File tempRoot;

    public JarArchive() {
        content = new ByteArrayOutputStream();
        stream = new ZipOutputStream(content);
    }

    public void addDirectory(File file) {
        if (tempRoot == null) {
            tempRoot = file;
        }
        if (file.isDirectory()) {
            for (File entry : file.listFiles()) {
                String path = getRelativePath(entry);
                if (entry.isDirectory()) {
                    createEntryInArchive(path.endsWith("/") ? path : path + "/");
                    addDirectory(entry);
                } else {
                    createEntryInArchive(path);
                    copyContentToArchive(entry);
                }
            }
        }
        if (tempRoot == file) {
            tempRoot = null;
        }
    }

    private String getRelativePath(File entry) {
        String name = entry.getAbsolutePath().replace(
                tempRoot.getAbsolutePath()
                        + System.getProperty("file.separator"), "");
        name = FilenameUtils.normalize(name, true);
        return name;
    }

    public File toFile(String filename) {
        File file = new File(filename);
        try {
            createEntryInArchive("MANIFEST");
            createEntryInArchive("MANIFEST/MANIFEST.MF");

            stream.close();
            copy(new ByteArrayInputStream(content.toByteArray()), new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private void copyContentToArchive(File file) {
        try {
            copy(new FileInputStream(file), stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createEntryInArchive(String name) {
        try {
            stream.putNextEntry(new ZipEntry(name));
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

}
