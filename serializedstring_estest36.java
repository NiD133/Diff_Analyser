package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link SerializedString} class, focusing on exception handling.
 */
public class SerializedStringTest {

    /**
     * Verifies that calling putQuotedUTF8 with a null ByteBuffer
     * correctly throws a NullPointerException.
     */
    @Test
    public void putQuotedUTF8ShouldThrowNullPointerExceptionWhenBufferIsNull() {
        // Arrange
        SerializedString serializedString = new SerializedString("any-value");
        ByteBuffer nullBuffer = null;

        // Act & Assert
        // The method should prevent operations on a null buffer by throwing an NPE.
        assertThrows(NullPointerException.class, () -> {
            serializedString.putQuotedUTF8(nullBuffer);
        });
    }
}