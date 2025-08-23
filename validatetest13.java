package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTestTest13 {

    @Test
    public void testAssertFail() {
        boolean result = false;
        boolean threw = false;
        try {
            result = Validate.assertFail("This should fail");
        } catch (ValidationException e) {
            threw = true;
            assertEquals("This should fail", e.getMessage());
        }
        assertTrue(threw);
        assertFalse(result);
    }
}
