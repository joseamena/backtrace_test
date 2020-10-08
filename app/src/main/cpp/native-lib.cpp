#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_joseantoniomena_simplecalculator_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_joseantoniomena_simplecalculator_MainActivity_add(JNIEnv *env, jobject, jint x, jint y) {
    return x + y;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_joseantoniomena_simplecalculator_MainActivity_subtract(JNIEnv *env, jobject, jint x, jint y) {
    return x - y;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_joseantoniomena_simplecalculator_MainActivity_multiply(JNIEnv *env, jobject, jint x, jint y) {
    return x * y;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_joseantoniomena_simplecalculator_MainActivity_divide(JNIEnv *env, jobject, jint x, jint y) {
    return x / y;
}