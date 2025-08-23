package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest44 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        JsonLocation jsonLocation0 = new JsonLocation((Object) null, 62, 500, (-2898), (-26));
        String string0 = jsonLocation0.toString();
        assertEquals((-26), jsonLocation0.getColumnNr());
        assertEquals(500L, jsonLocation0.getCharOffset());
        assertEquals("[Source: UNKNOWN; byte offset: #62]", string0);
        assertEquals((-2898), jsonLocation0.getLineNr());
    }
}
