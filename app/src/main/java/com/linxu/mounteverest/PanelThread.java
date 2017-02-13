package com.linxu.mounteverest;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Vanessa on 13.02.2017.
 */

public class PanelThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private Main_Activity_Layout panel;
    private boolean startRunning = false;


    public PanelThread(SurfaceHolder surfaceHolder, Main_Activity_Layout panel) {
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
    }


    public void setRunning(boolean run) { //Allow us to stop the thread
        startRunning = run;
    }


    @Override
    public void run() {
        Canvas c;
        while (startRunning) {     //When setRunning(false) occurs, startRunning is
            c = null;      //set to false and loop ends, stopping thread
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    //Insert methods to modify positions of items in onDraw()
                    // panel.incrementCount();
                    panel.postInvalidate();
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }

        }
    }

}
