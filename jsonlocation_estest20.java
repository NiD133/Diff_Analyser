package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest20 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        ContentReference contentReference0 = ContentReference.redacted();
        JsonLocation jsonLocation0 = new JsonLocation(contentReference0, 1852L, (-413L), 0, 74);
        long long0 = jsonLocation0.getByteOffset();
        assertEquals(1852L, long0);
        assertEquals(0, jsonLocation0.getLineNr());
        assertEquals((-413L), jsonLocation0.getCharOffset());
        assertEquals(74, jsonLocation0.getColumnNr());
    }
}
