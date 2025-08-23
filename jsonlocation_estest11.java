package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest11 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        ContentReference contentReference0 = ContentReference.rawReference(true, (Object) null);
        JsonLocation jsonLocation0 = new JsonLocation(contentReference0, (-2758L), 0L, (-3036), 0);
        int int0 = jsonLocation0.getColumnNr();
        assertEquals((-2758L), jsonLocation0.getByteOffset());
        assertEquals(0, int0);
        assertEquals(0L, jsonLocation0.getCharOffset());
        assertEquals((-3036), jsonLocation0.getLineNr());
    }
}
