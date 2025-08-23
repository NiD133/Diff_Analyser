package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparableObjectItem_ESTestTest2 extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Comparable<Object> comparable0 = (Comparable<Object>) mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(comparable0).toString();
        ComparableObjectItem comparableObjectItem0 = new ComparableObjectItem(comparable0, comparable0);
        Comparable comparable1 = comparableObjectItem0.getComparable();
        assertNotNull(comparable1);
    }
}
