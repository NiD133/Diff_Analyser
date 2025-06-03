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
    public void test09() throws Throwable {
        MockDate mockDate0 = new MockDate(774, 774, 32);
        DateRange dateRange0 = new DateRange(mockDate0, mockDate0);
        assertFalse(dateRange0.isNaNRange());
    }
}
