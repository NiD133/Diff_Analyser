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

public class JsonWriteContext_ESTestTest44 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test43() throws Throwable {
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext();
        JsonWriteContext jsonWriteContext1 = jsonWriteContext0.createChildArrayContext();
        assertNotNull(jsonWriteContext1);
        assertEquals("Array", jsonWriteContext1.typeDesc());
        PipedInputStream pipedInputStream0 = new PipedInputStream();
        DataInputStream dataInputStream0 = new DataInputStream(pipedInputStream0);
        JsonWriteContext jsonWriteContext2 = jsonWriteContext0.createChildObjectContext((Object) dataInputStream0);
        assertSame(jsonWriteContext2, jsonWriteContext1);
        assertEquals(1, jsonWriteContext2.getNestingDepth());
        assertEquals(0, jsonWriteContext0.getEntryCount());
        assertEquals("OBJECT", jsonWriteContext2.getTypeDesc());
        assertFalse(jsonWriteContext0.inArray());
        assertEquals(0, jsonWriteContext2.getEntryCount());
    }
}