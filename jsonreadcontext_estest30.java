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

public class JsonReadContext_ESTestTest30 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(11, 11, (DupDetector) null);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildObjectContext(11, 11);
        JsonReadContext jsonReadContext2 = jsonReadContext1.createChildObjectContext(1, 11);
        JsonReadContext jsonReadContext3 = jsonReadContext2.clearAndGetParent();
        assertEquals("ROOT", jsonReadContext0.getTypeDesc());
        assertEquals(0, jsonReadContext3.getEntryCount());
        assertEquals("Object", jsonReadContext3.typeDesc());
        assertEquals(2, jsonReadContext2.getNestingDepth());
        assertEquals(1, jsonReadContext3.getNestingDepth());
        assertNotNull(jsonReadContext3);
    }
}