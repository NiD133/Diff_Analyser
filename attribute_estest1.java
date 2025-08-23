package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link Attribute} class, focusing on boolean attribute detection.
 */
public class AttributeTest {

    @Test
    public void isBooleanAttributeShouldReturnTrueForKnownBooleanAttributes() {
        // A known boolean attribute from the HTML5 spec
        String booleanAttributeName = "allowfullscreen";

        boolean result = Attribute.isBooleanAttribute(booleanAttributeName);

        assertTrue("'" + booleanAttributeName + "' should be recognized as a boolean attribute.", result);
    }

    @Test
    public void isBooleanAttributeShouldReturnFalseForNonBooleanAttributes() {
        // A common, non-boolean attribute
        String nonBooleanAttributeName = "href";

        boolean result = Attribute.isBooleanAttribute(nonBooleanAttributeName);

        assertFalse("'" + nonBooleanAttributeName + "' should not be recognized as a boolean attribute.", result);
    }

    @Test
    public void isBooleanAttributeShouldBeCaseInsensitive() {
        // Boolean attributes are case-insensitive in HTML
        String mixedCaseAttributeName = "aLlOwFuLlScReEn";

        boolean result = Attribute.isBooleanAttribute(mixedCaseAttributeName);

        assertTrue("Boolean attribute check should be case-insensitive.", result);
    }

    @Test
    public void isBooleanAttributeShouldReturnFalseForNullOrEmptyInput() {
        assertFalse("Null input should not be a boolean attribute.", Attribute.isBooleanAttribute(null));
        assertFalse("Empty string should not be a boolean attribute.", Attribute.isBooleanAttribute(""));
    }
}