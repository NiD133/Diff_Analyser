package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest9 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[0];
        StackTraceElement[] stackTraceElementArray1 = stackTraceFilter0.filter(stackTraceElementArray0, false);
        stackTraceFilter0.findSourceFile(stackTraceElementArray1, "S-pOJA?<SA5#B9J&^;");
        assertNotSame(stackTraceElementArray1, stackTraceElementArray0);
        assertNotSame(stackTraceElementArray0, stackTraceElementArray1);
    }
}
