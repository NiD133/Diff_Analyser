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

public class JsonReadContext_ESTestTest18 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext((DupDetector) null);
        JsonReadContext jsonReadContext1 = new JsonReadContext(jsonReadContext0, (DupDetector) null, 2, 2, 1);
        JsonReadContext jsonReadContext2 = new JsonReadContext(jsonReadContext1, (DupDetector) null, 1, 2, 0);
        JsonReadContext jsonReadContext3 = jsonReadContext2.getParent();
        assertEquals(0, jsonReadContext3.getEntryCount());
        assertNotNull(jsonReadContext3);
        assertEquals(0, jsonReadContext0.getEntryCount());
        assertEquals(1, jsonReadContext3.getNestingDepth());
        assertEquals(2, jsonReadContext2.getNestingDepth());
        assertEquals("OBJECT", jsonReadContext3.getTypeDesc());
        assertEquals("ROOT", jsonReadContext0.getTypeDesc());
        assertFalse(jsonReadContext2.inObject());
    }
}
