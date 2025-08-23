package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest3 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        MockThrowable mockThrowable0 = new MockThrowable();
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[8];
        StackTraceElement stackTraceElement0 = new StackTraceElement("A", "$cbz1,wrBLR'*'t`%<", "$cbz1,wrBLR'*'t`%<", 6);
        stackTraceElementArray0[0] = stackTraceElement0;
        stackTraceElementArray0[1] = stackTraceElementArray0[0];
        stackTraceElementArray0[2] = stackTraceElementArray0[0];
        stackTraceElementArray0[3] = stackTraceElementArray0[0];
        stackTraceElementArray0[4] = stackTraceElementArray0[3];
        stackTraceElementArray0[5] = stackTraceElementArray0[1];
        stackTraceElementArray0[6] = stackTraceElementArray0[4];
        stackTraceElementArray0[7] = stackTraceElementArray0[4];
        mockThrowable0.setStackTrace(stackTraceElementArray0);
        StackTraceElement stackTraceElement1 = stackTraceFilter0.filterFirst(mockThrowable0, true);
        assertFalse(stackTraceElement1.isNativeMethod());
    }
}
