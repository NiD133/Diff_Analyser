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

public class JsonWriteContext_ESTestTest23 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext();
        JsonWriteContext jsonWriteContext1 = JsonWriteContext.createRootContext();
        jsonWriteContext1._child = jsonWriteContext0;
        assertEquals("ROOT", jsonWriteContext0.getTypeDesc());
        JsonWriteContext jsonWriteContext2 = jsonWriteContext1.createChildArrayContext();
        assertTrue(jsonWriteContext2.inArray());
        assertSame(jsonWriteContext2, jsonWriteContext0);
        assertEquals(0, jsonWriteContext1.getCurrentIndex());
        assertFalse(jsonWriteContext2.hasCurrentIndex());
    }
}
