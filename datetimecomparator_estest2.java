package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest2 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.yearOfEra();
        DateTimeComparator dateTimeComparator0 = DateTimeComparator.getInstance(dateTimeFieldType0);
        DateTimeFieldType dateTimeFieldType1 = dateTimeComparator0.getLowerLimit();
        assertNotNull(dateTimeFieldType1);
        assertEquals("yearOfEra", dateTimeFieldType1.toString());
    }
}
