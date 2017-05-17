/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.console;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException; 
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author febri
 * 
 */
public class ControlClassLoader extends ClassLoader {
   
    public Class getClassFile(String p_name, String p_url) throws FileNotFoundException, IOException {
        Class c = null;
        File f;
        f = new File(p_url);
        int size = (int) f.length();
        byte buff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        try (DataInputStream dis = new DataInputStream(fis)) {
            dis.readFully(buff);
        }
        c = defineClass(p_name, buff, 0, buff.length, null);
        return c;
    }

    public Class getClassJar(String p_name, String pathToJar) throws IOException, ClassNotFoundException {
        URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
        Class c;
        try (URLClassLoader cl = URLClassLoader.newInstance(urls)) {
            c = cl.loadClass(p_name);
        }
        return c;
    }
    

}
