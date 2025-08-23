package org.jsoup.parser;

import org.junit.Test;
import java.util.NoSuchElementException;

/**
 * Test suite for the {@link XmlTreeBuilder} class, focusing on its stack operations.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that calling pop() on a new XmlTreeBuilder with an empty element stack
     * correctly throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void popOnEmptyStackThrowsNoSuchElementException() {
        // Arrange: Create a new XmlTreeBuilder, which has an empty internal stack.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act: Attempt to pop an element from the empty stack.
        // Assert: The @Test(expected) annotation asserts that a NoSuchElementException is thrown.
        xmlTreeBuilder.pop();
    }
}