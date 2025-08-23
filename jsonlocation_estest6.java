package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest6 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        ErrorReportConfiguration errorReportConfiguration0 = new ErrorReportConfiguration(1, 1);
        ContentReference contentReference0 = ContentReference.construct(false, (Object) null, 2879, 2879, errorReportConfiguration0);
        JsonLocation jsonLocation0 = new JsonLocation(contentReference0, (long) 314, 0, (-2189));
        jsonLocation0.hashCode();
        assertEquals(314L, jsonLocation0.getCharOffset());
        assertEquals((-1L), jsonLocation0.getByteOffset());
        assertEquals((-2189), jsonLocation0.getColumnNr());
        assertEquals(0, jsonLocation0.getLineNr());
    }
}
