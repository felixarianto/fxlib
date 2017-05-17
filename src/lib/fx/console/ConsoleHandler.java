/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import static lib.fx.console.Console.TAG;
import lib.fx.logger.Log;

/**
 *
 * @author febri
 */
public abstract class ConsoleHandler extends Thread {

    private Socket mSocket;
    private BufferedWriter mWriter;
    private BufferedReader mReader;
    public ConsoleHandler(Socket p_socket) {
        try {
            mSocket  = p_socket;
            mWriter  = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            mReader  = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        }
        catch (IOException ex) {
            Log.e(TAG, ex);
        }
    }

    @Override
    public void run() {
        while (!mSocket.isClosed()) {
            try {
                String line = mReader.readLine();
                if (line == null) {
                    mSocket.close();
                }
                else {
                    onReceive(line);
                }
            }
            catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        try { mWriter.close();} catch (Exception ex) {}
        try { mReader.close();} catch (Exception ex) {}
    }

    public void writeLn(String p_data) {
        write(p_data + "\r\n");
    }
    public void write(String p_data) {
        try {
            if (mWriter != null) {
                mWriter.write(p_data);
                mWriter.flush();
            }
        }
        catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public abstract void onReceive(String p_data);
}
