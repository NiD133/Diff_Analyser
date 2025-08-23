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

public class JsonReadContext_ESTestTest41 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(dupDetector0);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildObjectContext(1, (-1694));
        assertTrue(jsonReadContext1.inObject());
        assertNotNull(jsonReadContext1);
        assertEquals(0, jsonReadContext1.getEntryCount());
        JsonReadContext jsonReadContext2 = jsonReadContext0.createChildObjectContext(778, 1);
        assertEquals(1, jsonReadContext2.getNestingDepth());
        assertSame(jsonReadContext2, jsonReadContext1);
        assertEquals("Object", jsonReadContext2.typeDesc());
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals("ROOT", jsonReadContext0.getTypeDesc());
    }
}
