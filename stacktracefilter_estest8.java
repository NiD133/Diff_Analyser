package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest8 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[1];
        // Undeclared exception!
        try {
            stackTraceFilter0.findSourceFile(stackTraceElementArray0, "org.mockito.internal.configuration.plugins.DefaultMockitoPlugins");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner", e);
        }
    }
}
