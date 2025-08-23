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

public class JsonReadContext_ESTestTest13 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext((DupDetector) null);
        boolean boolean0 = jsonReadContext0.hasCurrentName();
        assertFalse(boolean0);
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals("root", jsonReadContext0.typeDesc());
        assertEquals(0, jsonReadContext0.getEntryCount());
    }
}
