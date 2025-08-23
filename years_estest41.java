package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Years_ESTestTest41 extends Years_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        DateTimeFieldType[] dateTimeFieldTypeArray0 = new DateTimeFieldType[1];
        Partial partial0 = new Partial((Chronology) null, dateTimeFieldTypeArray0, (int[]) null);
        // Undeclared exception!
        try {
            Years.yearsBetween((ReadablePartial) partial0, (ReadablePartial) partial0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.Partial", e);
        }
    }
}
