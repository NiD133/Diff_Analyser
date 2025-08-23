package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest28 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        JsonLocation jsonLocation0 = new JsonLocation((Object) null, 0L, 1840L, (-201), 93);
        StringBuilder stringBuilder0 = new StringBuilder("_K|FUenM:'d");
        jsonLocation0.appendOffsetDescription(stringBuilder0);
        assertEquals("_K|FUenM:'dbyte offset: #0", stringBuilder0.toString());
    }
}
