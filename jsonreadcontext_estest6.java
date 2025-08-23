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

public class JsonReadContext_ESTestTest6 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(dupDetector0);
        JsonReadContext jsonReadContext1 = new JsonReadContext(jsonReadContext0, 1, dupDetector0, 1, 1, 1);
        JsonReadContext jsonReadContext2 = jsonReadContext1.withDupDetector(dupDetector0);
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertFalse(jsonReadContext0.inArray());
        assertEquals(1, jsonReadContext2.getNestingDepth());
        assertEquals(0, jsonReadContext2.getEntryCount());
        assertTrue(jsonReadContext2.inArray());
    }
}
