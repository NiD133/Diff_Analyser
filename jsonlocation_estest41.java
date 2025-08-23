package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest41 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        boolean boolean0 = jsonLocation0.equals((Object) null);
        assertFalse(boolean0);
    }
}
