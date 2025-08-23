package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Elements#val()} method.
 */
public class ElementsValTest {

    @Test
    public void val_onEmptyCollection_shouldReturnEmptyString() {
        // Arrange: Create an empty Elements collection. The Javadoc for val() states
        // it should return an empty string in this scenario.
        Elements emptyElements = new Elements();

        // Act: Call the val() method.
        String value = emptyElements.val();

        // Assert: Verify the result is an empty string.
        assertEquals("", value);
    }

    @Test
    public void val_onNonEmptyCollection_shouldReturnValueOfFirstElement() {
        // Arrange: Create a collection with multiple elements, each with a different value.
        Element input1 = new Element("input").val("firstValue");
        Element input2 = new Element("input").val("secondValue");
        Elements elements = new Elements(input1, input2);

        // Act: Call the val() method.
        String value = elements.val();

        // Assert: Verify the result is the value of the *first* element in the collection.
        assertEquals("firstValue", value);
    }
}