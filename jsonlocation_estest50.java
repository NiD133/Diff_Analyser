package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest50 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        long long0 = jsonLocation0.getByteOffset();
        assertEquals((-1L), long0);
    }
}
