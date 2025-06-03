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
    public void test07() throws Throwable {
        MockDate mockDate0 = new MockDate(1, 785, (-574), (-1), 32, (-1));
        MockDate mockDate1 = new MockDate(0, 0, 1);
        DateRange dateRange0 = null;
        try {
            dateRange0 = new DateRange(mockDate0, mockDate1);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Range(double, double): require lower (-1.62865681E11) <= upper (-2.2089888E12).
            //
            verifyException("org.jfree.data.Range", e);
        }
    }
}
