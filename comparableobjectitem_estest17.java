package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class ComparableObjectItem_ESTestTest17 extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        Comparable<ComparableObjectItem> comparable0 = (Comparable<ComparableObjectItem>) mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparable0).compareTo(any(org.jfree.data.ComparableObjectItem.class));
        doReturn((String) null).when(comparable0).toString();
        Object object0 = new Object();
        ComparableObjectItem comparableObjectItem0 = new ComparableObjectItem(comparable0, object0);
        int int0 = comparableObjectItem0.compareTo(comparableObjectItem0);
        assertEquals(0, int0);
    }
}