package com.google.common.reflect;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class ParameterTestTest1 extends TestCase {

    @SuppressWarnings("unused")
    private void someMethod(int i, int j) {
    }

    @SuppressWarnings("unused")
    private void anotherMethod(int i, String s) {
    }

    public void testNulls() {
        try {
            Class.forName("java.lang.reflect.AnnotatedType");
        } catch (ClassNotFoundException runningInAndroidVm) {
            /*
       * Parameter declares a method that returns AnnotatedType, which isn't available on Android.
       * This would cause NullPointerTester, which calls Class.getDeclaredMethods, to throw
       * NoClassDefFoundError.
       */
            return;
        }
        for (Method method : ParameterTest.class.getDeclaredMethods()) {
            for (Parameter param : Invokable.from(method).getParameters()) {
                new NullPointerTester().testAllPublicInstanceMethods(param);
            }
        }
    }
}
