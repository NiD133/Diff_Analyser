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

public class JsonWriteContext_ESTestTest38 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext(dupDetector0);
        JsonWriteContext jsonWriteContext1 = jsonWriteContext0.createChildObjectContext();
        assertNotNull(jsonWriteContext1);
        jsonWriteContext1.writeFieldName((String) null);
        jsonWriteContext1.writeValue();
        jsonWriteContext1.writeFieldName((String) null);
        int int0 = jsonWriteContext1.writeValue();
        assertEquals(2, jsonWriteContext1.getEntryCount());
        assertEquals(2, int0);
    }
}
