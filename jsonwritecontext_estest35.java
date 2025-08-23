package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.filter.FilteringGeneratorDelegate;
import com.fasterxml.jackson.core.filter.TokenFilter;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.StringWriter;
import java.io.Writer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class JsonWriteContext_ESTestTest35 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext(dupDetector0);
        JsonWriteContext jsonWriteContext1 = jsonWriteContext0.createChildObjectContext();
        jsonWriteContext1.writeFieldName("root");
        jsonWriteContext1.writeValue();
        try {
            jsonWriteContext1.writeFieldName("root");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            //
            // Duplicate field 'root'
            //
            verifyException("com.fasterxml.jackson.core.json.JsonWriteContext", e);
        }
    }
}
