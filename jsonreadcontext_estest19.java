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

public class JsonReadContext_ESTestTest19 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext((-966), (-504), dupDetector0);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildObjectContext((-1087), 2393);
        jsonReadContext0.setCurrentName("");
        jsonReadContext1.getParent();
        assertTrue(jsonReadContext0.hasCurrentName());
        assertEquals(1, jsonReadContext1.getNestingDepth());
    }
}
