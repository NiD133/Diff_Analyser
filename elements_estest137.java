package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test for the {@link Elements#toggleClass(String)} method.
 */
public class ElementsToggleClassTest {

    /**
     * Verifies that calling toggleClass with a class name containing only whitespace
     * is ignored and does not modify the elements' class attributes.
     */
    @Test
    public void toggleClassWithWhitespaceOnlyClassNameShouldBeIgnored() {
        // Arrange: Create a document with two elements that have no class attribute.
        Document doc = Jsoup.parse("<div></div><p></p>");
        Elements elements = doc.select("div, p");

        // Act: Call the method under test with a whitespace-only string.
        Elements returnedElements = elements.toggleClass("   ");

        // Assert:
        // 1. The method should return the same Elements instance to allow for method chaining.
        assertSame("toggleClass should return the same instance for chaining.", elements, returnedElements);

        // 2. The class attributes of the elements should remain unchanged (i.e., empty).
        for (Element el : elements) {
            assertEquals("The class attribute should not be added for a whitespace-only class name.",
                         "", el.attr("class"));
        }
    }
}