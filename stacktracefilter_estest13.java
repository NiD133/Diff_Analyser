package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

public class StackTraceFilter_ESTestTest13 extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        StackTraceFilter stackTraceFilter0 = new StackTraceFilter();
        MockThrowable mockThrowable0 = new MockThrowable();
        StackTraceElement stackTraceElement0 = stackTraceFilter0.filterFirst(mockThrowable0, false);
        //  // Unstable assertion: assertNotNull(stackTraceElement0);
        //  // Unstable assertion: assertEquals("jdk.internal.reflect.GeneratedConstructorAccessor44", stackTraceElement0.getClassName());
        StackTraceElement[] stackTraceElementArray0 = new StackTraceElement[1];
        stackTraceElementArray0[0] = stackTraceElement0;
        StackTraceElement[] stackTraceElementArray1 = stackTraceFilter0.filter(stackTraceElementArray0, false);
        //  // Unstable assertion: assertEquals(1, stackTraceElementArray1.length);
        //  // Unstable assertion: assertNotSame(stackTraceElementArray1, stackTraceElementArray0);
    }
}
