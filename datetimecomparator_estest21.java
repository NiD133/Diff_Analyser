package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest21 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        DateTimeComparator dateTimeComparator0 = DateTimeComparator.getInstance();
        DateTimeComparator dateTimeComparator1 = DateTimeComparator.getDateOnlyInstance();
        boolean boolean0 = dateTimeComparator1.equals(dateTimeComparator0);
        assertFalse(boolean0);
    }
}
