package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest14 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        Object object0 = new Object();
        JsonLocation jsonLocation0 = new JsonLocation(object0, 0L, 0L, 0, 0);
        Object object1 = jsonLocation0.getSourceRef();
        assertNotNull(object1);
        assertEquals(0, jsonLocation0.getColumnNr());
        assertEquals(0, jsonLocation0.getLineNr());
    }
}
