package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest12 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[1];
        StackTraceElement stackTraceElement0 = new StackTraceElement("org.mockito.internal.PremainAttach", "org.mockito.internal.PremainAttach", "org.mockito.internal.PremainAttach", (-1512));
        stackTraceElementArray0[0] = stackTraceElement0;
        StackTraceElement[] stackTraceElementArray1 = stackTraceFilter0.filter(stackTraceElementArray0, false);
        assertEquals(0, stackTraceElementArray1.length);
    }
}
