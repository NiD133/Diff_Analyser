package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest13 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        DateTimeComparator dateTimeComparator0 = DateTimeComparator.getTimeOnlyInstance();
        Object object0 = new Object();
        boolean boolean0 = dateTimeComparator0.equals(object0);
        assertFalse(boolean0);
    }
}