package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link Elements#hasAttr(String)} method.
 *
 * This class was improved from a previous version (ElementsTestTest3)
 * for better clarity and adherence to testing best practices.
 */
public class ElementsTest {

    private Elements elements;

    @BeforeEach
    void setUp() {
        // Arrange: Create a common set of elements for all tests in this class.
        // The HTML contains four <p> elements. Two have a 'class' attribute,
        // but none have a 'style' attribute.
        String html = "<p title=foo><p title=bar><p class=foo><p class=bar>";
        Document doc = Jsoup.parse(html);
        elements = doc.select("p");
    }

    @Test
    void hasAttrShouldReturnTrueWhenAttributeExistsOnAnyElement() {
        // Act: Check for an attribute that is present on at least one element.
        boolean hasClassAttribute = elements.hasAttr("class");

        // Assert: The method should return true.
        assertTrue(hasClassAttribute, "Expected true because at least one element has the 'class' attribute.");
    }

    @Test
    void hasAttrShouldReturnFalseWhenAttributeIsAbsentFromAllElements() {
        // Act: Check for an attribute that is not present on any element.
        boolean hasStyleAttribute = elements.hasAttr("style");

        // Assert: The method should return false.
        assertFalse(hasStyleAttribute, "Expected false because no elements have the 'style' attribute.");
    }
}