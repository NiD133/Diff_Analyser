package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest22 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        Object object0 = new Object();
        ErrorReportConfiguration errorReportConfiguration0 = new ErrorReportConfiguration((-1262), (-1262));
        ContentReference contentReference0 = ContentReference.construct(true, object0, errorReportConfiguration0);
        ContentReference contentReference1 = ContentReference.construct(true, (Object) contentReference0, 4615, 0, errorReportConfiguration0);
        JsonLocation jsonLocation0 = new JsonLocation(contentReference1, 3039L, 575, (-559));
        ContentReference contentReference2 = jsonLocation0.contentReference();
        assertEquals(575, jsonLocation0.getLineNr());
        assertEquals(0, contentReference2.contentLength());
        assertEquals((-559), jsonLocation0.getColumnNr());
        assertEquals((-1L), jsonLocation0.getByteOffset());
        assertEquals(3039L, jsonLocation0.getCharOffset());
    }
}
