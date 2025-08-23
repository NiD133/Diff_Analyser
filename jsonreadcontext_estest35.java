package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParser;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonReadContext} class.
 */
public class JsonReadContextTest {

    /**
     * Verifies that calling {@link JsonReadContext#setCurrentName(String)} with a null argument
     * throws a NullPointerException when a duplicate detector is active.
     * <p>
     * The underlying {@link DupDetector} is responsible for handling name validation and is
     * expected to throw the exception, as it does not accept null names.
     */
    @Test(expected = NullPointerException.class)
    public void setCurrentNameWithNullShouldThrowNPEWhenDupDetectorIsEnabled() throws Exception {
        // Arrange: Create a root context configured with a duplicate detector.
        DupDetector dupDetector = DupDetector.rootDetector((JsonParser) null);
        JsonReadContext context = JsonReadContext.createRootContext(dupDetector);

        // Act: Attempt to set the current name to null. This action is expected to throw.
        context.setCurrentName(null);

        // Assert: The @Test(expected = NullPointerException.class) annotation handles the
        // verification that the expected exception was thrown.
    }
}