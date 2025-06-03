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
    public void test02() throws Throwable {
        Range range0 = Range.expandToInclude((Range) null, 1801.7621);
        DateRange dateRange0 = new DateRange(range0);
        long long0 = dateRange0.getLowerMillis();
        assertEquals(1801L, dateRange0.getUpperMillis());
        assertEquals(1801L, long0);
    }
}
