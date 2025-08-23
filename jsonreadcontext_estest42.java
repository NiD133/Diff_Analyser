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

public class JsonReadContext_ESTestTest42 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test41() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(dupDetector0);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildObjectContext(1, (-1694));
        assertEquals("Object", jsonReadContext1.typeDesc());
        assertNotNull(jsonReadContext1);
        JsonReadContext jsonReadContext2 = jsonReadContext0.createChildArrayContext(1, (-1526));
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals(0, jsonReadContext2.getEntryCount());
        assertTrue(jsonReadContext2.inArray());
        assertEquals(1, jsonReadContext2.getNestingDepth());
        assertSame(jsonReadContext2, jsonReadContext1);
    }
}
