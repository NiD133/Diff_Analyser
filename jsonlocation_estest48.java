package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest48 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test47() throws Throwable {
        ContentReference contentReference0 = ContentReference.redacted();
        JsonLocation jsonLocation0 = new JsonLocation((Object) contentReference0, (-578L), 500, 500);
        int int0 = jsonLocation0.getColumnNr();
        assertEquals((-578L), jsonLocation0.getCharOffset());
        assertEquals(500, jsonLocation0.getLineNr());
        assertEquals(500, int0);
        assertEquals((-1L), jsonLocation0.getByteOffset());
    }
}
