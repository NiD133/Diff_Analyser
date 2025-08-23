package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest30 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        JsonLocation jsonLocation0 = new JsonLocation((Object) null, (-219L), 314, 314);
        StringBuilder stringBuilder0 = new StringBuilder("; ");
        jsonLocation0.appendOffsetDescription(stringBuilder0);
        assertEquals("; line: 314, column: 314", stringBuilder0.toString());
        assertEquals(314, jsonLocation0.getColumnNr());
    }
}
