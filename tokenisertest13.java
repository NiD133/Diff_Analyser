package org.jsoup.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link TokenData} class.
 *
 * Note: The class under test, TokenData, is a package-private helper class
 * used by the Tokeniser. It acts as a specialized string buffer.
 */
public class TokenDataTest {

    @Test
    void toStringShouldAccuratelyReflectDataChanges() {
        // Arrange: Create a new TokenData instance.
        TokenData data = new TokenData();
        assertEquals("", data.toString(), "A new TokenData instance should have an empty string representation.");

        // Act & Assert: Set a value.
        data.set("abc");
        assertEquals("abc", data.toString(), "After setting a value, toString() should return that value.");

        // Act & Assert: Append another value.
        data.append("def");
        assertEquals("abcdef", data.toString(), "After appending, toString() should return the combined string.");
    }
}