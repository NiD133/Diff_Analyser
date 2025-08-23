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

public class JsonWriteContext_ESTestTest27 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext();
        Object object0 = new Object();
        JsonWriteContext jsonWriteContext1 = new JsonWriteContext(65599, jsonWriteContext0, (DupDetector) null, object0);
        JsonWriteContext jsonWriteContext2 = new JsonWriteContext(5, jsonWriteContext1, (DupDetector) null);
        JsonWriteContext jsonWriteContext3 = jsonWriteContext2.clearAndGetParent();
        assertEquals(0, jsonWriteContext2.getEntryCount());
        assertEquals(0, jsonWriteContext3.getEntryCount());
        assertNotSame(jsonWriteContext3, jsonWriteContext2);
        assertEquals(2, jsonWriteContext2.getNestingDepth());
        assertEquals("?", jsonWriteContext3.getTypeDesc());
        assertNotNull(jsonWriteContext3);
        assertEquals("ROOT", jsonWriteContext0.getTypeDesc());
        assertFalse(jsonWriteContext2.inRoot());
    }
}
