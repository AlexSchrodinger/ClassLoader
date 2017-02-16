package ru.bernarsoft;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.net.URL;

public class JarClassLoader extends ClassLoader {

//    private String jarFile = "Animal.jar";
    private HashMap<String, Class> classes = new HashMap<>();
    private URL url;


    public JarClassLoader() {
        super(JarClassLoader.class.getClassLoader());
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }

    public Class loadClassFromURL(String className, String URL) throws ClassNotFoundException {


        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        try (InputStream is = url.openStream()) {
//            Files.copy(is, Paths.get(jarFile), StandardCopyOption.REPLACE_EXISTING);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return findClass(className);
    }

    public Class findClass(String className) {
        byte classByte[];
        Class result = null;

        result = classes.get(className);
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (ClassNotFoundException e) {
        }

        try {
            try (InputStream is = url.openStream()) {
                Files.copy(is, Paths.get("Animal.jar"), StandardCopyOption.REPLACE_EXISTING);
            }

            JarFile jar = new JarFile("Animal.jar");
            JarEntry entry = jar.getJarEntry(className + ".class");
            InputStream is = jar.getInputStream(entry);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            classByte = byteStream.toByteArray();
            result = defineClass(className, classByte, 0, classByte.length, null);
            classes.put(className, result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
