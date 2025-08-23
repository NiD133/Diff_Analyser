package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest7 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[1];
        StackTraceElement stackTraceElement0 = new StackTraceElement((String) null, (String) null, (String) null, "org.mockito.internal.exceptions.stacktrace.StackTraceFilter", "org.mockito.internal.exceptions.stacktrace.StackTraceFilter", "org.mockito.internal.exceptions.stacktrace.StackTraceFilter", 2);
        stackTraceElementArray0[0] = stackTraceElement0;
        String string0 = stackTraceFilter0.findSourceFile(stackTraceElementArray0, (String) null);
        assertNull(string0);
    }
}
