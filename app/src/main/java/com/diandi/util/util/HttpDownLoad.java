package com.diandi.util.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpDownLoad {

    public boolean downFile(String urlStr, String fileName) {
        InputStream inputStream = null;
        FileHelper fileUtils = new FileHelper();
        inputStream = getInputStreamFromUrl(urlStr);
        boolean resultFile = fileUtils.writeFile(fileName, inputStream);
        return resultFile;

    }

    public boolean downFile(String urlStr, String path, String fileName) {
        boolean resultFile = false;
        InputStream inputStream = null;
        FileHelper fileUtils = new FileHelper();
        File file = new File(Environment.getExternalStorageDirectory() + "/" + path, fileName);
        if (!file.exists()) {
            ///???有疑问
            Log.e("HttpDownLoad", "不存在");
            inputStream = getInputStreamFromUrl(urlStr);
            resultFile = fileUtils.writeFile(fileName, path, inputStream);
            return resultFile;
        } else {

            Log.e("HttpDownLoad", "存在");
        }
        return resultFile;

    }

    public InputStream getInputStreamFromUrl(String urlStr) {
        URL url = null;
        HttpURLConnection urlConn = null;
        InputStream inputStream = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            urlConn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream = urlConn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
