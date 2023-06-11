/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_linusdev_clgl_nat_cl_CL */

#ifndef _Included_de_linusdev_clgl_nat_cl_CL
#define _Included_de_linusdev_clgl_nat_cl_CL
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clGetPlatformIDs
 * Signature: (ILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clGetPlatformIDs
  (JNIEnv *, jclass, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clGetPlatformInfo
 * Signature: (JIILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clGetPlatformInfo
  (JNIEnv *, jclass, jlong, jint, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clGetDeviceIDs
 * Signature: (JIILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clGetDeviceIDs
  (JNIEnv *, jclass, jlong, jint, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clCreateContext
 * Signature: (Ljava/nio/ByteBuffer;ILjava/nio/ByteBuffer;Ljava/lang/Class;JLjava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clCreateContext
  (JNIEnv *, jclass, jobject, jint, jobject, jclass, jlong, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clReleaseContext
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clReleaseContext
  (JNIEnv *, jclass, jlong);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clCreateCommandQueueWithProperties
 * Signature: (JJLjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clCreateCommandQueueWithProperties
  (JNIEnv *, jclass, jlong, jlong, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clCreateCommandQueue
 * Signature: (JJJLjava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clCreateCommandQueue
  (JNIEnv *, jclass, jlong, jlong, jlong, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clReleaseCommandQueue
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clReleaseCommandQueue
  (JNIEnv *, jclass, jlong);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clCreateBuffer
 * Signature: (JJJLjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clCreateBuffer
  (JNIEnv *, jclass, jlong, jlong, jlong, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clReleaseMemObject
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clReleaseMemObject
  (JNIEnv *, jclass, jlong);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clEnqueueReadBuffer
 * Signature: (JJZJJLjava/nio/ByteBuffer;ILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clEnqueueReadBuffer
  (JNIEnv *, jclass, jlong, jlong, jboolean, jlong, jlong, jobject, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clEnqueueWriteBuffer
 * Signature: (JJZJJLjava/nio/ByteBuffer;ILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clEnqueueWriteBuffer
  (JNIEnv *, jclass, jlong, jlong, jboolean, jlong, jlong, jobject, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clCreateProgramWithSource
 * Signature: (JLjava/lang/String;Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clCreateProgramWithSource
  (JNIEnv *, jclass, jlong, jstring, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clReleaseProgram
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clReleaseProgram
  (JNIEnv *, jclass, jlong);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clBuildProgram
 * Signature: (JILjava/nio/ByteBuffer;Ljava/lang/String;Ljava/lang/Class;J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clBuildProgram
  (JNIEnv *, jclass, jlong, jint, jobject, jstring, jclass, jlong);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clGetProgramBuildInfo
 * Signature: (JJIJLjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clGetProgramBuildInfo
  (JNIEnv *, jclass, jlong, jlong, jint, jlong, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clGetDeviceInfo
 * Signature: (JIJLjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clGetDeviceInfo
  (JNIEnv *, jclass, jlong, jint, jlong, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clCreateKernel
 * Signature: (JLjava/lang/String;Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clCreateKernel
  (JNIEnv *, jclass, jlong, jstring, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clReleaseKernel
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clReleaseKernel
  (JNIEnv *, jclass, jlong);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clSetKernelArg
 * Signature: (JIJJZ)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clSetKernelArg
  (JNIEnv *, jclass, jlong, jint, jlong, jlong, jboolean);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clGetKernelInfo
 * Signature: (JIJLjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clGetKernelInfo
  (JNIEnv *, jclass, jlong, jint, jlong, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clEnqueueNDRangeKernel
 * Signature: (JJILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;ILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clEnqueueNDRangeKernel
  (JNIEnv *, jclass, jlong, jlong, jint, jobject, jobject, jobject, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clCreateFromGLRenderbuffer
 * Signature: (JJILjava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clCreateFromGLRenderbuffer
  (JNIEnv *, jclass, jlong, jlong, jint, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clEnqueueAcquireGLObjects
 * Signature: (JILjava/nio/ByteBuffer;ILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clEnqueueAcquireGLObjects
  (JNIEnv *, jclass, jlong, jint, jobject, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clEnqueueReleaseGLObjects
 * Signature: (JILjava/nio/ByteBuffer;ILjava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clEnqueueReleaseGLObjects
  (JNIEnv *, jclass, jlong, jint, jobject, jint, jobject, jobject);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clFinish
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clFinish
  (JNIEnv *, jclass, jlong);

/*
 * Class:     de_linusdev_clgl_nat_cl_CL
 * Method:    _clFlush
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_linusdev_clgl_nat_cl_CL__1clFlush
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
