LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := JniDemo
LOCAL_LDLIBS := -llog -landroid
LOCAL_SRC_FILES := JniDemo.cpp
LOCAL_SRC_FILES += com_example_jnidemo_MainActivity.cpp

include $(BUILD_SHARED_LIBRARY)
