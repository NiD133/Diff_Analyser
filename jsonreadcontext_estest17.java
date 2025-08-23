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

public class JsonReadContext_ESTestTest17 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext((DupDetector) null);
        JsonReadContext jsonReadContext1 = jsonReadContext0.getParent();
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals(0, jsonReadContext0.getEntryCount());
        assertNull(jsonReadContext1);
        assertEquals("ROOT", jsonReadContext0.getTypeDesc());
    }
}
