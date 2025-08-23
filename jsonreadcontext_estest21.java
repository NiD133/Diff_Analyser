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

public class JsonReadContext_ESTestTest21 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(dupDetector0);
        JsonReadContext jsonReadContext1 = new JsonReadContext(jsonReadContext0, (-1476), dupDetector0, (-1476), 92, 2);
        JsonReadContext jsonReadContext2 = jsonReadContext1.createChildObjectContext(92, 1);
        JsonReadContext jsonReadContext3 = jsonReadContext2.getParent();
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertTrue(jsonReadContext2.inObject());
        assertEquals((-1475), jsonReadContext2.getNestingDepth());
        assertNotNull(jsonReadContext3);
        assertFalse(jsonReadContext3.inObject());
        assertEquals(0, jsonReadContext3.getEntryCount());
        assertTrue(jsonReadContext0.inRoot());
    }
}
