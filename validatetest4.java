package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTestTest4 {

    @Test
    public void testWtf() {
        boolean threw = false;
        try {
            Validate.wtf("Unexpected state reached");
        } catch (IllegalStateException e) {
            threw = true;
            assertEquals("Unexpected state reached", e.getMessage());
        }
        assertTrue(threw);
    }
}
