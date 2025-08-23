package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest32 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        ContentReference contentReference0 = ContentReference.rawReference(true, (Object) null);
        StringBuilder stringBuilder0 = new StringBuilder();
        JsonLocation jsonLocation0 = new JsonLocation(contentReference0, (long) (-3036), 500, (-1291));
        jsonLocation0.appendOffsetDescription(stringBuilder0);
        assertEquals("line: 500, column: UNKNOWN", stringBuilder0.toString());
        assertEquals((-1L), jsonLocation0.getByteOffset());
    }
}
