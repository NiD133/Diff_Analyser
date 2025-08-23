package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Verifies the behavior of the {@link Elements#set(int, Element)} method,
 * specifically for handling invalid index arguments.
 */
public class ElementsSetInvalidIndexTest {

    /**
     * Tests that calling set() with a negative index on an Elements object
     * throws an ArrayIndexOutOfBoundsException. The underlying implementation
     * delegates to ArrayList, which has this behavior.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void setWithNegativeIndexThrowsException() {
        // Arrange: Create an empty list of elements and a new element to add.
        Elements elements = new Elements();
        Element elementToSet = new Element("p");
        int negativeIndex = -1;

        // Act: Attempt to set an element at the negative index.
        // This call is expected to throw the exception, which is handled by the
        // 'expected' parameter in the @Test annotation.
        elements.set(negativeIndex, elementToSet);
    }
}