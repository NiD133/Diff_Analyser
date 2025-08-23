package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest7 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Object object0 = new Object();
        ContentReference contentReference0 = JsonLocation._wrap(object0);
        JsonLocation jsonLocation0 = new JsonLocation((Object) contentReference0, 0L, 0L, 1831, 0);
        jsonLocation0.hashCode();
        assertEquals(0, jsonLocation0.getColumnNr());
        assertEquals(1831, jsonLocation0.getLineNr());
        assertFalse(contentReference0.hasTextualContent());
    }
}