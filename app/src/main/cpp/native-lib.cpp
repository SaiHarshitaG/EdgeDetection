#include <jni.h>
#include <string>
#include <android/log.h>
#include <vector>

#define LOG_TAG "native-lib"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// NOTE: This implementation is a fallback stub (no OpenCV) that maps the Y plane -> RGBA bytes.
// For real edge detection, include OpenCV headers, convert NV21->Mat, run cv::Canny, convert to RGBA,
// then return the bytes as shown below.

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_edgedetector_NativeBridge_processFrame(JNIEnv* env, jclass clazz,
                                                       jbyteArray frameNV21, jint width, jint height) {
    jsize len = env->GetArrayLength(frameNV21);
    jbyte* data = env->GetByteArrayElements(frameNV21, nullptr);

    int w = width;
    int h = height;
    int outLen = w * h * 4; // RGBA
    std::vector<uint8_t> out(outLen);

    // Map Y plane to grayscale RGBA
    for (int i = 0; i < w*h; ++i) {
        uint8_t y = static_cast<uint8_t>(data[i]);
        out[4*i + 0] = y; // R
        out[4*i + 1] = y; // G
        out[4*i + 2] = y; // B
        out[4*i + 3] = 255; // A
    }

    jbyteArray result = env->NewByteArray(outLen);
    env->SetByteArrayRegion(result, 0, outLen, reinterpret_cast<jbyte*>(out.data()));

    env->ReleaseByteArrayElements(frameNV21, data, JNI_ABORT);
    return result;
}
