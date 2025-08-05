package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonReadContext} class, focusing on its state management,
 * navigation, and property access.
 */
public class JsonReadContext_ImprovedTest {

    private final DupDetector rootDetector = DupDetector.rootDetector((JsonParser) null);

    // ========================================================================
    // Context Creation and Lifecycle
    // ========================================================================

    @Test
    public void createRootContext_shouldInitializeAsRoot() {
        // Arrange & Act
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);

        // Assert
        assertTrue(context.inRoot());
        assertFalse(context.inArray());
        assertFalse(context.inObject());
        assertEquals("ROOT", context.getTypeDesc());
        assertEquals(0, context.getNestingDepth());
        assertEquals(0, context.getEntryCount());
        assertNull(context.getParent());
    }

    @Test
    public void createChildObjectContext_shouldCreateNestedObjectContext() {
        // Arrange
        JsonReadContext rootContext = JsonReadContext.createRootContext(rootDetector);

        // Act
        JsonReadContext childContext = rootContext.createChildObjectContext(10, 20);

        // Assert
        assertTrue(childContext.inObject());
        assertEquals("Object", childContext.getTypeDesc());
        assertEquals(1, childContext.getNestingDepth());
        assertEquals(0, childContext.getEntryCount());
        assertSame(rootContext, childContext.getParent());
    }

    @Test
    public void createChildArrayContext_shouldCreateNestedArrayContext() {
        // Arrange
        JsonReadContext rootContext = JsonReadContext.createRootContext(rootDetector);

        // Act
        JsonReadContext childContext = rootContext.createChildArrayContext(10, 20);

        // Assert
        assertTrue(childContext.inArray());
        assertEquals("ARRAY", childContext.getTypeDesc());
        assertEquals(1, childContext.getNestingDepth());
        assertEquals(0, childContext.getEntryCount());
        assertSame(rootContext, childContext.getParent());
    }

    @Test
    public void createChildContext_shouldReuseChildInstance() {
        // Arrange
        JsonReadContext rootContext = JsonReadContext.createRootContext(rootDetector);

        // Act
        JsonReadContext objectChild = rootContext.createChildObjectContext(1, 1);
        JsonReadContext arrayChild = rootContext.createChildArrayContext(2, 2);

        // Assert
        // The same child instance is reset and reused for performance.
        assertSame(objectChild, arrayChild);
        assertTrue("Context should be reset to an array type", arrayChild.inArray());
        assertEquals(1, arrayChild.getNestingDepth());
    }

    @Test
    public void clearAndGetParent_shouldReturnParentAndResetState() {
        // Arrange
        JsonReadContext rootContext = JsonReadContext.createRootContext(rootDetector);
        JsonReadContext childContext = rootContext.createChildObjectContext(1, 1);
        childContext.setCurrentName("testField");
        childContext.setCurrentValue("testValue");

        // Act
        JsonReadContext parent = childContext.clearAndGetParent();

        // Assert
        assertSame(rootContext, parent);
        
        // Verify that the child context state was cleared (it might be reused later)
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getCurrentValue());
    }

    @Test
    public void clearAndGetParent_shouldReturnNull_forRootContext() {
        // Arrange
        JsonReadContext rootContext = JsonReadContext.createRootContext(rootDetector);

        // Act
        JsonReadContext parent = rootContext.clearAndGetParent();

        // Assert
        assertNull(parent);
    }

    // ========================================================================
    // Field Name Handling
    // ========================================================================

    @Test
    public void hasCurrentName_shouldReturnTrue_whenNameIsSet() throws JsonProcessingException {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);
        
        // Act
        context.setCurrentName("myField");

        // Assert
        assertTrue(context.hasCurrentName());
        assertEquals("myField", context.getCurrentName());
    }

    @Test
    public void hasCurrentName_shouldReturnFalse_whenNameIsNotSet() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);

        // Assert
        assertFalse(context.hasCurrentName());
        assertNull(context.getCurrentName());
    }

    @Test(expected = IOException.class)
    public void setCurrentName_shouldThrowIOException_forDuplicateName() throws IOException {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);
        context.setCurrentName("duplicateField");

        // Act
        context.setCurrentName("duplicateField"); // Should throw
    }
    
    @Test
    public void setCurrentName_shouldThrowIOExceptionWithLocation_whenParserIsAvailable() {
        // Arrange
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createNonBlockingByteBufferParser();
        DupDetector detector = DupDetector.rootDetector(parser);
        JsonReadContext context = JsonReadContext.createRootContext(detector);

        try {
            context.setCurrentName("duplicateField");
            // Act
            context.setCurrentName("duplicateField");
            fail("Expected an IOException for duplicate field");
        } catch (IOException e) {
            // Assert
            assertTrue(e.getMessage().contains("Duplicate field 'duplicateField'"));
            // Check that location information from the parser is included
            assertTrue(e.getMessage().contains("line: 1, column: 1"));
        }
    }

    // ========================================================================
    // Value Handling
    // ========================================================================

    @Test
    public void getCurrentValue_shouldReturnPreviouslySetObject() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);
        Object value = new Object();

        // Act
        context.setCurrentValue(value);

        // Assert
        assertSame(value, context.getCurrentValue());
    }

    @Test
    public void getCurrentValue_shouldReturnNull_whenValueIsNotSet() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);

        // Assert
        assertNull(context.getCurrentValue());
    }

    // ========================================================================
    // Location Information
    // ========================================================================

    @Test
    public void startLocation_shouldReflectContextCreationCoordinates() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(12, 34, rootDetector);
        ContentReference contentRef = ContentReference.unknown();

        // Act
        JsonLocation location = context.startLocation(contentRef);

        // Assert
        assertEquals(12, location.getLineNr());
        assertEquals(34, location.getColumnNr());
        assertEquals(-1L, location.getCharOffset());
        assertSame(contentRef, location.contentReference());
    }

    @Test
    public void startLocation_shouldUseDefaultCoordinates_whenNotProvided() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);
        ContentReference contentRef = ContentReference.unknown();

        // Act
        JsonLocation location = context.startLocation(contentRef);

        // Assert
        assertEquals(1, location.getLineNr());
        assertEquals(0, location.getColumnNr());
    }

    // ========================================================================
    // expectComma() Behavior
    // ========================================================================

    @Test
    public void expectComma_shouldReturnFalseOnFirstCallAndTrueOnSubsequentCalls() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector).createChildArrayContext(1, 1);

        // Act & Assert
        assertEquals(0, context.getEntryCount());
        
        boolean firstCall = context.expectComma();
        assertFalse("First call to expectComma should return false", firstCall);
        assertEquals(1, context.getEntryCount());
        assertEquals(0, context.getCurrentIndex());

        boolean secondCall = context.expectComma();
        assertTrue("Second call to expectComma should return true", secondCall);
        assertEquals(2, context.getEntryCount());
        assertEquals(1, context.getCurrentIndex());

        boolean thirdCall = context.expectComma();
        assertTrue("Third call to expectComma should return true", thirdCall);
        assertEquals(3, context.getEntryCount());
        assertEquals(2, context.getCurrentIndex());
    }

    // ========================================================================
    // Duplicate Detector Handling
    // ========================================================================

    @Test
    public void getDupDetector_shouldReturnConfiguredDetector() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(rootDetector);

        // Act & Assert
        assertSame(rootDetector, context.getDupDetector());
    }

    @Test
    public void getDupDetector_shouldReturnNull_whenNotConfigured() {
        // Arrange
        JsonReadContext context = JsonReadContext.createRootContext(null);

        // Act & Assert
        assertNull(context.getDupDetector());
    }

    @Test
    public void withDupDetector_shouldCreateNewContextWithGivenDetector() {
        // Arrange
        JsonReadContext originalContext = JsonReadContext.createRootContext(null);
        originalContext.reset(JsonReadContext.TYPE_OBJECT, 1, 1);

        // Act
        JsonReadContext newContext = originalContext.withDupDetector(rootDetector);

        // Assert
        assertNotSame(originalContext, newContext);
        assertSame(rootDetector, newContext.getDupDetector());
        assertEquals(originalContext.getType(), newContext.getType());
        assertEquals(originalContext.getNestingDepth(), newContext.getNestingDepth());
    }
}