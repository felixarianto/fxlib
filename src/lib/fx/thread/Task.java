/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.thread;

/**
 *
 * @author febri
 */
public abstract class Task extends Thread {

    private boolean bRunning = false;
    public  long interval = 1000;
    
    @Override
    public void run() {
        bRunning = true;
        try {
            while (bRunning) {            
                onRunning();
                if (sleepCheck()) {
                    try {sleep(interval);} catch (Exception e) {}
                }
            }
        }
        catch (Exception e) {
            e("", e);
        }
        bRunning = false;
    }
    
    public void stopd() {
        bRunning = false;
    }
    protected abstract String getTag();
    protected abstract void onRunning();
    protected boolean sleepCheck(){return true;};
    protected abstract void e(String pMetalog, Throwable e);
    protected abstract void e(String pMetalog, String pMessage);
    protected abstract void d(String pMetalog, String pMessage);
    protected abstract void i(String pMetalog, String pMessage);
}
