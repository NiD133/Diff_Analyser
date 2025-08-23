package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest4 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        MockThrowable mockThrowable0 = new MockThrowable();
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        mockThrowable0.setOriginForDelegate((StackTraceElement) null);
        // Undeclared exception!
        try {
            stackTraceFilter0.filterFirst(mockThrowable0, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner", e);
        }
    }
}
