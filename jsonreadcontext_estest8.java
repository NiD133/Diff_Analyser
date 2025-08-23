package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.ContentReference;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonReadContext_ESTestTest8 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(0, 50000, (DupDetector) null);
        ContentReference contentReference0 = ContentReference.unknown();
        JsonLocation jsonLocation0 = jsonReadContext0.startLocation(contentReference0);
        assertEquals(0, jsonLocation0.getLineNr());
        assertEquals(0, jsonReadContext0.getEntryCount());
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals((-1L), jsonLocation0.getCharOffset());
        assertTrue(jsonReadContext0.inRoot());
        assertEquals(50000, jsonLocation0.getColumnNr());
    }
}
