package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Entities#unescape(String)} method.
 */
public class EntitiesUnescapeTest {

    @Test
    public void unescapesKnownNamedEntities() {
        // Arrange
        String input = "An ampersand is &amp; and a less-than is &lt;.";
        String expected = "An ampersand is & and a less-than is <.";

        // Act
        String unescaped = Entities.unescape(input);

        // Assert
        assertEquals(expected, unescaped);
    }

    @Test
    public void unescapesNumericAndHexadecimalEntities() {
        // Arrange
        String input = "Pi is &#960; (or &#960). A CJK character is &#x65B0;.";
        String expected = "Pi is π (or π). A CJK character is 新.";

        // Act
        String unescaped = Entities.unescape(input);

        // Assert
        assertEquals(expected, unescaped);
    }

    @Test
    public void handlesEntitiesWithoutTrailingSemicolon() {
        // Arrange
        String input = "Handles &LT&gt and &reg correctly";
        String expected = "Handles <> and ® correctly";

        // Act
        String unescaped = Entities.unescape(input);

        // Assert
        assertEquals(expected, unescaped);
    }

    @Test
    public void handlesMixedCaseEntities() {
        // Arrange
        String input = "Copyright &copy; or &COPY;. Ligature &AElig;.";
        String expected = "Copyright © or ©. Ligature Æ.";

        // Act
        String unescaped = Entities.unescape(input);

        // Assert
        assertEquals(expected, unescaped);
    }

    @Test
    public void leavesUnknownAndInvalidEntitiesAsIs() {
        // Arrange
        String input = "Unknown: &unknown. Malformed: &0987654321; and &!.";
        String expected = "Unknown: &unknown. Malformed: &0987654321; and &!.";

        // Act
        String unescaped = Entities.unescape(input);

        // Assert
        assertEquals(expected, unescaped);
    }

    @Test
    public void distinguishesBetweenValidAndAmbiguousEntities() {
        // Arrange
        // &angst; is a valid entity (Å), but &angst without a semicolon is not one of the
        // base entities that can be parsed without one, so it should be left as is.
        String input = "Valid: &angst; vs. ambiguous: &angst here.";
        String expected = "Valid: Å vs. ambiguous: &angst here.";

        // Act
        String unescaped = Entities.unescape(input);

        // Assert
        assertEquals(expected, unescaped);
    }

    @Test
    public void unescapesComplexStringWithMultipleEntityTypes() {
        // This test combines multiple scenarios from the original test case.
        // Arrange
        String input = "Hello &AElig; &amp;&LT&gt; &reg &frac34; &copy; &COPY; &#960; &#x65B0; there";
        String expected = "Hello Æ &<> ® ¾ © © π 新 there";

        // Act
        String unescaped = Entities.unescape(input);

        // Assert
        assertEquals(expected, unescaped);
    }
}