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

public class JsonReadContext_ESTestTest22 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(2783, 2783, (DupDetector) null);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildArrayContext(484, 2783);
        JsonReadContext jsonReadContext2 = jsonReadContext1.getParent();
        assertEquals(0, jsonReadContext2.getNestingDepth());
        assertNotNull(jsonReadContext2);
        assertTrue(jsonReadContext1.inArray());
        assertEquals(1, jsonReadContext1.getNestingDepth());
        assertFalse(jsonReadContext2.inArray());
        assertEquals(0, jsonReadContext2.getEntryCount());
    }
}
