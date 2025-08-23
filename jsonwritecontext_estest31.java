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

public class JsonWriteContext_ESTestTest31 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext jsonWriteContext0 = new JsonWriteContext((-1), (JsonWriteContext) null, dupDetector0, (Object) null);
        assertFalse(jsonWriteContext0.inObject());
        jsonWriteContext0.reset(2);
        assertEquals("OBJECT", jsonWriteContext0.getTypeDesc());
    }
}
