package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparableObjectItem_ESTestTest18 extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        ComparableObjectItem comparableObjectItem0 = new ComparableObjectItem("^VA/-a*$;'d", "^VA/-a*$;'d");
        Object object0 = comparableObjectItem0.clone();
        assertNotSame(comparableObjectItem0, object0);
    }
}
