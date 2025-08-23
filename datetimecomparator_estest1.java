package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class DateTimeComparator_ESTestTest1 extends DateTimeComparator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        DateTimeComparator dateTimeComparator0 = DateTimeComparator.getDateOnlyInstance();
        // Undeclared exception!
        try {
            dateTimeComparator0.compare((Object) null, "DateTimeComparator[dayOfYear-]");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Invalid format: \"DateTimeComparator[dayOfYear-]\"
            //
            verifyException("org.joda.time.format.DateTimeParserBucket", e);
        }
    }
}
