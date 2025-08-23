package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Test suite for the {@link Elements} class.
 */
public class ElementsTest {

    /**
     * Verifies that calling {@link Elements#append(String)} on a collection
     * that contains null elements throws a {@link NullPointerException}.
     * <p>
     * The append operation iterates through each element in the list, and attempting
     * to call the instance method {@code .append()} on a null reference will
     * naturally cause this exception. This test confirms that behavior.
     */
    @Test(expected = NullPointerException.class)
    public void appendShouldThrowNullPointerExceptionWhenListContainsNulls() {
        // Arrange: Create an Elements collection containing a null element.
        // A single null is sufficient to trigger the condition.
        Element[] elementsWithNull = { null };
        Elements elements = new Elements(elementsWithNull);
        String htmlToAppend = "<p>Some HTML</p>";

        // Act: Attempt to append HTML to the collection.
        // This action is expected to throw a NullPointerException.
        elements.append(htmlToAppend);

        // Assert: The test will pass if a NullPointerException is thrown,
        // as declared by the @Test(expected=...) annotation.
    }
}