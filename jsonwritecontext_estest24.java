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

public class JsonWriteContext_ESTestTest24 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext();
        JsonWriteContext jsonWriteContext1 = jsonWriteContext0.createChildObjectContext();
        Object object0 = new Object();
        JsonWriteContext jsonWriteContext2 = jsonWriteContext1.createChildArrayContext(object0);
        JsonWriteContext jsonWriteContext3 = jsonWriteContext2.clearAndGetParent();
        assertEquals(2, jsonWriteContext2.getNestingDepth());
        assertEquals(0, jsonWriteContext2.getEntryCount());
        assertEquals(0, jsonWriteContext3.getEntryCount());
        assertTrue(jsonWriteContext3.inObject());
        assertNotNull(jsonWriteContext3);
        assertTrue(jsonWriteContext2.inArray());
        assertTrue(jsonWriteContext0.inRoot());
    }
}
