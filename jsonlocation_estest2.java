package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest2 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        JsonLocation jsonLocation1 = new JsonLocation(jsonLocation0, 500, 500, 500, 500);
        JsonLocation jsonLocation2 = new JsonLocation(jsonLocation0, 500, 1L, 500, 500);
        boolean boolean0 = jsonLocation2.equals(jsonLocation1);
        assertEquals(500L, jsonLocation1.getCharOffset());
        assertEquals(500, jsonLocation2.getLineNr());
        assertEquals(500L, jsonLocation2.getByteOffset());
        assertFalse(jsonLocation1.equals((Object) jsonLocation2));
        assertEquals(500, jsonLocation2.getColumnNr());
        assertFalse(boolean0);
    }
}
