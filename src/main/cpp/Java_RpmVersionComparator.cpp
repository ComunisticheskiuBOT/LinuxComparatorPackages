#include <jni.h>
#include "org_example_RpmVersionComparator.h"
#include "rpmvercmp.h"
#include <stdio.h>
extern "C"{
    JNIEXPORT jboolean JNICALL Java_org_example_RpmVersionComparator_compareVersions(JNIEnv* env, jobject obj, jstring version1, jstring version2) {
        const char* v1 = env->GetStringUTFChars(version1, NULL);
        const char* v2 = env->GetStringUTFChars(version2, NULL);

        int result = rpmvercmp(v1, v2);
        env->ReleaseStringUTFChars(version1, v1);
        env->ReleaseStringUTFChars(version2, v2);

        return result > 0 ? JNI_TRUE : JNI_FALSE;
    }
}