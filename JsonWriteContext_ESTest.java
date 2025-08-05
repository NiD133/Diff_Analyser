/*
 * This test suite has been refactored from an auto-generated version to improve
 * understandability and maintainability. The goal is to provide clear, focused tests
 * that are easy for developers to read and debug.
 */
package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link JsonWriteContext}, focusing on its state management
 * for JSON generation, including context creation, nesting, and value/field writing logic.
 */
public class JsonWriteContextTest {

    // --- Context Creation and Nesting Tests ---

    @Test
    public void createRootContext_initializesCorrectly() {
        // Arrange & Act
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Assert
        assertTrue("Context should be in the root state", rootContext.inRoot());
        assertEquals("Nesting depth should be 0 for root", 0, rootContext.getNestingDepth());
        assertEquals("Entry count should be 0 initially", 0, rootContext.getEntryCount());
        assertEquals("Type description should be 'ROOT'", "ROOT", rootContext.getTypeDesc());
        assertNull("Parent of root context should be null", rootContext.getParent());
    }

    @Test
    public void createChild_contextsAreCorrectlyNested() {
        // Arrange
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Act
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        JsonWriteContext arrayContext = objectContext.createChildArrayContext();

        // Assert
        assertEquals("Object context's parent should be root", rootContext, objectContext.getParent());
        assertEquals("Array context's parent should be object", objectContext, arrayContext.getParent());

        assertEquals("Root nesting depth should be 0", 0, rootContext.getNestingDepth());
        assertEquals("Object nesting depth should be 1", 1, objectContext.getNestingDepth());
        assertEquals("Array nesting depth should be 2", 2, arrayContext.getNestingDepth());

        assertTrue(rootContext.inRoot());
        assertTrue(objectContext.inObject());
        assertTrue(arrayContext.inArray());
    }

    @Test
    public void createChild_reusesChildInstance() {
        // Arrange
        JsonWriteContext parent = JsonWriteContext.createRootContext();

        // Act
        JsonWriteContext child1 = parent.createChildArrayContext();
        child1.writeValue(); // Change state to ensure it's reset
        JsonWriteContext child2 = parent.createChildObjectContext();

        // Assert
        assertSame("Child context instance should be reused for performance", child1, child2);
        assertTrue("Reused context should now be an object", child2.inObject());
        assertEquals("Reused context should be reset", 0, child2.getEntryCount());
    }

    // --- writeValue() Tests ---

    @Test
    public void writeValue_inArrayContext_returnsStatusOkAfterCommaAndIncrementsIndex() {
        // Arrange
        JsonWriteContext arrayContext = JsonWriteContext.createRootContext().createChildArrayContext();

        // Act & Assert for the first value
        int firstStatus = arrayContext.writeValue();
        assertEquals("First value should return STATUS_OK_AS_IS", JsonWriteContext.STATUS_OK_AS_IS, firstStatus);
        assertEquals(0, arrayContext.getCurrentIndex());

        // Act & Assert for subsequent values
        int secondStatus = arrayContext.writeValue();
        assertEquals("Second value should return STATUS_OK_AFTER_COMMA", JsonWriteContext.STATUS_OK_AFTER_COMMA, secondStatus);
        assertEquals(1, arrayContext.getCurrentIndex());
        assertEquals(2, arrayContext.getEntryCount());
    }

    @Test
    public void writeValue_inRootContext_returnsStatusOkAsIs() {
        // Arrange
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();

        // Act
        int status = rootContext.writeValue();

        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(1, rootContext.getEntryCount());
    }

    @Test
    public void writeValue_inObjectContextBeforeName_returnsStatusExpectName() {
        // Arrange
        JsonWriteContext objectContext = JsonWriteContext.createRootContext().createChildObjectContext();

        // Act
        int status = objectContext.writeValue();

        // Assert
        assertEquals("Should expect a name before a value in an object", JsonWriteContext.STATUS_EXPECT_NAME, status);
        assertEquals("Entry count should not increment", 0, objectContext.getEntryCount());
    }

    // --- writeFieldName() Tests ---

    @Test
    public void writeFieldName_inObjectContext_returnsStatusOkAfterColon() throws IOException {
        // Arrange
        JsonWriteContext objectContext = JsonWriteContext.createRootContext().createChildObjectContext();

        // Act
        int status = objectContext.writeFieldName("field1");

        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertTrue(objectContext.hasCurrentName());
        assertEquals("field1", objectContext.getCurrentName());
    }

