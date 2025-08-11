package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonReadContext;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonReadContextTest extends JsonReadContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRootContextCreation() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(187, 187, dupDetector);
        JsonReadContext childContext = new JsonReadContext(rootContext, dupDetector, -499, -499, -499);

        boolean expectsComma = childContext.expectComma();

        assertEquals(1, childContext.getEntryCount());
        assertFalse(expectsComma);
    }

    @Test(timeout = 4000)
    public void testChildObjectContextCreation() throws Throwable {
        JsonReadContext rootContext = JsonReadContext.createRootContext(11, 11, null);
        JsonReadContext childContext = rootContext.createChildObjectContext(11, 11);

        assertEquals("Object", childContext.typeDesc());
        assertEquals(0, childContext.getEntryCount());

        JsonReadContext anotherChildContext = rootContext.createChildObjectContext(1, 0);
        assertEquals("ROOT", rootContext.getTypeDesc());
        assertSame(anotherChildContext, childContext);
        assertNotNull(anotherChildContext);
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(1, anotherChildContext.getNestingDepth());
    }

    @Test(timeout = 4000)
    public void testDupDetectorIntegration() throws Throwable {
        DupDetector rootDupDetector = DupDetector.rootDetector((JsonParser) null);
        DupDetector childDupDetector = rootDupDetector.child();
        JsonReadContext rootContext = JsonReadContext.createRootContext(childDupDetector);
        JsonReadContext childContext = new JsonReadContext(rootContext, -2901, rootDupDetector, -2901, 490, 2);
        JsonReadContext contextWithDupDetector = childContext.withDupDetector(childDupDetector);

        assertEquals(0, rootContext.getNestingDepth());
        assertEquals("ROOT", rootContext.getTypeDesc());
        assertEquals("?", contextWithDupDetector.getTypeDesc());
        assertEquals(-2901, contextWithDupDetector.getNestingDepth());
        assertEquals(0, contextWithDupDetector.getEntryCount());
    }

    @Test(timeout = 4000)
    public void testCurrentNameHandling() throws Throwable {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        rootContext.setCurrentName("Duplicate field '");
        boolean hasCurrentName = rootContext.hasCurrentName();

        assertTrue(hasCurrentName);
    }

    @Test(timeout = 4000)
    public void testStartLocation() throws Throwable {
        JsonReadContext rootContext = JsonReadContext.createRootContext(0, 50000, null);
        ContentReference contentReference = ContentReference.unknown();
        JsonLocation startLocation = rootContext.startLocation(contentReference);

        assertEquals(0, startLocation.getLineNr());
        assertEquals(0, rootContext.getEntryCount());
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(-1L, startLocation.getCharOffset());
        assertTrue(rootContext.inRoot());
        assertEquals(50000, startLocation.getColumnNr());
    }

    @Test(timeout = 4000)
    public void testClearAndGetParent() throws Throwable {
        JsonReadContext rootContext = JsonReadContext.createRootContext(11, 11, null);
        JsonReadContext childContext = rootContext.createChildObjectContext(11, 11);
        JsonReadContext parentContext = childContext.clearAndGetParent();

        assertEquals("ROOT", rootContext.getTypeDesc());
        assertEquals(0, parentContext.getEntryCount());
        assertEquals("Object", parentContext.typeDesc());
        assertEquals(1, parentContext.getNestingDepth());
        assertNotNull(parentContext);
    }

    @Test(timeout = 4000)
    public void testDuplicationExceptionHandling() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);
        dupDetector.isDup("np*=*,a*YZ)0");

        try {
            rootContext.setCurrentName(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.json.DupDetector", e);
        }
    }

    @Test(timeout = 4000)
    public void testDuplicateFieldException() throws Throwable {
        JsonFactoryBuilder factoryBuilder = new JsonFactoryBuilder();
        JsonFactory jsonFactory = new JsonFactory(factoryBuilder);
        JsonParser parser = jsonFactory.createNonBlockingByteBufferParser();
        DupDetector dupDetector = DupDetector.rootDetector(parser);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);
        rootContext.setCurrentName("JSON");

        try {
            rootContext.setCurrentName("JSON");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.fasterxml.jackson.core.json.JsonReadContext", e);
        }
    }
}