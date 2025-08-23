package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest12 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Object object0 = new Object();
        ContentReference contentReference0 = ContentReference.rawReference(true, object0);
        JsonLocation jsonLocation0 = new JsonLocation(contentReference0, (-4198L), (-4198L), (-1143), 0);
        int int0 = jsonLocation0.getLineNr();
        assertEquals((-4198L), jsonLocation0.getByteOffset());
        assertEquals(0, jsonLocation0.getColumnNr());
        assertEquals((-1143), int0);
        assertEquals((-4198L), jsonLocation0.getCharOffset());
    }
}
