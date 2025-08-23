package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest18 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        JsonLocation jsonLocation0 = new JsonLocation((ContentReference) null, 2675L, 2675L, (-356), (-356));
        long long0 = jsonLocation0.getCharOffset();
        assertEquals(2675L, long0);
        assertEquals((-356), jsonLocation0.getLineNr());
        assertEquals((-356), jsonLocation0.getColumnNr());
        assertEquals(2675L, jsonLocation0.getByteOffset());
    }
}