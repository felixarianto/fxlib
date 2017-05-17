/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import lib.fx.logger.Log;

/**
 *
 * @author ricky robiansyah
 */
public class Console {
    
    public static final String TAG = "Console";
        
    public Console() {
    }
    
    public interface BindCallback {
        public void onAccept(Socket p_socket);
    }
    
    private ServerSocket mServer = null;
    public final void bind(int p_port, BindCallback p_callback) throws IOException {
        mServer = new ServerSocket(p_port);
        new Thread() {
            @Override
            public void run() {
                while (!mServer.isClosed()) {                    
                    try {
                        p_callback.onAccept(mServer.accept());
                    }
                    catch (Exception e) {
                        Log.e(TAG, e);
                    }
                }
            }
        }.start();
    }
    
    public final void unbind() throws IOException {
        if (mServer != null){
            mServer.close();
        }
    }
    
    public static ConsoleHandler loadHandler(String p_path) {
        ConsoleHandler handler = null;
        try {
            handler = (ConsoleHandler) new ControlClassLoader().getClassFile("lib.fx.console.ConsoleHandler", p_path).newInstance();
        }
        catch (Exception | Error e) {
            Log.e(TAG, e);
        }
        return handler;
    }
    
    
}