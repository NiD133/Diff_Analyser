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

/**
 * Test suite for JsonReadContext functionality including context creation,
 * navigation, duplicate detection, and location tracking.
 */
public class JsonReadContextTest {

    // Constants for better readability
    private static final int DEFAULT_LINE = 1;
    private static final int DEFAULT_COLUMN = 0;
    private static final String SAMPLE_FIELD_NAME = "testField";

    /*
    /**********************************************************
    /* Root Context Creation Tests
    /**********************************************************
     */

    @Test
    public void testCreateRootContext_WithoutDuplicateDetector() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        
        assertRootContextProperties(rootContext);
        assertNull("Root context should have no duplicate detector", rootContext.getDupDetector());
    }

    @Test
    public void testCreateRootContext_WithDuplicateDetector() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext rootContext = JsonReadContext.createRootContext(dupDetector);
        
        assertRootContextProperties(rootContext);
        assertNotNull("Root context should have duplicate detector", rootContext.getDupDetector());
    }

    @Test
    public void testCreateRootContext_WithCustomLineAndColumn() {
        int customLine = 10;
        int customColumn = 5;
        JsonReadContext rootContext = JsonReadContext.createRootContext(customLine, customColumn, null);
        
        assertRootContextProperties(rootContext);
        
        JsonLocation startLocation = rootContext.getStartLocation(null);
        assertEquals("Custom line number should be preserved", customLine, startLocation.getLineNr());
        assertEquals("Custom column number should be preserved", customColumn, startLocation.getColumnNr());
    }

    /*
    /**********************************************************
    /* Child Context Creation Tests
    /**********************************************************
     */

    @Test
    public void testCreateChildObjectContext() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        JsonReadContext objectContext = rootContext.createChildObjectContext(DEFAULT_LINE, DEFAULT_COLUMN);
        
        assertChildContextProperties(objectContext, rootContext);
        assertTrue("Child context should be an object context", objectContext.inObject());
        assertEquals("Object context type description", "Object", objectContext.typeDesc());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        JsonReadContext arrayContext = rootContext.createChildArrayContext(DEFAULT_LINE, DEFAULT_COLUMN);
        
        assertChildContextProperties(arrayContext, rootContext);
        assertTrue("Child context should be an array context", arrayContext.inArray());
        assertEquals("Array context type description", "Array", arrayContext.typeDesc());
    }

    @Test
    public void testReuseChildContext_ObjectToArray() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        JsonReadContext firstChild = rootContext.createChildObjectContext(DEFAULT_LINE, DEFAULT_COLUMN);
        JsonReadContext secondChild = rootContext.createChildArrayContext(DEFAULT_LINE, DEFAULT_COLUMN);
        
        assertSame("Child contexts should be reused for performance", firstChild, secondChild);
        assertTrue("Reused context should now be array context", secondChild.inArray());
    }

    /*
    /**********************************************************
    /* Context Navigation Tests
    /**********************************************************
     */

    @Test
    public void testGetParent_RootContext() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        
        assertNull("Root context should have no parent", rootContext.getParent());
    }

    @Test
    public void testGetParent_ChildContext() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        JsonReadContext childContext = rootContext.createChildObjectContext(DEFAULT_LINE, DEFAULT_COLUMN);
        
        assertSame("Child context should reference its parent", rootContext, childContext.getParent());
    }

    @Test
    public void testClearAndGetParent_RootContext() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        
        assertNull("Root context clear should return null", rootContext.clearAndGetParent());
    }

    @Test
    public void testClearAndGetParent_ChildContext() {
        JsonReadContext rootContext = JsonReadContext.createRootContext(null);
        JsonReadContext childContext = rootContext.createChildObjectContext(DEFAULT_LINE, DEFAULT_COLUMN);
        childContext.setCurrentValue("test value");
        
        JsonReadContext parent = childContext.clearAndGetParent();
        
        assertSame("Should return parent context", rootContext, parent);
        // Note: Current value clearing behavior would need to be verified based on implementation
    }

    /*
    /**********************************************************
    /* Field Name Management Tests
    /**********************************************************
     */

    @Test
    public void testCurrentName_InitiallyNull() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        
        assertNull("Initial current name should be null", context.getCurrentName());
        assertFalse("Should not have current name initially", context.hasCurrentName());
    }

    @Test
    public void testSetCurrentName_ValidName() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        
        context.setCurrentName(SAMPLE_FIELD_NAME);
        
        assertEquals("Current name should be set", SAMPLE_FIELD_NAME, context.getCurrentName());
        assertTrue("Should have current name after setting", context.hasCurrentName());
    }

    @Test
    public void testSetCurrentName_EmptyString() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        
        context.setCurrentName("");
        
        assertEquals("Empty string should be valid field name", "", context.getCurrentName());
        assertTrue("Should have current name even if empty", context.hasCurrentName());
    }

    /*
    /**********************************************************
    /* Duplicate Detection Tests
    /**********************************************************
     */

    @Test(expected = NullPointerException.class)
    public void testSetCurrentName_NullWithDuplicateDetector_ThrowsException() {
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);
        
        context.setCurrentName(null); // Should throw NPE
    }

    @Test
    public void testDuplicateFieldDetection_ThrowsIOException() throws IOException {
        JsonFactoryBuilder builder = new JsonFactoryBuilder();
        JsonFactory factory = new JsonFactory(builder);
        JsonParser parser = factory.createNonBlockingByteBufferParser();
        DupDetector dupDetector = DupDetector.rootDetector(parser);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);
        
        context.setCurrentName("duplicateField");
        
        try {
            context.setCurrentName("duplicateField"); // Should detect duplicate
            fail("Expected IOException for duplicate field");
        } catch (IOException e) {
            assertTrue("Exception message should mention duplicate field", 
                      e.getMessage().contains("Duplicate field"));
        }
    }

    @Test
    public void testWithDupDetector_ReplacesDetector() {
        DupDetector originalDetector = DupDetector.rootDetector((JsonParser) null);
        DupDetector newDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonReadContext context = JsonReadContext.createRootContext(originalDetector);
        
        JsonReadContext newContext = context.withDupDetector(newDetector);
        
        assertNotSame("Should create new context instance", context, newContext);
        assertEquals("Should preserve other properties", context.getTypeDesc(), newContext.getTypeDesc());
    }

    /*
    /**********************************************************
    /* Entry Counting Tests
    /**********************************************************
     */

    @Test
    public void testExpectComma_InitiallyFalse() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        
        assertFalse("First entry should not expect comma", context.expectComma());
        assertEquals("Entry count should be 1 after first call", 1, context.getEntryCount());
    }

    @Test
    public void testExpectComma_SubsequentCallsReturnTrue() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        
        context.expectComma(); // First call
        boolean secondCall = context.expectComma();
        
        assertTrue("Second entry should expect comma", secondCall);
        assertEquals("Entry count should be 2 after second call", 2, context.getEntryCount());
    }

    /*
    /**********************************************************
    /* Location Tracking Tests
    /**********************************************************
     */

    @Test
    public void testStartLocation_WithContentReference() {
        JsonReadContext context = JsonReadContext.createRootContext(5, 10, null);
        ContentReference contentRef = ContentReference.unknown();
        
        JsonLocation location = context.startLocation(contentRef);
        
        assertEquals("Line number should match context", 5, location.getLineNr());
        assertEquals("Column number should match context", 10, location.getColumnNr());
        assertEquals("Character offset should be -1 for unknown source", -1L, location.getCharOffset());
    }

    @Test
    public void testGetStartLocation_WithCustomObject() {
        JsonReadContext context = JsonReadContext.createRootContext(3, 7, null);
        Object sourceObject = new Object();
        
        JsonLocation location = context.getStartLocation(sourceObject);
        
        assertEquals("Line number should match context", 3, location.getLineNr());
        assertEquals("Column number should match context", 7, location.getColumnNr());
        assertEquals("Character offset should be -1", -1L, location.getCharOffset());
    }

    /*
    /**********************************************************
    /* Value Management Tests
    /**********************************************************
     */

    @Test
    public void testCurrentValue_InitiallyNull() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        
        assertNull("Initial current value should be null", context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        String testValue = "test value";
        
        context.setCurrentValue(testValue);
        
        assertEquals("Current value should be set", testValue, context.getCurrentValue());
    }

    /*
    /**********************************************************
    /* Context Reset Tests
    /**********************************************************
     */

    @Test
    public void testReset_ChangesContextType() {
        JsonReadContext context = JsonReadContext.createRootContext(null);
        assertTrue("Should initially be root context", context.inRoot());
        
        context.reset(1, DEFAULT_LINE, DEFAULT_COLUMN); // 1 = ARRAY type
        
        assertTrue("Should be array context after reset", context.inArray());
        assertEquals("Entry count should be reset", 0, context.getEntryCount());
    }

    /*
    /**********************************************************
    /* Helper Methods
    /**********************************************************
     */

    private void assertRootContextProperties(JsonReadContext context) {
        assertNotNull("Root context should not be null", context);
        assertTrue("Should be root context", context.inRoot());
        assertEquals("Root nesting depth should be 0", 0, context.getNestingDepth());
        assertEquals("Root entry count should be 0", 0, context.getEntryCount());
        assertEquals("Root type description", "ROOT", context.getTypeDesc());
        assertNull("Root context should have no parent", context.getParent());
    }

    private void assertChildContextProperties(JsonReadContext child, JsonReadContext parent) {
        assertNotNull("Child context should not be null", child);
        assertEquals("Child nesting depth should be parent + 1", 
                    parent.getNestingDepth() + 1, child.getNestingDepth());
        assertEquals("Child entry count should start at 0", 0, child.getEntryCount());
        assertSame("Child should reference parent", parent, child.getParent());
        assertFalse("Child should not be root context", child.inRoot());
    }
}