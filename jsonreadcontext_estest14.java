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

public class JsonReadContext_ESTestTest14 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(0, 1, dupDetector0);
        JsonLocation jsonLocation0 = jsonReadContext0.getStartLocation((Object) null);
        assertEquals((-1L), jsonLocation0.getCharOffset());
        assertEquals(1, jsonLocation0.getColumnNr());
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals(0, jsonReadContext0.getEntryCount());
        assertEquals(0, jsonLocation0.getLineNr());
        assertEquals("ROOT", jsonReadContext0.getTypeDesc());
    }
}
