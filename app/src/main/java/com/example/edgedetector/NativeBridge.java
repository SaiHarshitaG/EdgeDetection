package com.example.edgedetector;

public class NativeBridge {
    // Native method: accepts NV21 byte array and returns RGBA byte array
    public static native byte[] processFrame(byte[] frameNV21, int width, int height);
}
