package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTestTest14 {

    @Test
    public void testNotEmptyParam() {
        // Test with a non-empty string
        Validate.notEmptyParam("foo", "param");
        // Test with an empty string
        boolean threw = false;
        try {
            Validate.notEmptyParam("", "param");
        } catch (ValidationException e) {
            threw = true;
            assertEquals("The 'param' parameter must not be empty.", e.getMessage());
        }
        assertTrue(threw);
        // Test with a null string
        threw = false;
        try {
            Validate.notEmptyParam(null, "param");
        } catch (ValidationException e) {
            threw = true;
            assertEquals("The 'param' parameter must not be empty.", e.getMessage());
        }
        assertTrue(threw);
    }
}
