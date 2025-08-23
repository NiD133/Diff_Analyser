package com.google.common.reflect;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class ParameterTestTest2 extends TestCase {

    @SuppressWarnings("unused")
    private void someMethod(int i, int j) {
    }

    @SuppressWarnings("unused")
    private void anotherMethod(int i, String s) {
    }

    public void testEquals() {
        EqualsTester tester = new EqualsTester();
        for (Method method : ParameterTest.class.getDeclaredMethods()) {
            for (Parameter param : Invokable.from(method).getParameters()) {
                tester.addEqualityGroup(param);
            }
        }
        tester.testEquals();
    }
}
