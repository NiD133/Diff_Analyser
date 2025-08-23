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

public class JsonReadContext_ESTestTest38 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        JsonFactoryBuilder jsonFactoryBuilder0 = new JsonFactoryBuilder();
        JsonFactory jsonFactory0 = new JsonFactory(jsonFactoryBuilder0);
        JsonParser jsonParser0 = jsonFactory0.createNonBlockingByteBufferParser();
        DupDetector dupDetector0 = DupDetector.rootDetector(jsonParser0);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(dupDetector0);
        jsonReadContext0.setCurrentName("JSON");
        try {
            jsonReadContext0.setCurrentName("JSON");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Duplicate field 'JSON'
            //  at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 1]
            //
            verifyException("com.fasterxml.jackson.core.json.JsonReadContext", e);
        }
    }
}
