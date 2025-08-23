package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest13 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        JsonLocation jsonLocation1 = new JsonLocation((Object) null, 500, 500, 500);
        ContentReference contentReference0 = ContentReference.redacted();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        ErrorReportConfiguration.overrideDefaultErrorReportConfiguration(errorReportConfiguration0);
        JsonLocation._wrap(contentReference0);
        Object object0 = new Object();
        String string0 = jsonLocation0.sourceDescription();
        assertEquals("UNKNOWN", string0);
    }
}
