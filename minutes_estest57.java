package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Minutes_ESTestTest57 extends Minutes_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test56() throws Throwable {
        int[] intArray0 = new int[1];
        Partial partial0 = new Partial((Chronology) null, (DateTimeFieldType[]) null, intArray0);
        // Undeclared exception!
        try {
            Minutes.minutesBetween((ReadablePartial) partial0, (ReadablePartial) partial0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.joda.time.Partial", e);
        }
    }
}
