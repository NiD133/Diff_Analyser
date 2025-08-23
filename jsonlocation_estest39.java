package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest39 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        ContentReference contentReference0 = ContentReference.rawReference((Object) jsonLocation0);
        JsonLocation jsonLocation1 = new JsonLocation((Object) contentReference0, 1795L, (long) 500, 2, 3981);
        boolean boolean0 = jsonLocation0.equals(jsonLocation1);
        assertEquals(3981, jsonLocation1.getColumnNr());
        assertEquals(1795L, jsonLocation1.getByteOffset());
        assertEquals(500L, jsonLocation1.getCharOffset());
        assertEquals(2, jsonLocation1.getLineNr());
        assertFalse(boolean0);
    }
}
