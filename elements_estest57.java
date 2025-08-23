package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * This test class contains tests for the {@link Elements} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class Elements_ESTestTest57 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling {@link Elements#val(String)} on a collection
     * that contains a null element throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void valThrowsExceptionWhenElementIsNull() {
        // Arrange: Create an Elements list and add a null element to it.
        Elements elements = new Elements();
        elements.add((Element) null);

        // Act: Attempt to set the value on all elements in the list.
        // Assert: The @Test annotation expects a NullPointerException to be thrown,
        // as the method will try to dereference the null element.
        elements.val("any-value");
    }
}