package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest21 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        ContentReference contentReference0 = ContentReference.redacted();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        ContentReference contentReference1 = ContentReference.construct(true, (Object) contentReference0, 0, 1929, errorReportConfiguration0);
        JsonLocation jsonLocation0 = new JsonLocation((Object) contentReference1, (-578L), 500, 500);
        ContentReference contentReference2 = jsonLocation0.contentReference();
        assertEquals(500, jsonLocation0.getLineNr());
        assertEquals(500, jsonLocation0.getColumnNr());
        assertEquals((-1L), jsonLocation0.getByteOffset());
        assertTrue(contentReference2.hasTextualContent());
        assertEquals((-578L), jsonLocation0.getCharOffset());
    }
}
