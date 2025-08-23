package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTestTest8 {

    @Test
    void expectNotNull() {
        String foo = "Foo";
        String foo2 = Validate.expectNotNull(foo);
        assertSame(foo, foo2);
        // Test with a null object
        String bar = null;
        boolean threw = false;
        try {
            Validate.expectNotNull(bar);
        } catch (ValidationException e) {
            threw = true;
            assertEquals("Object must not be null", e.getMessage());
        }
        assertTrue(threw);
    }
}
