package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest24 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        ContentReference contentReference0 = ContentReference.redacted();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        ContentReference contentReference1 = ContentReference.construct(true, (Object) contentReference0, 0, 1929, errorReportConfiguration0);
        ContentReference contentReference2 = JsonLocation._wrap(contentReference1);
        assertTrue(contentReference2.hasTextualContent());
    }
}
