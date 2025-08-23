package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest16 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        int int0 = jsonLocation0.getColumnNr();
        assertEquals((-1), int0);
    }
}
