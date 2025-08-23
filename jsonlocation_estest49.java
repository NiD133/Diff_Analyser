package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.io.ContentReference;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonLocation_ESTestTest49 extends JsonLocation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        ContentReference contentReference0 = ContentReference.redacted();
        JsonLocation jsonLocation0 = new JsonLocation((Object) contentReference0, (-578L), 500, 500);
        ContentReference contentReference1 = jsonLocation0.contentReference();
        assertNotNull(contentReference1);
        assertEquals((-578L), jsonLocation0.getCharOffset());
        assertEquals(500, jsonLocation0.getColumnNr());
        assertSame(contentReference1, contentReference0);
        assertEquals((-1L), jsonLocation0.getByteOffset());
        assertEquals(500, jsonLocation0.getLineNr());
    }
}
