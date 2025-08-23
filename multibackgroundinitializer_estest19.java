package org.apache.commons.lang3.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for {@link MultiBackgroundInitializer}.
 */
public class MultiBackgroundInitializerTest {

    @Test
    public void addInitializerShouldThrowNullPointerExceptionWhenInitializerIsNull() {
        // Arrange
        final MultiBackgroundInitializer multiInitializer = new MultiBackgroundInitializer();
        final String initializerName = "testInitializer";

        // Act & Assert
        // The addInitializer method should throw a NullPointerException because the
        // provided BackgroundInitializer instance is null.
        final NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            multiInitializer.addInitializer(initializerName, null);
        });

        // Verify that the exception message is as expected from Objects.requireNonNull.
        assertEquals("backgroundInitializer", thrown.getMessage());
    }
}