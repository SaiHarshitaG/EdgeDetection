# EdgeDetector — Android + JNI + OpenCV + OpenGL + TypeScript Viewer

**Author:** Sai Harshita Ganti  
**Assignment:** Software Engineering Intern (R&D) — Image Processing Prototype  
**Platform:** Android (Java + C++), Web (TypeScript)

---

## 🚀 Project Overview
This project implements a **real-time image processing pipeline** on Android using **native C++** (via JNI) and **OpenGL ES rendering**.  
It demonstrates the integration of multiple system components:
- Android **Camera2 API** for capturing frames
- **JNI bridge** for communication between Java and native code
- **C++ (OpenCV)** for performing **edge detection** on live camera frames
- **OpenGL ES** for rendering processed frames on the device
- A **TypeScript web viewer** to visualize processed images and FPS

This structure mirrors real-world **embedded vision and R&D prototype** systems — where native performance, platform integration, and visualization are equally important.

---

## 🧩 Architecture Diagram
```text
Camera2 (NV21 Frames)
        ↓
  JNI Bridge (Java ↔ C++)
        ↓
 OpenCV Native Processing (Canny Edge)
        ↓
 RGBA Output Buffer
        ↓
  OpenGL Renderer (GLSurfaceView)
        ↓
 Display on Android Device
```
Additionally, the web viewer (`/web`) allows displaying a static processed frame with FPS overlay for simulation or demo purposes.

---

## 🧠 Tech Stack
| Layer | Technology |
|-------|-------------|
| Android Frontend | Java, Camera2, TextureView, GLSurfaceView |
| Native Processing | C++, OpenCV (Canny edge detection), JNI |
| Rendering | OpenGL ES 2.0 |
| Web Visualization | TypeScript, HTML, CSS |

---

## ⚙️ Features
- Simulated NV21 → RGBA grayscale frame conversion (fallback when OpenCV not configured)
- Modular CMake + JNI setup for native libraries
- Simple OpenGL renderer for real-time frame drawing
- Extendable to integrate any OpenCV transformation (Canny, Sobel, etc.)
- Optional TypeScript web viewer with FPS and resolution display

---

## 📁 Project Structure
```text
EdgeDetectorProject/
│
├── app/                      # Android app module
│   ├── src/main/java/com/example/edgedetector/
│   │   ├── MainActivity.java          # Camera2 + GL integration
│   │   ├── Camera2Helper.java         # Frame source (simulated)
│   │   ├── GLRenderer.java            # OpenGL rendering
│   │   ├── GLSurfaceViewWrapper.java  # GLSurfaceView wrapper
│   │   └── NativeBridge.java          # JNI bridge
│   │
│   ├── src/main/cpp/
│   │   ├── CMakeLists.txt             # Native build config
│   │   └── native-lib.cpp             # C++ image processing
│   │
│   └── src/main/AndroidManifest.xml
│
├── web/                     # Optional TypeScript web viewer
│   ├── index.html
│   ├── ts/app.ts
│   ├── tsconfig.json
│   └── app.js (compiled)
│
└── README.md
```
---

## 🛠️ Dependencies

### Android Requirements
- Android Studio (latest)
- Android SDK (API Level ≥ 33)
- Android NDK + CMake (via SDK Tools)
- Physical Android device (for Camera & JNI)
- Optional: OpenCV Android SDK (for real edge detection)

### Web Requirements
- Node.js & npm
- TypeScript (`npm install -g typescript`)

---

## 🔧 Build & Run (Android)
1. Open the project folder in **Android Studio**
2. Ensure NDK and CMake are installed
3. Build the project (`Build → Make Project`)
4. Connect an Android device and run (`Run → Run 'app'`)
5. Grant camera permission if requested

> The app simulates NV21 frames by default.  
> To enable real processing, add OpenCV SDK and replace the stub logic in `native-lib.cpp`.

---

## 🌐 Build & Run (Web)
1. Open terminal in `/web` folder
2. Run:
   ```bash
   npm install -g typescript
   npx tsc -p tsconfig.json
3. Open index.html in a browser
