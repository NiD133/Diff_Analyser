package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * This test class contains tests for the {@link Elements} class.
 * This specific test case focuses on the behavior of the wrap() method.
 */
// Note: The original test class name and inheritance are preserved.
public class Elements_ESTestTest55 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling wrap() on an Elements collection that contains a null element
     * correctly throws a NullPointerException.
     */
    @Test
    public void wrapThrowsNullPointerExceptionWhenListContainsNull() {
        // Arrange: Create an Elements collection containing a single null element.
        // The Elements constructor accepts a varargs of Elements, so we must cast null.
        Elements elementsWithNull = new Elements((Element) null);
        String wrapperHtml = "<div></div>";

        // Act & Assert
        try {
            elementsWithNull.wrap(wrapperHtml);
            fail("Expected a NullPointerException to be thrown because the list contains a null element.");
        } catch (NullPointerException e) {
            // Success: The expected exception was caught.
            // The wrap() method iterates through its list and calls methods on each element.
            // When it encounters the null element, it throws an NPE as expected.
        }
    }
}