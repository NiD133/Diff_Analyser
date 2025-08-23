package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest31 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        Object object0 = new Object();
        JsonLocation jsonLocation0 = new JsonLocation(object0, 0L, 759, (-1047));
        // Undeclared exception!
        try {
            jsonLocation0.appendOffsetDescription((StringBuilder) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.fasterxml.jackson.core.JsonLocation", e);
        }
    }
}
