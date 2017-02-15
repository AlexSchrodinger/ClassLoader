package ru.bernarsoft;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.net.URL;

public class JarClassLoader extends ClassLoader {

    private String jarFile = "Animal.jar"; //Path to the jar file
    private Hashtable classes = new Hashtable(); //used to cache already defined classes

    public JarClassLoader() {
        super(JarClassLoader.class.getClassLoader()); //calls the parent class loader's constructor
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }

    public Class loadClassFromURL(String className, String URL) throws ClassNotFoundException {
        java.net.URL url = null;

        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try (InputStream is = url.openStream()) {
            Files.copy(is, Paths.get(jarFile), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return findClass(className);
    }

    public Class findClass(String className) {
        byte classByte[];
        Class result = null;

        result = (Class) classes.get(className); //checks in cached classes
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (Exception e) {
        }

        try {
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> entries = jar.entries();
            JarEntry anEntry;
            JarEntry entry = jar.getJarEntry(className + ".class");
            if (entry == null) while (entries.hasMoreElements()) {
                anEntry = entries.nextElement();
                if (!anEntry.isDirectory()) {
                    String[] dirsAndName = anEntry.getName().split("/");
                    String name = dirsAndName[dirsAndName.length-1];
                    if (name.equals(className + ".class")) {
                        entry = anEntry;
                    }
                }
            }
            InputStream is = jar.getInputStream(entry);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            String classAndPack = entry.getName();
            classAndPack = classAndPack.replace('.', ' ');
            classAndPack = classAndPack.replace('/', '.');
            String classPath = classAndPack.split(" ")[0];

            classByte = byteStream.toByteArray();

            result = defineClass(classPath, classByte, 0, classByte.length);
            classes.put(className, result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
