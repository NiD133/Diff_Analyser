package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest45 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        ContentReference contentReference0 = ContentReference.construct(true, (Object) jsonLocation0, errorReportConfiguration0);
        JsonLocation jsonLocation1 = new JsonLocation(contentReference0, (long) 500, (-1), 256);
        String string0 = jsonLocation1.toString();
        assertEquals((-1L), jsonLocation1.getByteOffset());
        assertEquals((-1), jsonLocation1.getLineNr());
        assertEquals(500L, jsonLocation1.getCharOffset());
        assertEquals("[Source: (com.fasterxml.jackson.core.JsonLocation); line: UNKNOWN, column: 256]", string0);
    }
}
