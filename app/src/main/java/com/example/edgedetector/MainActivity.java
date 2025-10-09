package com.example.edgedetector;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {
    static { System.loadLibrary("native-lib"); }

    private static final String TAG = "MainActivity";
    private final int REQ_CAMERA = 101;
    private TextureView cameraPreview;
    private Camera2Helper camera2Helper;
    private GLRenderer glRenderer;
    private GLSurfaceViewWrapper glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout root = new FrameLayout(this);
        cameraPreview = new TextureView(this);
        cameraPreview.setSurfaceTextureListener(this);

        glView = new GLSurfaceViewWrapper(this);
        glRenderer = new GLRenderer(this);

        glView.setEGLContextClientVersion(2);
        glView.setRenderer(glRenderer);
        glView.setRenderMode(android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        root.addView(glView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(cameraPreview, new FrameLayout.LayoutParams(1,1)); // hidden preview
        setContentView(root);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        Log.i(TAG, "Texture available");
        // set expected frame size here
        final int frameWidth = 1280;
        final int frameHeight = 720;

        camera2Helper = new Camera2Helper(this, surface, frameWidth, frameHeight, frame -> {
            // frame is NV21 byte[]; send to native and update texture
            byte[] processed = NativeBridge.processFrame(frame, frameWidth, frameHeight);
            if (processed != null) {
                glRenderer.updateFrame(processed, frameWidth, frameHeight);
                glView.requestRender();
            }
        });
        camera2Helper.startCamera();
    }

    @Override public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {}
    @Override public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) { if (camera2Helper!=null) camera2Helper.close(); return true; }
    @Override public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted — Texture listener will start camera when available
            } else {
                // permission denied — handle gracefully
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
