package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest9 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Object object0 = new Object();
        JsonLocation jsonLocation0 = new JsonLocation(object0, 3981, 2, 2, 0);
        String string0 = jsonLocation0.toString();
        assertEquals(2L, jsonLocation0.getCharOffset());
        assertEquals("[Source: (Object); line: 2]", string0);
        assertEquals(0, jsonLocation0.getColumnNr());
        assertEquals(3981L, jsonLocation0.getByteOffset());
    }
}