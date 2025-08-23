package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest1 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        JsonLocation jsonLocation0 = new JsonLocation((Object) null, 500, 500, 500);
        ContentReference contentReference0 = ContentReference.redacted();
        JsonLocation jsonLocation1 = new JsonLocation(contentReference0, (long) 500, (long) 500, 500, 500);
        boolean boolean0 = jsonLocation1.equals(jsonLocation0);
        assertEquals(500, jsonLocation1.getLineNr());
        assertEquals(500, jsonLocation1.getColumnNr());
        assertEquals(500L, jsonLocation1.getByteOffset());
        assertFalse(boolean0);
        assertEquals(500L, jsonLocation0.getCharOffset());
        assertFalse(jsonLocation0.equals((Object) jsonLocation1));
    }
}
