package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * This test class focuses on verifying the behavior of the Elements class.
 * The original test was auto-generated, and this version has been refactored for clarity.
 */
public class Elements_ESTestTest127 extends Elements_ESTest_scaffolding {

    /**
     * Verifies that calling tagName() on an Elements collection containing a null element
     * throws a NullPointerException. This is expected because the method iterates through
     * its internal list and calls a method on each element.
     */
    @Test(expected = NullPointerException.class)
    public void tagNameThrowsExceptionWhenElementsListContainsNull() {
        // Arrange: Create an Elements collection containing a null element.
        // The original test created an array of 12 nulls, but one is sufficient to test the behavior.
        Elements elementsWithNull = new Elements((Element) null);

        // Act: Calling tagName() on this collection should throw a NullPointerException
        // when it attempts to call a method on the null element.
        elementsWithNull.tagName("div");

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected=...) annotation.
    }
}