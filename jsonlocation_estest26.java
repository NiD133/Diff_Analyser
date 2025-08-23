package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest26 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        StringBuilder stringBuilder0 = new StringBuilder(500);
        ErrorReportConfiguration errorReportConfiguration0 = new ErrorReportConfiguration(500, (-1));
        ContentReference contentReference0 = ContentReference.construct(true, (Object) stringBuilder0, errorReportConfiguration0);
        JsonLocation jsonLocation0 = new JsonLocation(contentReference0, (long) 500, (long) 500, 1571, 500);
        // Undeclared exception!
        try {
            jsonLocation0.toString();
            fail("Expecting exception: StringIndexOutOfBoundsException");
        } catch (StringIndexOutOfBoundsException e) {
            //
            // String index out of range: -1
            //
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }
}
