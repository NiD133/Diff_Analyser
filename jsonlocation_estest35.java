package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest35 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        Object object0 = new Object();
        ContentReference contentReference0 = ContentReference.rawReference(true, object0);
        JsonLocation jsonLocation0 = new JsonLocation((Object) contentReference0, (long) 0, 0, 0);
        StringBuilder stringBuilder0 = new StringBuilder(863);
        jsonLocation0.appendOffsetDescription(stringBuilder0);
        assertEquals("line: 0, column: 0", stringBuilder0.toString());
    }
}
