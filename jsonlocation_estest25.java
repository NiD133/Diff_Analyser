package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest25 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Object object0 = new Object();
        ErrorReportConfiguration errorReportConfiguration0 = new ErrorReportConfiguration(0, 0);
        ContentReference contentReference0 = ContentReference.construct(true, object0, 2492, 0, errorReportConfiguration0);
        ContentReference contentReference1 = JsonLocation._wrap(contentReference0);
        assertSame(contentReference0, contentReference1);
    }
}
