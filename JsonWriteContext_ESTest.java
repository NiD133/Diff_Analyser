package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.filter.FilteringGeneratorDelegate;
import com.fasterxml.jackson.core.filter.TokenFilter;
import com.fasterxml.jackson.core.json.DupDetector;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Test suite for JsonWriteContext functionality.
 * Tests context management during JSON writing operations including
 * arrays, objects, field names, and duplicate detection.
 */
public class JsonWriteContextTest {

    // Tests for array context and value writing
    
    @Test
    public void testArrayValueWriting() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = rootContext.createChildArrayContext("");
        
        // Write multiple values to array
        arrayContext.writeValue();
        arrayContext.writeValue();
        int status = arrayContext.writeValue();
        
        assertEquals("Should have written 3 values", 2, arrayContext.getCurrentIndex());
        assertEquals("Status should indicate comma needed", 1, status);
    }

    // Tests for root context behavior
    
    @Test
    public void testRootContextValueWriting() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.reset(50000, null);
        
        int status = rootContext.writeValue();
        
        assertEquals("Root should have 1 entry", 1, rootContext.getEntryCount());
        assertEquals("Status should be OK as-is", 0, status);
    }

    @Test
    public void testRootContextFieldNameWriting() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        rootContext.reset(240); // Reset to non-root type
        
        int status = rootContext.writeFieldName("");
        
        assertFalse("Should no longer be root context", rootContext.inRoot());
        assertEquals("Status should expect value", 4, status);
    }

    // Tests for object context and field name handling
    
    @Test
    public void testObjectContextFieldNames() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = new JsonWriteContext((byte) (-16), null, dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext(new Object());
        
        objectContext.writeFieldName("testField");
        objectContext.withDupDetector(dupDetector);
        
        assertTrue("Should have current field name", objectContext.hasCurrentName());
    }

    @Test
    public void testObjectContextValueWriting() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        
        int status = objectContext.writeValue();
        
        assertEquals("Status should indicate error (no field name)", 5, status);
        assertEquals("Nesting depth should be 1", 1, objectContext.getNestingDepth());
    }

    // Tests for context hierarchy and parent relationships
    
    @Test
    public void testContextHierarchy() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        JsonWriteContext arrayContext = objectContext.createChildArrayContext(new Object());
        
        JsonWriteContext parent = arrayContext.getParent();
        
        assertNotNull("Array should have parent", parent);
        assertTrue("Parent should be object context", parent.inObject());
        assertEquals("Array should be at depth 2", 2, arrayContext.getNestingDepth());
        assertEquals("Array type should be correct", "ARRAY", arrayContext.getTypeDesc());
    }

    @Test
    public void testClearAndGetParent() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        JsonWriteContext arrayContext = objectContext.createChildArrayContext(new Object());
        
        JsonWriteContext clearedParent = arrayContext.clearAndGetParent();
        
        assertNotNull("Should return parent after clearing", clearedParent);
        assertTrue("Parent should be object context", clearedParent.inObject());
        assertEquals("Parent should have no entries", 0, clearedParent.getEntryCount());
    }

    // Tests for context reset functionality
    
    @Test
    public void testContextReset() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object testValue = new Object();
        
        JsonWriteContext resetContext = rootContext.reset(0, testValue);
        
        assertEquals("Entry count should be reset", 0, rootContext.getEntryCount());
        assertEquals("Nesting depth should be 0", 0, resetContext.getNestingDepth());
        assertTrue("Should be root context", rootContext.inRoot());
    }

    @Test
    public void testResetToObjectType() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(null);
        
        rootContext.reset(2, null); // Type 2 = OBJECT
        
        assertEquals("Type should be OBJECT", "OBJECT", rootContext.getTypeDesc());
    }

    // Tests for duplicate detection
    
    @Test
    public void testDuplicateFieldDetection() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        
        objectContext.writeFieldName("duplicateField");
        objectContext.writeValue();
        
        try {
            objectContext.writeFieldName("duplicateField");
            fail("Should throw IOException for duplicate field");
        } catch (IOException e) {
            assertTrue("Should mention duplicate field", e.getMessage().contains("Duplicate field"));
        }
    }

    @Test
    public void testNullFieldNameWithDuplicateDetection() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        
        objectContext.writeFieldName(null);
        objectContext.writeValue();
        
        try {
            objectContext.writeFieldName(null);
            fail("Should throw NullPointerException for null field name");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // Tests for current value management
    
    @Test
    public void testCurrentValueHandling() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        Object testValue = new Object();
        
        rootContext._currentValue = testValue;
        Object retrievedValue = rootContext.getCurrentValue();
        
        assertSame("Should return same object", testValue, retrievedValue);
    }

    @Test
    public void testSetCurrentValue() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        
        rootContext.setCurrentValue(rootContext);
        
        assertEquals("Entry count should remain 0", 0, rootContext.getEntryCount());
        assertTrue("Should still be root", rootContext.inRoot());
    }

    // Tests for context state queries
    
    @Test
    public void testHasCurrentName() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        
        assertFalse("Root should not have current name", rootContext.hasCurrentName());
        
        rootContext.reset(1); // Change to non-root
        rootContext._currentName = "testName";
        
        assertTrue("Should have current name after setting", rootContext.hasCurrentName());
    }

    @Test
    public void testGetCurrentName() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        
        assertNull("Root should have null current name", rootContext.getCurrentName());
        
        rootContext.reset(1);
        rootContext._currentName = "testName";
        
        assertEquals("Should return set name", "testName", rootContext.getCurrentName());
    }

    // Tests for context type descriptions
    
    @Test
    public void testContextTypeDescriptions() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals("Root type description", "ROOT", rootContext.getTypeDesc());
        
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        assertEquals("Array type description", "ARRAY", arrayContext.getTypeDesc());
        
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        assertEquals("Object type description", "OBJECT", objectContext.getTypeDesc());
    }

    // Tests for edge cases and error conditions
    
    @Test
    public void testRootContextClearAndGetParent() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        
        JsonWriteContext parent = rootContext.clearAndGetParent();
        
        assertNull("Root context should have no parent", parent);
    }

    @Test
    public void testFieldNameInRootContext() throws Throwable {
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        
        int status = rootContext.writeFieldName(null);
        
        assertEquals("Should expect value after field name", 4, status);
    }

    // Tests for child context reuse
    
    @Test
    public void testChildContextReuse() throws Throwable {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext firstChild = rootContext.createChildArrayContext();
        JsonWriteContext secondChild = rootContext.createChildObjectContext(new Object());
        
        // Second child should reuse the first child instance
        assertSame("Should reuse child context instance", firstChild, secondChild);
        assertEquals("Type should be updated to OBJECT", "OBJECT", secondChild.getTypeDesc());
    }
}