package com.example.edgedetector;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import android.opengl.GLSurfaceView.Renderer;
import android.graphics.Bitmap;

public class GLRenderer implements Renderer {
    private static final String TAG = "GLRenderer";
    private final Context ctx;
    private int textureId = -1;
    private ByteBuffer currentFrame;
    private int texWidth = 0, texHeight = 0;

    // simple quad (posX,posY,posZ, u, v)
    private final float[] VERTICES = {
        -1f, -1f, 0f,  0f, 1f,
         1f, -1f, 0f,  1f, 1f,
        -1f,  1f, 0f,  0f, 0f,
         1f,  1f, 0f,  1f, 0f,
    };
    private FloatBuffer vertexBuffer;

    public GLRenderer(Context c) {
        ctx = c;
        vertexBuffer = ByteBuffer.allocateDirect(VERTICES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(VERTICES).position(0);
    }

    public synchronized void updateFrame(byte[] rgba, int width, int height) {
        texWidth = width; texHeight = height;
        currentFrame = ByteBuffer.allocateDirect(rgba.length);
        currentFrame.put(rgba).position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        textureId = createTexture();
        GLES20.glClearColor(0f,0f,0f,1f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        GLES20.glViewport(0,0,w,h);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (currentFrame != null && textureId!=-1 && texWidth>0 && texHeight>0) {
            currentFrame.position(0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

            // Create a temporary Bitmap and upload (simple, not optimal for performance).
            // For better performance, use glTexImage2D with the ByteBuffer directly (GL_UNSIGNED_BYTE, GL_RGBA).
            Bitmap bmp = Bitmap.createBitmap(texWidth, texHeight, Bitmap.Config.ARGB_8888);
            bmp.copyPixelsFromBuffer(currentFrame);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
            bmp.recycle();

            // A minimal draw call would be required: set up shader/program, vertex attribs, draw arrays.
            // For scaffold simplicity we skip full shader implementation.
        }
    }

    private int createTexture() {
        final int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        return tex[0];
    }
}
