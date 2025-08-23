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

public class JsonReadContext_ESTestTest33 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test32() throws Throwable {
        JsonReadContext jsonReadContext0 = new JsonReadContext((JsonReadContext) null, (-2325), (DupDetector) null, 73, 1473, (-29));
        JsonReadContext jsonReadContext1 = new JsonReadContext(jsonReadContext0, (DupDetector) null, (-29), (-1754), (-2786));
        JsonReadContext jsonReadContext2 = jsonReadContext1.clearAndGetParent();
        assertEquals((-2324), jsonReadContext1.getNestingDepth());
        assertEquals(0, jsonReadContext1.getEntryCount());
        assertEquals(0, jsonReadContext2.getEntryCount());
        assertNotNull(jsonReadContext2);
        assertNotSame(jsonReadContext2, jsonReadContext1);
        assertEquals("?", jsonReadContext2.getTypeDesc());
        assertEquals("?", jsonReadContext1.getTypeDesc());
    }
}
