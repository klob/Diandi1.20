package com.diandi.util.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {

    private Context context;
    private String SDPATH;

    public FileHelper(Context context) {
        this.context = context;
    }

    public FileHelper() {
        SDPATH = Environment.getExternalStorageDirectory() + "/";

    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        Log.e("info", "Not writable");
        return false;
    }

    public File createDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdirs();
        return dir;
    }

    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    public boolean isFileExist(String path, String fileName) {
        File file = new File(SDPATH + path + "/" + fileName);
        return file.exists();
    }

    public boolean writeFile(String fileName, InputStream input) {
        String fileAddress = SDPATH + fileName;
        boolean flag = false;
        OutputStream output = null;
        File file = new File(fileAddress);
        if (isExternalStorageWritable() && file.exists()) {
            try {
                output = new FileOutputStream(file);
                byte[] buffer = new byte[4 * 1024];
                int length = 0;
                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null)
                    try {
                        output.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
        return flag;
    }

    public boolean writeFile(String fileName, String path, InputStream input) {
        createDir(path);
        String fileAddress = SDPATH + "/" + path;

        boolean flag = false;
        OutputStream output = null;
        File file = new File(fileAddress, fileName);
        if (isExternalStorageWritable() && !(file.exists())) {
            try {
                output = new FileOutputStream(file);
                byte[] buffer = new byte[4 * 1024];
                int length = 0;
                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null)
                    try {
                        output.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

        }

        return flag;
    }


    public boolean copyFile(String fromPath, String toPath, String filename) {
        InputStream in = null;
        boolean flag = false;
        in = inputStream(SDPATH + "/" + fromPath + "/" + filename);
        flag = writeFile(filename, toPath, in);
        return flag;
    }

    public InputStream inputStream(String fileName) {
        InputStream in = null;
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    public OutputStream outputStream(String fileName) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }

}
