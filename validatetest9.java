package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTestTest9 {

    @Test
    public void testNotNullParam() {
        // Test with a non-null object
        Object obj = new Object();
        Validate.notNullParam(obj, "param");
        // Test with a null object
        boolean threw = false;
        try {
            Validate.notNullParam(null, "param");
        } catch (ValidationException e) {
            threw = true;
            assertEquals("The parameter 'param' must not be null.", e.getMessage());
        }
        assertTrue(threw);
    }
}