    @Test
    public void writeFieldName_forSecondFieldAfterValue_returnsStatusOkAfterColon() throws IOException {
        // Arrange
        JsonWriteContext objectContext = JsonWriteContext.createRootContext().createChildObjectContext();
        objectContext.writeFieldName("field1");
        objectContext.writeValue(); // Write value for field1

        // Act
        int status = objectContext.writeFieldName("field2");

        // Assert
        assertEquals("Writing a second field name should expect a value next", JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertTrue(objectContext.hasCurrentName());
        assertEquals("field2", objectContext.getCurrentName());
    }

    @Test
    public void writeFieldName_whenAnotherNameIsPending_returnsStatusExpectValue() throws IOException {
        // Arrange
        JsonWriteContext objectContext = JsonWriteContext.createRootContext().createChildObjectContext();
        objectContext.writeFieldName("field1");

        // Act
        int status = objectContext.writeFieldName("field2");

        // Assert
        assertEquals("Writing a field name when another is pending is an error state", JsonWriteContext.STATUS_EXPECT_VALUE, status);
        assertEquals("The current name should be updated to the new name", "field2", objectContext.getCurrentName());
    }

    @Test(expected = JsonProcessingException.class)
    public void writeFieldName_withDuplicateName_throwsException() throws IOException {
        // Arrange
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext objectContext = JsonWriteContext.createRootContext(dupDetector).createChildObjectContext();
        objectContext.writeFieldName("duplicate");
        objectContext.writeValue();

        // Act
        objectContext.writeFieldName("duplicate"); // Should throw
    }

    // --- State Management Tests (reset, clearAndGetParent, etc.) ---

    @Test
    public void reset_changesContextTypeAndState() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeValue(); // Change state
        assertEquals(1, context.getEntryCount());

        // Act: Reset to Array
        context.reset(JsonWriteContext.TYPE_ARRAY);

        // Assert: Array state
        assertTrue(context.inArray());
        assertEquals("State should be cleared", 0, context.getEntryCount());

        // Act: Reset to Object
        context.reset(JsonWriteContext.TYPE_OBJECT);

        // Assert: Object state
        assertTrue(context.inObject());
        assertEquals("State should be cleared", 0, context.getEntryCount());
    }

    @Test
    public void clearAndGetParent_onChildContext_returnsParentAndClearsChild() throws IOException {
        // Arrange
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = rootContext.createChildObjectContext();
        childContext.setCurrentValue("some value");
        childContext.writeFieldName("name");

        // Act
        JsonWriteContext parent = childContext.clearAndGetParent();

        // Assert
        assertSame(rootContext, parent);
        assertNull("Child's current value should be cleared", childContext.getCurrentValue());
        assertFalse("Child should no longer have a current name", childContext.hasCurrentName());
    }

    @Test
    public void hasCurrentName_returnsCorrectStateDuringObjectWriting() throws IOException {
        // Arrange
        JsonWriteContext objectContext = JsonWriteContext.createRootContext().createChildObjectContext();

        // Assert initial state
        assertFalse("Initially, context should not have a current name", objectContext.hasCurrentName());

        // Act: Write field name
        objectContext.writeFieldName("testName");

        // Assert after setting name
        assertTrue("After writing a name, context should have a current name", objectContext.hasCurrentName());
        assertEquals("testName", objectContext.getCurrentName());

        // Act: Write value
        objectContext.writeValue();

        // Assert after writing value
        assertFalse("After writing a value, the name is 'consumed' and should not be current", objectContext.hasCurrentName());
    }

    // --- Duplicate Detection Tests ---

    @Test
    public void withDupDetector_setsDetectorOnContext() {
        // Arrange
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertNull(rootContext.getDupDetector());
        DupDetector dupDetector = DupDetector.rootDetector((JsonGenerator) null);

        // Act
        JsonWriteContext newContext = rootContext.withDupDetector(dupDetector);

        // Assert
        assertSame("The same context instance should be returned", rootContext, newContext);
        assertSame(dupDetector, newContext.getDupDetector());
    }

    @Test
    public void childContext_inheritsDupDetectorFromParent() {
        // Arrange
        DupDetector parentDetector = DupDetector.rootDetector((JsonGenerator) null);
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(parentDetector);

        // Act
        JsonWriteContext childContext = rootContext.createChildObjectContext();
        DupDetector childDetector = childContext.getDupDetector();

        // Assert
        assertNotNull("Child context should have a DupDetector", childDetector);
        assertNotSame("Child's DupDetector should be a new instance for the new scope",
                      parentDetector, childDetector);
    }
}