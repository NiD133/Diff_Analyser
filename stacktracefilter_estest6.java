package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest6 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        MockThrowable mockThrowable0 = new MockThrowable();
        StackTraceElement stackTraceElement0 = stackTraceFilter0.filterFirst(mockThrowable0, false);
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[1];
        stackTraceElementArray0[0] = stackTraceElement0;
        String string0 = stackTraceFilter0.findSourceFile(stackTraceElementArray0, "org.mockito.internal.configuration.plugins.DefaultMockitoPlugins");
        //  // Unstable assertion: assertNull(string0);
    }
}
