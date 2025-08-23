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

public class JsonReadContext_ESTestTest32 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext((-1735), (-1735), (DupDetector) null);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildArrayContext((-1735), (-1));
        JsonReadContext jsonReadContext2 = jsonReadContext1.createChildObjectContext(3270, (-1735));
        JsonReadContext jsonReadContext3 = jsonReadContext2.clearAndGetParent();
        assertEquals(2, jsonReadContext2.getNestingDepth());
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertNotNull(jsonReadContext3);
        assertFalse(jsonReadContext3.inObject());
        assertEquals("root", jsonReadContext0.typeDesc());
        assertTrue(jsonReadContext2.inObject());
        assertEquals(0, jsonReadContext3.getEntryCount());
    }
}
