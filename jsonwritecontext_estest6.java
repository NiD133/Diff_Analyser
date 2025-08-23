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

public class JsonWriteContext_ESTestTest6 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext();
        Object object0 = new Object();
        JsonWriteContext jsonWriteContext1 = jsonWriteContext0.reset(0, object0);
        assertEquals(0, jsonWriteContext0.getEntryCount());
        assertEquals(0, jsonWriteContext1.getNestingDepth());
        assertTrue(jsonWriteContext0.inRoot());
    }
}
