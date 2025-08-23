package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest38 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        JsonLocation jsonLocation0 = new JsonLocation((Object) null, 500, 500, 500);
        ContentReference contentReference0 = ContentReference.redacted();
        JsonLocation jsonLocation1 = new JsonLocation((Object) contentReference0, (-1513L), (long) 500, 500, 500);
        boolean boolean0 = jsonLocation1.equals(jsonLocation0);
        assertFalse(boolean0);
        assertEquals(500, jsonLocation1.getLineNr());
        assertEquals(500L, jsonLocation1.getCharOffset());
        assertEquals((-1513L), jsonLocation1.getByteOffset());
        assertEquals(500, jsonLocation1.getColumnNr());
        assertFalse(jsonLocation0.equals((Object) jsonLocation1));
    }
}
