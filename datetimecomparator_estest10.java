package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest10 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DateTimeComparator dateTimeComparator0 = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator dateTimeComparator1 = DateTimeComparator.getInstance();
        boolean boolean0 = dateTimeComparator0.equals(dateTimeComparator1);
        assertFalse(boolean0);
    }
}
