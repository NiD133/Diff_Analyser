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

public class JsonWriteContext_ESTestTest41 extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        JsonWriteContext jsonWriteContext0 = JsonWriteContext.createRootContext();
        JsonWriteContext jsonWriteContext1 = jsonWriteContext0.createChildObjectContext();
        assertNotNull(jsonWriteContext1);
        assertTrue(jsonWriteContext1.inObject());
        JsonFactory jsonFactory0 = new JsonFactory();
        StringWriter stringWriter0 = new StringWriter();
        JsonGenerator jsonGenerator0 = jsonFactory0.createGenerator((Writer) stringWriter0);
        TokenFilter tokenFilter0 = TokenFilter.INCLUDE_ALL;
        TokenFilter.Inclusion tokenFilter_Inclusion0 = TokenFilter.Inclusion.INCLUDE_NON_NULL;
        FilteringGeneratorDelegate filteringGeneratorDelegate0 = new FilteringGeneratorDelegate(jsonGenerator0, tokenFilter0, tokenFilter_Inclusion0, false);
        JsonWriteContext jsonWriteContext2 = jsonWriteContext0.createChildArrayContext((Object) filteringGeneratorDelegate0);
        assertEquals(1, jsonWriteContext2.getNestingDepth());
        assertFalse(jsonWriteContext2.inObject());
        assertSame(jsonWriteContext2, jsonWriteContext1);
    }
}
