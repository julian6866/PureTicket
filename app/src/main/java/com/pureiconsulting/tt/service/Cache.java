package com.pureiconsulting.tt.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Hashtable;

/**
 * Created by julianzhu on 11/16/15.
 */
public class Cache {


    private static Cache instance = null;

    private Context context = null;

    private Hashtable<String, Hashtable> cache = new Hashtable<String, Hashtable>();


    public Cache(Context context) {

        this.context = context;
    }

    /**
     * Get cache instance to use (the only instance)
     *
     * @return
     */
    public static Cache getInstance(Context context) {

        if (instance == null)
            instance = new Cache(context);

        return instance;
    }

    /**
     * Get
     *
     * @param clsType
     * @return
     */
    public Hashtable getObjectCache(String clsType) {

        synchronized (cache) {

            Hashtable<String, Cacheable> objectCache = cache.get(clsType);

            if (objectCache == null) {

                objectCache = new Hashtable<String, Cacheable>();
                cache.put(clsType, objectCache);
            }

            return cache.get(clsType);
        }

    }

    public Cacheable getObject(Cacheable object) {

        Hashtable<String, Cacheable> objectCache = getObjectCache(object.getClass().getName());
        return objectCache.get(object.getId());
    }

    public void cacheObject(Cacheable object) {

        if (object == null) return;

        Hashtable<String, Cacheable> objectCache = getObjectCache(object.getClass().getName());

        objectCache.put(object.getId(), object);

    }

    public void remove(Cacheable object) {

        if (object == null || object.getId() == null) return;

        Hashtable<String, Cacheable> objectCache = getObjectCache(object.getClass().getName());

        objectCache.remove(object.getId());

    }

    public String readAssetFile(String filename) {

        //File file = new File(context.getFilesDir(), filename);
        //String filename = "myfile";
        //String string = "Hello world!";
        InputStream is;
        StringBuilder sb = new StringBuilder();

        try {
            //is = context.openFileInput(filename);
            is = context.getAssets().open(filename);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            is.close();

        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        } finally {
            return sb.toString();
        }

    }

    protected String readFromFile(String filename) {

        //File file = new File(context.getFilesDir(), filename);
        //String filename = "myfile";
        //String string = "Hello world!";
        FileInputStream is;
        StringBuilder sb = new StringBuilder();

        try {
            is = context.openFileInput(filename);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            is.close();

        } catch (Exception e) {
            //e.printStackTrace();
            return null;

        } finally {
            return sb.toString();
        }

    }

    public String readFromURL(String urlname) {

        //File file = new File(context.getFilesDir(), filename);
        //String filename = "myfile";
        //String string = "Hello world!";
        InputStream is;
        StringBuilder sb = new StringBuilder();

        try {

            URL url = new URL(urlname);

            is = url.openStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null; // nothing is received.
        } finally {
            return sb.toString();
        }

    }

    protected void saveAsFile(String file, String str) {

        //File file = new File(context.getFilesDir(), filename);

        FileOutputStream outputStream;

        try {
            Log.i("File", "Output stream ....");
            outputStream = context.openFileOutput(file, Context.MODE_PRIVATE);
            outputStream.write(str.getBytes());
            outputStream.close();

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public File getTempFile(Context context, String url) {

        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (java.io.IOException e) {
            // Error while creating file
        }
        return file;

    }

}
