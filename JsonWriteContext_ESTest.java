package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.filter.FilteringGeneratorDelegate;
import com.fasterxml.jackson.core.filter.TokenFilter;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.StringWriter;
import java.io.Writer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonWriteContext_ESTest extends JsonWriteContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testWriteValueInChildArrayContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = rootContext.createChildArrayContext((Object) "");
        arrayContext.writeValue();
        arrayContext.writeValue();
        int status = arrayContext.writeValue();
        assertEquals(2, arrayContext.getCurrentIndex());
        assertEquals(1, status);
    }

    @Test(timeout = 4000)
    public void testResetRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.reset(50000, null);
        int status = rootContext.writeValue();
        assertEquals(1, rootContext.getEntryCount());
        assertEquals(0, status);
    }

    @Test(timeout = 4000)
    public void testWriteFieldNameInRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("ROOT", rootContext.getTypeDesc());

        rootContext.reset(240);
        int status = rootContext.writeFieldName("");
        assertFalse(rootContext.inRoot());
        assertEquals(4, status);
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextWithDupDetector() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = new JsonWriteContext((byte) (-16), null, dupDetector);
        Object value = new Object();
        JsonWriteContext objectContext = rootContext.createChildObjectContext(value);
        assertNotNull(objectContext);

        objectContext.writeFieldName("I=b]y/#@HKfkg#;");
        objectContext.withDupDetector(dupDetector);
        assertTrue(objectContext.hasCurrentName());
    }

    @Test(timeout = 4000)
    public void testCreateChildArrayContextWithoutDupDetector() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(null);
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        JsonWriteContext arrayContextWithDup = arrayContext.withDupDetector(null);
        assertEquals(1, arrayContextWithDup.getNestingDepth());
        assertTrue(arrayContextWithDup.inArray());
        assertEquals(0, arrayContextWithDup.getEntryCount());
        assertFalse(rootContext.inArray());
    }

    @Test(timeout = 4000)
    public void testResetRootContextWithObject() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object value = new Object();
        JsonWriteContext resetContext = rootContext.reset(0, value);
        assertEquals(0, rootContext.getEntryCount());
        assertEquals(0, resetContext.getNestingDepth());
        assertTrue(rootContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testResetRootContextToObject() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(null);
        assertFalse(rootContext.inObject());

        rootContext.reset(2, null);
        assertEquals("OBJECT", rootContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testResetRootContextToNonRoot() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertTrue(rootContext.inRoot());

        JsonWriteContext resetContext = rootContext.reset(1, null);
        assertFalse(resetContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextAndReset() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object value = new Object();
        JsonWriteContext objectContext = rootContext.createChildObjectContext(value);
        assertEquals(0, objectContext.getEntryCount());
        assertNotNull(objectContext);
        assertEquals("OBJECT", objectContext.getTypeDesc());

        JsonWriteContext resetContext = rootContext._child.reset(2);
        assertEquals(0, rootContext.getEntryCount());
        assertEquals(1, resetContext.getNestingDepth());
        assertFalse(resetContext.hasCurrentIndex());
        assertEquals("ROOT", rootContext.getTypeDesc());
        assertEquals("OBJECT", resetContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testHasCurrentNameAfterReset() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext resetContext = rootContext.reset(1);
        resetContext._currentName = "{?4P :";
        boolean hasCurrentName = rootContext.hasCurrentName();
        assertTrue(hasCurrentName);
    }

    @Test(timeout = 4000)
    public void testHasNoCurrentNameInRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        boolean hasCurrentName = rootContext.hasCurrentName();
        assertFalse(hasCurrentName);
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());
        assertTrue(rootContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testGetParentOfRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext parentContext = rootContext.getParent();
        assertEquals(0, rootContext.getNestingDepth());
        assertNull(parentContext);
        assertEquals(0, rootContext.getEntryCount());
        assertTrue(rootContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testCreateChildArrayContextInObjectContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object value = new Object();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        JsonWriteContext arrayContext = objectContext.createChildArrayContext(value);
        JsonWriteContext parentContext = arrayContext.getParent();
        assertNotNull(parentContext);
        assertEquals(0, parentContext.getEntryCount());
        assertEquals(0, arrayContext.getEntryCount());
        assertEquals(2, arrayContext.getNestingDepth());
        assertTrue(parentContext.inObject());
        assertEquals("ARRAY", arrayContext.getTypeDesc());
        assertEquals("ROOT", rootContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextWithCurrentName() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext._currentName = "";
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        JsonWriteContext parentContext = objectContext.getParent();
        assertNotNull(parentContext);
        assertTrue(parentContext.inRoot());
        assertEquals("OBJECT", objectContext.getTypeDesc());
        assertEquals(0, parentContext.getEntryCount());
        assertEquals(1, objectContext.getNestingDepth());
    }

    @Test(timeout = 4000)
    public void testWriteValueInRootAndObjectContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.writeValue();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        rootContext.writeValue();
        objectContext.getParent();
        assertEquals(1, rootContext.getCurrentIndex());
        assertTrue(rootContext.hasCurrentIndex());
    }

    @Test(timeout = 4000)
    public void testCreateRootContextWithDupDetector() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        DupDetector retrievedDupDetector = rootContext.getDupDetector();
        assertEquals("ROOT", rootContext.getTypeDesc());
        assertNotNull(retrievedDupDetector);
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());
    }

    @Test(timeout = 4000)
    public void testGetCurrentValueInRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object value = new Object();
        rootContext._currentValue = value;
        rootContext.getCurrentValue();
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());
        assertEquals("ROOT", rootContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testGetCurrentNameInRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.getCurrentName();
        assertEquals(0, rootContext.getEntryCount());
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals("ROOT", rootContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testGetCurrentNameAfterResetWithObject() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("ROOT", rootContext.getTypeDesc());

        JsonWriteContext resetContext = rootContext.reset(1);
        resetContext._currentName = "{?4P :";
        rootContext.getCurrentName();
        assertFalse(rootContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testResetWithNegativeTypeAndObject() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("ROOT", rootContext.getTypeDesc());

        Object value = new Object();
        JsonWriteContext resetContext = rootContext.reset(-248, value);
        resetContext._currentName = "v;grA ol7R@";
        resetContext._currentName = "";
        rootContext.getCurrentName();
        assertFalse(rootContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextInArrayContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object value = new Object();
        JsonWriteContext arrayContext = rootContext.createChildArrayContext(value);
        arrayContext._child = rootContext;
        assertFalse(arrayContext._child.inObject());

        arrayContext.createChildObjectContext(value);
        assertEquals("Object", rootContext.typeDesc());
        assertFalse(arrayContext.hasCurrentIndex());
    }

    @Test(timeout = 4000)
    public void testCreateChildArrayContextInRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext._child = rootContext;
        assertFalse(rootContext._child.inArray());

        rootContext.createChildArrayContext(rootContext);
        assertEquals("ARRAY", rootContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testCreateChildArrayContextInRootContextWithDupDetector() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext anotherRootContext = JsonWriteContext.createRootContext();
        anotherRootContext._child = rootContext;
        assertEquals("ROOT", rootContext.getTypeDesc());

        JsonWriteContext arrayContext = anotherRootContext.createChildArrayContext();
        assertTrue(arrayContext.inArray());
        assertSame(arrayContext, rootContext);
        assertEquals(0, anotherRootContext.getCurrentIndex());
        assertFalse(arrayContext.hasCurrentIndex());
    }

    @Test(timeout = 4000)
    public void testClearAndGetParentInArrayContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        Object value = new Object();
        JsonWriteContext arrayContext = objectContext.createChildArrayContext(value);
        JsonWriteContext parentContext = arrayContext.clearAndGetParent();
        assertEquals(2, arrayContext.getNestingDepth());
        assertEquals(0, arrayContext.getEntryCount());
        assertEquals(0, parentContext.getEntryCount());
        assertTrue(parentContext.inObject());
        assertNotNull(parentContext);
        assertTrue(arrayContext.inArray());
        assertTrue(rootContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testClearAndGetParentInNestedArrayContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        JsonWriteContext nestedArrayContext = arrayContext.createChildArrayContext();
        JsonWriteContext parentContext = nestedArrayContext.clearAndGetParent();
        assertNotNull(parentContext);
        assertEquals(2, nestedArrayContext.getNestingDepth());
        assertEquals(0, parentContext.getEntryCount());
        assertEquals("Array", parentContext.typeDesc());
        assertTrue(rootContext.inRoot());
        assertEquals(1, parentContext.getNestingDepth());
    }

    @Test(timeout = 4000)
    public void testClearAndGetParentInArrayContextWithCurrentName() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext._currentName = "";
        Object value = new Object();
        JsonWriteContext arrayContext = rootContext.createChildArrayContext(value);
        assertNotNull(arrayContext);

        JsonWriteContext parentContext = arrayContext.clearAndGetParent();
        assertEquals("Array", arrayContext.typeDesc());
        assertEquals(0, arrayContext.getEntryCount());
        assertEquals("ROOT", parentContext.getTypeDesc());
        assertEquals(0, parentContext.getEntryCount());
        assertNotNull(parentContext);
        assertEquals(1, arrayContext.getNestingDepth());
    }

    @Test(timeout = 4000)
    public void testClearAndGetParentInCustomContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object value = new Object();
        JsonWriteContext customContext = new JsonWriteContext(65599, rootContext, null, value);
        JsonWriteContext anotherCustomContext = new JsonWriteContext(5, customContext, null);
        JsonWriteContext parentContext = anotherCustomContext.clearAndGetParent();
        assertEquals(0, anotherCustomContext.getEntryCount());
        assertEquals(0, parentContext.getEntryCount());
        assertNotSame(parentContext, anotherCustomContext);
        assertEquals(2, anotherCustomContext.getNestingDepth());
        assertEquals("?", parentContext.getTypeDesc());
        assertNotNull(parentContext);
        assertEquals("ROOT", rootContext.getTypeDesc());
        assertFalse(anotherCustomContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testWriteValueInRootAndObjectContextWithClear() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.writeValue();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        rootContext.writeValue();
        objectContext.clearAndGetParent();
        assertEquals(1, rootContext.getCurrentIndex());
        assertEquals(2, rootContext.getEntryCount());
    }

    @Test(timeout = 4000)
    public void testWriteFieldNameAndValueWithDupDetector() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.writeFieldName(": was expecting closing '*/' for comment");
        objectContext.writeValue();
        try {
            objectContext.writeFieldName(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.fasterxml.jackson.core.json.DupDetector", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteDuplicateFieldName() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.writeFieldName("root");
        objectContext.writeValue();
        try {
            objectContext.writeFieldName("root");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.fasterxml.jackson.core.json.JsonWriteContext", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteDuplicateFieldNameWithJsonGeneratorDelegate() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonGeneratorDelegate jsonGeneratorDelegate = new JsonGeneratorDelegate(null, false);
        DupDetector dupDetector = DupDetector.rootDetector(jsonGeneratorDelegate);
        JsonWriteContext anotherRootContext = JsonWriteContext.createRootContext(dupDetector);
        rootContext._child = anotherRootContext;
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.writeFieldName("/");
        objectContext.writeValue();
        try {
            objectContext.writeFieldName("/");
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("com.fasterxml.jackson.core.json.JsonWriteContext", e);
        }
    }

    @Test(timeout = 4000)
    public void testWriteFieldNameTwiceInObjectContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        objectContext.writeFieldName("NRN=M&Q");
        int status = objectContext.writeFieldName("):<z");
        assertTrue(objectContext.hasCurrentName());
        assertEquals(4, status);
    }

    @Test(timeout = 4000)
    public void testWriteNullFieldNameAndValueInObjectContext() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        assertNotNull(objectContext);

        objectContext.writeFieldName(null);
        objectContext.writeValue();
        objectContext.writeFieldName(null);
        int status = objectContext.writeValue();
        assertEquals(2, objectContext.getEntryCount());
        assertEquals(2, status);
    }

    @Test(timeout = 4000)
    public void testWriteNullFieldNameInRootContext() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        int status = rootContext.writeFieldName(null);
        assertEquals(4, status);
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());
        assertEquals("root", rootContext.typeDesc());
    }

    @Test(timeout = 4000)
    public void testCreateChildArrayContextWithDupDetector() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext arrayContext = rootContext.createChildArrayContext(dupDetector);
        assertEquals(1, arrayContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());
        assertEquals(0, arrayContext.getEntryCount());
        assertNotNull(arrayContext);
        assertTrue(rootContext.inRoot());
        assertEquals("ARRAY", arrayContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextAndArrayContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        assertNotNull(objectContext);
        assertTrue(objectContext.inObject());

        JsonFactory jsonFactory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator((Writer) stringWriter);
        TokenFilter tokenFilter = TokenFilter.INCLUDE_ALL;
        TokenFilter.Inclusion tokenFilterInclusion = TokenFilter.Inclusion.INCLUDE_NON_NULL;
        FilteringGeneratorDelegate filteringGeneratorDelegate = new FilteringGeneratorDelegate(jsonGenerator, tokenFilter, tokenFilterInclusion, false);
        JsonWriteContext arrayContext = rootContext.createChildArrayContext(filteringGeneratorDelegate);
        assertEquals(1, arrayContext.getNestingDepth());
        assertFalse(arrayContext.inObject());
        assertSame(arrayContext, objectContext);
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextInArrayContextWithDupDetector() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = new JsonWriteContext((byte) (-16), null, dupDetector);
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        assertNotNull(arrayContext);

        Object value = new Object();
        JsonWriteContext objectContext = arrayContext.createChildObjectContext(value);
        JsonWriteContext parentContext = objectContext.getParent();
        assertFalse(rootContext.inArray());
        assertEquals(0, parentContext.getEntryCount());
        assertNotNull(parentContext);
        assertEquals(2, objectContext.getNestingDepth());
        assertTrue(parentContext.inArray());
        assertEquals(0, objectContext.getEntryCount());
        assertEquals("Object", objectContext.typeDesc());
    }

    @Test(timeout = 4000)
    public void testResetWithDupDetector() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext contextWithDup = rootContext.withDupDetector(dupDetector);
        assertTrue(contextWithDup.inRoot());

        Object value = new Object();
        JsonWriteContext resetContext = contextWithDup.reset(-940, value);
        assertEquals("?", resetContext.getTypeDesc());
    }

    @Test(timeout = 4000)
    public void testCreateChildArrayContextAndObjectContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        assertNotNull(arrayContext);
        assertEquals("Array", arrayContext.typeDesc());

        PipedInputStream pipedInputStream = new PipedInputStream();
        JsonWriteContext objectContext = rootContext.createChildObjectContext(pipedInputStream);
        assertSame(objectContext, arrayContext);
        assertEquals(1, objectContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());
        assertEquals("OBJECT", objectContext.getTypeDesc());
        assertFalse(rootContext.inArray());
        assertEquals(0, objectContext.getEntryCount());
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextWithDupDetector() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector(null);
        JsonWriteContext rootContext = new JsonWriteContext(-1, null, dupDetector, null);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        assertEquals(0, objectContext.getEntryCount());
        assertEquals(0, rootContext.getEntryCount());
        assertEquals("OBJECT", objectContext.getTypeDesc());
        assertNotNull(objectContext);
        assertEquals("?", rootContext.typeDesc());
        assertEquals(1, objectContext.getNestingDepth());
    }

    @Test(timeout = 4000)
    public void testGetCurrentValueInCustomContext() throws Throwable {
        JsonWriteContext customContext = new JsonWriteContext(2033, null, null);
        customContext.getCurrentValue();
        assertEquals(0, customContext.getNestingDepth());
        assertEquals("?", customContext.typeDesc());
        assertEquals(0, customContext.getEntryCount());
    }

    @Test(timeout = 4000)
    public void testClearAndGetParentInRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext parentContext = rootContext.clearAndGetParent();
        assertEquals(0, rootContext.getNestingDepth());
        assertEquals(0, rootContext.getEntryCount());
        assertNull(parentContext);
        assertTrue(rootContext.inRoot());
    }

    @Test(timeout = 4000)
    public void testSetCurrentValueInRootContext() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.setCurrentValue(rootContext);
        assertEquals(0, rootContext.getEntryCount());
        assertTrue(rootContext.inRoot());
        assertEquals(0, rootContext.getNestingDepth());
    }

    @Test(timeout = 4000)
    public void testCreateChildObjectContextAndGetDupDetector() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object value = new Object();
        JsonWriteContext objectContext = rootContext.createChildObjectContext(value);
        assertEquals(0, objectContext.getEntryCount());
        assertNotNull(objectContext);
        assertEquals("OBJECT", objectContext.getTypeDesc());
        assertEquals(1, objectContext.getNestingDepth());

        rootContext._child.getDupDetector();
        assertEquals(0, rootContext.getEntryCount());
        assertEquals("root", rootContext.typeDesc());
        assertTrue(rootContext.inRoot());
    }
}