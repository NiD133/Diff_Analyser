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
    public void test03() throws Throwable {
        DateRange dateRange0 = new DateRange((-1157.7534), (-1157.7534));
        long long0 = dateRange0.getLowerMillis();
        assertEquals((-1157L), long0);
        assertEquals((-1157L), dateRange0.getUpperMillis());
    }
}
