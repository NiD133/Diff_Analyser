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

public class JsonReadContext_ESTestTest11 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext((DupDetector) null);
        JsonReadContext jsonReadContext1 = new JsonReadContext(jsonReadContext0, (DupDetector) null, (-2792), 2, (-380));
        Object object0 = new Object();
        ErrorReportConfiguration errorReportConfiguration0 = ErrorReportConfiguration.defaults();
        ContentReference contentReference0 = ContentReference.construct(false, object0, errorReportConfiguration0);
        JsonLocation jsonLocation0 = jsonReadContext1.startLocation(contentReference0);
        assertEquals((-380), jsonLocation0.getColumnNr());
        assertEquals(1, jsonReadContext1.getNestingDepth());
        assertEquals(2, jsonLocation0.getLineNr());
        assertFalse(jsonReadContext1.hasCurrentIndex());
        assertEquals(0, jsonReadContext0.getEntryCount());
        assertEquals("?", jsonReadContext1.typeDesc());
        assertEquals("root", jsonReadContext0.typeDesc());
        assertEquals((-1L), jsonLocation0.getCharOffset());
    }
}