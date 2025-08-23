package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest47 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        ContentReference contentReference0 = ContentReference.redacted();
        JsonLocation jsonLocation0 = new JsonLocation((Object) contentReference0, (-578L), 500, 500);
        long long0 = jsonLocation0.getCharOffset();
        assertEquals((-1L), jsonLocation0.getByteOffset());
        assertEquals(500, jsonLocation0.getLineNr());
        assertEquals(500, jsonLocation0.getColumnNr());
        assertEquals((-578L), long0);
    }
}