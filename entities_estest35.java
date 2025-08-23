package org.jsoup.nodes;

import org.junit.Test;
import org.jsoup.nodes.Document.OutputSettings;

/**
 * Test suite for exception handling in {@link Entities#escape}.
 */
public class EntitiesEscapeTest {

    /**
     * Verifies that the escape method throws a NullPointerException if the destination Appendable is null.
     * This ensures the method fails fast with invalid arguments, preventing potential errors downstream.
     */
    @Test(expected = NullPointerException.class)
    public void escapeShouldThrowNullPointerExceptionWhenAppendableIsNull() {
        // Arrange: Create the necessary, non-null arguments for the method call.
        // The specific values of these arguments are irrelevant to this test.
        OutputSettings settings = new OutputSettings();
        String anyString = "<data>";
        int anyOptions = 0;

        // Act: Call the method under test with a null Appendable.
        // The @Test annotation will assert that a NullPointerException is thrown.
        Entities.escape(null, anyString, settings, anyOptions);
    }
}