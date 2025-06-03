package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Date;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockDate;
import org.jfree.data.Range;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        DateRange dateRange0 = new DateRange();
        long long0 = dateRange0.getLowerMillis();
        assertEquals(1L, dateRange0.getUpperMillis());
        assertEquals(0L, long0);
    }
}
