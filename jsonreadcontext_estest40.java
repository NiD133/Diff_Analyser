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

public class JsonReadContext_ESTestTest40 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(dupDetector0);
        jsonReadContext0.expectComma();
        JsonReadContext jsonReadContext1 = new JsonReadContext(jsonReadContext0, dupDetector0, 0, 1, 0);
        jsonReadContext0.expectComma();
        jsonReadContext1.getParent();
        assertEquals(2, jsonReadContext0.getEntryCount());
        assertEquals(1, jsonReadContext0.getCurrentIndex());
    }
}
