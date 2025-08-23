package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparableObjectItem_ESTestTest7 extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        ComparableObjectItem comparableObjectItem0 = new ComparableObjectItem("h", "h");
        // Undeclared exception!
        try {
            comparableObjectItem0.compareTo((ComparableObjectItem) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.data.ComparableObjectItem", e);
        }
    }
}
