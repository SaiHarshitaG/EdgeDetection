package com.example.edgedetector;

import android.content.Context;
import android.graphics.SurfaceTexture;

import java.util.concurrent.Executors;

/*
 * NOTE: This is a light-weight Camera2 helper placeholder.
 * It simulates frames by generating a neutral NV21-like buffer every 100ms.
 * Replace with real Camera2 + ImageReader code (YUV_420_888 -> NV21 conversion) for production.
 */

public class Camera2Helper {
    public interface FrameListener { void onFrame(byte[] nv21); }
    private final Context ctx;
    private final FrameListener listener;
    private int width, height;

    public Camera2Helper(Context c, SurfaceTexture previewTexture, int w, int h, FrameListener l) {
        ctx = c;
        width = w; height = h;
        listener = l;
    }

    public void startCamera() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                while (true) {
                    int frameSize = width * height;
                    byte[] nv21 = new byte[frameSize + frameSize/2];
                    for (int i=0;i<frameSize;i++) nv21[i] = (byte)128; // mid-gray Y
                    // UV plane set to 128 by default (neutral color)
                    listener.onFrame(nv21);
                    Thread.sleep(100); // ~10 fps simulation
                }
            } catch (InterruptedException e) { }
        });
    }

    public void close() { /* cleanup */ }
}
