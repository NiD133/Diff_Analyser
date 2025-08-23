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

public class JsonReadContext_ESTestTest15 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(11, 11, (DupDetector) null);
        JsonLocation jsonLocation0 = jsonReadContext0.getStartLocation(jsonReadContext0);
        assertEquals((-1L), jsonLocation0.getCharOffset());
        assertEquals(0, jsonReadContext0.getEntryCount());
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertTrue(jsonReadContext0.inRoot());
        assertEquals(11, jsonLocation0.getLineNr());
        assertEquals(11, jsonLocation0.getColumnNr());
    }
}
