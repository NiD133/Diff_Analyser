package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Test suite for the {@link Elements#val()} method.
 */
public class Elements_ESTestTest58 {

    /**
     * Verifies that calling val() on an Elements collection throws a NullPointerException
     * if the first element in the collection is null.
     */
    @Test(expected = NullPointerException.class)
    public void valShouldThrowNullPointerExceptionWhenFirstElementIsNull() {
        // Arrange: Create an Elements collection where the first item is null.
        // The val() method operates on the first element, so if that element is null,
        // an exception is expected when its methods are invoked.
        Elements elementsWithNull = new Elements((Element) null);

        // Act: Attempt to get the value from the collection.
        elementsWithNull.val();

        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
    }
}