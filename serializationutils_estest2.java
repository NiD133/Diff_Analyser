package org.apache.commons.lang3;

import static org.junit.Assert.assertThrows;

import java.io.InputStream;
import org.junit.Test;

/**
 * Unit tests for the inner classes of {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that the constructor of {@code ClassLoaderAwareObjectInputStream} throws
     * a {@code NullPointerException} when the provided {@code InputStream} is null.
     */
    @Test
    public void classLoaderAwareObjectInputStreamConstructor_throwsNPE_forNullInputStream() {
        // Arrange
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        // Act & Assert
        // The constructor's call to super(InputStream) is expected to throw the NPE.
        assertThrows(NullPointerException.class, () ->
                new SerializationUtils.ClassLoaderAwareObjectInputStream(null, classLoader)
        );
    }
}