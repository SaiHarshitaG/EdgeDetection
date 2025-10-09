cmake_minimum_required(VERSION 3.10.2)
project("edge_detector_native")

add_library(native-lib SHARED native-lib.cpp)

find_library(log-lib log)

set(OpenCV_DIR "C:/OpenCV-android-sdk/sdk/native/jni")
find_package(OpenCV REQUIRED)
include_directories(${OpenCV_INCLUDE_DIRS})
target_link_libraries(native-lib ${OpenCV_LIBS} ${log-lib})


target_link_libraries(native-lib ${log-lib})
