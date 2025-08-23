package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparableObjectItem_ESTestTest12 extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Object object0 = new Object();
        ComparableObjectItem comparableObjectItem0 = new ComparableObjectItem("^VA/-a*$;'d", object0);
        ComparableObjectItem comparableObjectItem1 = new ComparableObjectItem("^VA/-a*$;'d", comparableObjectItem0);
        boolean boolean0 = comparableObjectItem0.equals(comparableObjectItem1);
        assertFalse(boolean0);
    }
}
