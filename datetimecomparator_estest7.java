package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest7 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        DateTimeComparator dateTimeComparator0 = DateTimeComparator.getInstance();
        String string0 = dateTimeComparator0.toString();
        assertEquals("DateTimeComparator[]", string0);
    }
}
