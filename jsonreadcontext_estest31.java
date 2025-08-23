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

public class JsonReadContext_ESTestTest31 extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test30() throws Throwable {
        JsonReadContext jsonReadContext0 = JsonReadContext.createRootContext((-2732), (-1735), (DupDetector) null);
        JsonReadContext jsonReadContext1 = jsonReadContext0.createChildObjectContext(2, 1);
        jsonReadContext0.setCurrentName("o;?KjQ~B");
        jsonReadContext1.clearAndGetParent();
        assertTrue(jsonReadContext0.hasCurrentName());
        assertFalse(jsonReadContext1.inRoot());
    }
}
