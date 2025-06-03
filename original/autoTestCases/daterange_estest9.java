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
    public void test08() throws Throwable {
        DateRange dateRange0 = null;
        try {
            dateRange0 = new DateRange(0.0, (-5043.58));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Range(double, double): require lower (0.0) <= upper (-5043.58).
            //
            verifyException("org.jfree.data.Range", e);
        }
    }
}
