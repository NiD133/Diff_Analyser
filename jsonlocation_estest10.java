package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest10 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        Object object0 = new Object();
        ContentReference contentReference0 = JsonLocation._wrap(object0);
        JsonLocation jsonLocation0 = new JsonLocation(contentReference0, (-1L), 1L, 0, 0);
        String string0 = jsonLocation0.offsetDescription();
        assertEquals("byte offset: #UNKNOWN", string0);
        assertEquals(1L, jsonLocation0.getCharOffset());
        assertEquals(0, jsonLocation0.getColumnNr());
        assertEquals(0, jsonLocation0.getLineNr());
    }
}
