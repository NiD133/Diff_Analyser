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

public class JsonReadContext_ESTestTest3 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        DupDetector dupDetector0 = DupDetector.rootDetector((JsonParser) null);
        DupDetector dupDetector1 = dupDetector0.child();
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext(dupDetector1);
        JsonReadContext jsonReadContext1 = new JsonReadContext(jsonReadContext0, (-2901), dupDetector0, (-2901), 490, 2);
        JsonReadContext jsonReadContext2 = jsonReadContext1.withDupDetector(dupDetector1);
        assertEquals(0, jsonReadContext0.getNestingDepth());
        assertEquals("ROOT", jsonReadContext0.getTypeDesc());
        assertEquals("?", jsonReadContext2.getTypeDesc());
        assertEquals((-2901), jsonReadContext2.getNestingDepth());
        assertEquals(0, jsonReadContext2.getEntryCount());
    }
}
