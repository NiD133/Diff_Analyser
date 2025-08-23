package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest29 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        JsonLocation jsonLocation0 = JsonLocation.NA;
        Object object0 = jsonLocation0.getSourceRef();
        StringBuilder stringBuilder0 = new StringBuilder("byte offset: #UNKNOWN");
        JsonLocation jsonLocation1 = new JsonLocation(object0, (-1L), 2, (-4651));
        jsonLocation1.appendOffsetDescription(stringBuilder0);
        assertEquals("byte offset: #UNKNOWNline: 2", stringBuilder0.toString());
    }
}