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
    public void test10() throws Throwable {
        DateRange dateRange0 = new DateRange(0.0, 0.0);
        Date date0 = dateRange0.getUpperDate();
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", date0.toString());
    }
}
