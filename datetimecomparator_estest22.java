package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest22 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        DateTimeComparator dateTimeComparator0 = DateTimeComparator.getDateOnlyInstance();
        String string0 = dateTimeComparator0.toString();
        assertEquals("DateTimeComparator[dayOfYear-]", string0);
    }
}
