package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest8 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Object object0 = new Object();
        JsonLocation jsonLocation0 = new JsonLocation(object0, (-2650L), 0L, 3981, 2);
        String string0 = jsonLocation0.toString();
        assertEquals((-2650L), jsonLocation0.getByteOffset());
        assertEquals(0L, jsonLocation0.getCharOffset());
        assertEquals("[Source: (Object); line: 3981, column: 2]", string0);
    }
}