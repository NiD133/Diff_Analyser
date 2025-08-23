package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTestTest6 {

    @Test
    public void testEnsureNotNullWithMessage() {
        // Test with a non-null object
        Object obj = new Object();
        assertSame(obj, Validate.ensureNotNull(obj, "Object must not be null"));
        // Test with a null object
        boolean threw = false;
        try {
            Validate.ensureNotNull(null, "Custom error message");
        } catch (ValidationException e) {
            threw = true;
            assertEquals("Custom error message", e.getMessage());
        }
        assertTrue(threw);
    }
}
