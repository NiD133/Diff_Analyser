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
    public void test04() throws Throwable {
        DateRange dateRange0 = new DateRange();
        Range range0 = Range.expandToInclude(dateRange0, 1.2030679447063568);
        // Undeclared exception!
        try {
            range0.toString();
            //  fail("Expecting exception: IllegalArgumentException");
            // Unstable assertion
        } catch (IllegalArgumentException e) {
        }
    }
}
