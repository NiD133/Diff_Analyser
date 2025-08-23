package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest11 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[0];
        MockThrowable mockThrowable0 = new MockThrowable();
        mockThrowable0.setStackTrace(stackTraceElementArray0);
        StackTraceElement stackTraceElement0 = stackTraceFilter0.filterFirst(mockThrowable0, false);
        assertNull(stackTraceElement0);
    }
}
