package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest43 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        JsonLocation jsonLocation1 = new JsonLocation((Object) null, 500, 500, 500);
        boolean boolean0 = jsonLocation0.equals(jsonLocation1);
        assertEquals(500, jsonLocation1.getColumnNr());
        assertEquals(500L, jsonLocation1.getCharOffset());
        assertFalse(boolean0);
        assertEquals((-1L), jsonLocation1.getByteOffset());
        assertEquals(500, jsonLocation1.getLineNr());
    }
}
