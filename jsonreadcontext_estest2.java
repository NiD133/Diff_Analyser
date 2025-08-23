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

public class JsonReadContext_ESTestTest2 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(11, 11, (DupDetector) null);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildObjectContext(11, 11);
        assertEquals("Object", jsonReadContext1.typeDesc());
        assertEquals(0, jsonReadContext1.getEntryCount());
        JsonReadContext jsonReadContext2 = jsonReadContext0.createChildObjectContext(1, 0);
        assertEquals("ROOT", jsonReadContext0.getTypeDesc());
        assertSame(jsonReadContext2, jsonReadContext1);
        assertNotNull(jsonReadContext2);
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals(1, jsonReadContext2.getNestingDepth());
    }
}
