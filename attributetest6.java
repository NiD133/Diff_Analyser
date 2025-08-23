package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Attribute} class.
 */
public class AttributeTest {

    @Test
    void parsedBooleanAttributeShouldHaveCorrectProperties() {
        // Arrange: Parse an element with a boolean attribute (e.g., 'hidden') that has no explicit value.
        String html = "<div hidden></div>";
        Element div = Jsoup.parse(html).selectFirst("div");
        assertNotNull(div, "The 'div' element should be found in the parsed HTML.");

        // Act: Retrieve the attribute from the element.
        // There's only one attribute, so we can safely get the first one from the iterator.
        Attribute hiddenAttribute = div.attributes().iterator().next();

        // Assert: Verify the properties of the parsed boolean attribute.
        // A boolean attribute without a value is parsed as having a key, but its value is an empty string.
        assertEquals("", div.attr("hidden"), "Element.attr() should return an empty string for a boolean attribute.");

        assertAll("Properties of the Attribute object",
            () -> assertEquals("hidden", hiddenAttribute.getKey(), "The key should be 'hidden'."),
            () -> assertEquals("", hiddenAttribute.getValue(), "The value should be an empty string."),
            () -> assertFalse(hiddenAttribute.hasDeclaredValue(), "Should be false as no value was explicitly declared in the HTML."),
            () -> assertTrue(Attribute.isBooleanAttribute(hiddenAttribute.getKey()), "The key 'hidden' should be recognized as a boolean attribute type.")
        );
    }
}