/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.logger;

import java.text.SimpleDateFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author febri
 */
public class Log {
    
    private static Logger mLogger;
    public static void create(String p_name) {
        mLogger = Logger.getLogger(p_name);
        mLogger.setLevel(Level.ALL);
        mLogger.addHandler(new Handler(){
            @Override
            public void publish(LogRecord log) {
                if (log.getLevel().equals(Level.SEVERE)) {
                    System.out.println(log.getMillis() + " " + log.getLevel().getName() + " " + log.getMessage());
                    System.out.println(log.toString());
                }
                else {
                    System.out.println(log.getMillis() + " " + log.getLevel().getName() + " " + log.getMessage());
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
            
        });
    }
    public static void i(String p_tag, String p_metalog, String p_message) {
        log(Level.INFO, p_tag, p_metalog, p_message);
    }
    public static void d(String p_tag, String p_metalog, String p_message) {
        log(Level.FINE, p_tag, p_metalog, p_message);
    }
    public static void w(String p_tag, String p_metalog, String p_message) {
        log(Level.WARNING, p_tag, p_metalog, p_message);
    }
    public static void c(String p_tag, String p_metalog, String p_message) {
        log(Level.CONFIG, p_tag, p_metalog, p_message);
    }
    public static void e(String p_tag, String p_metalog, String p_message) {
        log(Level.SEVERE, p_tag, p_metalog, p_message);
    }
    public static void e(String p_tag, Throwable p_throw) {
        e(p_tag, "", p_throw);
    }
    public static void e(String p_tag, String p_metalog, Throwable p_throw) {
        log(Level.SEVERE, p_tag, p_metalog, p_throw.toString());
        try {
            StackTraceElement[] stacks = p_throw.getStackTrace();
            if (stacks != null) {
                for (StackTraceElement stack : stacks) {
                    log(Level.SEVERE, p_tag, p_metalog, stack.toString());
                }
            }
        }
        catch (Exception e) {
            p_throw.printStackTrace();
        }
    }
    private static void log(Level p_level, String p_tag, String p_metalog, String p_message) {
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis()) + "   " + p_level.getName().substring(0, 4) + "   " +  p_tag.concat("|").concat(p_metalog).concat("   ").concat(p_message));
    }
    
}
