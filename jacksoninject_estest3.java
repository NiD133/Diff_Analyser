package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test suite focuses on the JacksonInject.Value class.
 */
public class JacksonInjectValueTest {

    /**
     * Tests that willUseInput() returns the explicitly configured 'useInput' value
     * (which is false in this case), and correctly ignores the provided default value.
     */
    @Test
    public void willUseInput_WhenUseInputIsExplicitlyFalse_ShouldReturnFalse() {
        // Arrange: Create a Value instance where 'useInput' is explicitly set to FALSE.
        Boolean useInput = Boolean.FALSE;
        Boolean optional = Boolean.FALSE; // Not relevant for this test, but needed for constructor.
        JacksonInject.Value injectValue = JacksonInject.Value.construct(null, useInput, optional);

        // Use a default value that is different from the expected result to ensure
        // the method is not accidentally returning the default.
        boolean defaultValueToIgnore = true;

        // Act: Call the method under test.
        boolean result = injectValue.willUseInput(defaultValueToIgnore);

        // Assert: The result should be false, matching the explicitly set value.
        assertFalse("Expected willUseInput to return the explicit value (false), not the default.", result);
    }
}