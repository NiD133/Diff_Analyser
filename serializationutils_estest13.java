package org.apache.commons.lang3;

import static org.junit.Assert.assertThrows;

import java.io.InputStream;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    @Test
    public void deserialize_withNullInputStream_shouldThrowNullPointerException() {
        // The Javadoc for deserialize(InputStream) guarantees that a NullPointerException
        // is thrown for a null input. This test verifies that contract.
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            SerializationUtils.deserialize((InputStream) null);
        });
    }
}