package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * This test case verifies the behavior of the {@link Elements#html()} method
 * when the collection contains null elements.
 */
public class ElementsHtmlTest {

    /**
     * Verifies that calling {@link Elements#html()} on a collection that contains
     * a {@code null} element correctly throws a {@code NullPointerException}.
     * <p>
     * The {@code html()} method iterates through its elements and calls the
     * {@code .html()} method on each one. If an element is null, this operation
     * will naturally result in a NullPointerException, which is the expected behavior
     * for invalid input.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void htmlShouldThrowNullPointerExceptionWhenListContainsNull() {
        // Arrange: Create an Elements object containing a single null element.
        // The varargs constructor `Elements(Element... elements)` is used here.
        Elements elementsWithNull = new Elements((Element) null);

        // Act: Attempt to get the combined HTML of the elements. This action is
        // expected to trigger the NullPointerException.
        elementsWithNull.html();

        // Assert: The test framework asserts that a NullPointerException was thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}