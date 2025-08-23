package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest15 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        JsonLocation jsonLocation0 = new JsonLocation((Object) null, (-219L), 314, 314);
        int int0 = jsonLocation0.getLineNr();
        assertEquals(314, int0);
        assertEquals(314, jsonLocation0.getColumnNr());
        assertEquals((-219L), jsonLocation0.getCharOffset());
        assertEquals((-1L), jsonLocation0.getByteOffset());
    }
}
