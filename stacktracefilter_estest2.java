package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest2 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[1];
        StackTraceElement stackTraceElement0 = new StackTraceElement("", "", "", 0);
        stackTraceElementArray0[0] = stackTraceElement0;
        MockThrowable mockThrowable0 = new MockThrowable();
        mockThrowable0.setStackTrace(stackTraceElementArray0);
        StackTraceElement stackTraceElement1 = stackTraceFilter0.filterFirst(mockThrowable0, false);
        assertEquals("", stackTraceElement1.getMethodName());
    }
}
