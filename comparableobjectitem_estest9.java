package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparableObjectItem_ESTestTest9 extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        ComparableObjectItem comparableObjectItem0 = null;
        try {
            comparableObjectItem0 = new ComparableObjectItem((Comparable) null, (Object) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Null 'x' argument.
            //
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }
}
