package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTestTest12 {

    @Test
    public void testIsFalse() {
        // Test with a false value
        Validate.isFalse(false);
        // Test with a true value
        boolean threw = false;
        try {
            Validate.isFalse(true);
        } catch (ValidationException e) {
            threw = true;
            assertEquals("Must be false", e.getMessage());
        }
        assertTrue(threw);
    }
}
