package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest19 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.dayOfMonth();
        DateTimeComparator dateTimeComparator0 = new DateTimeComparator(dateTimeFieldType0, dateTimeFieldType0);
        dateTimeComparator0.hashCode();
    }
}
