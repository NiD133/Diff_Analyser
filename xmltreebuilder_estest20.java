package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for {@link XmlTreeBuilder}, focusing on its stack manipulation behavior.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that attempting to pop from an empty element stack throws an exception.
     * The internal stack of a new XmlTreeBuilder instance is empty by default.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void popOnEmptyStackThrowsException() {
        // Arrange: Create a new XmlTreeBuilder, which has an empty internal element stack.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Act: Attempt to pop from the empty stack.
        // Assert: The @Test(expected) annotation asserts that an
        // ArrayIndexOutOfBoundsException is thrown.
        xmlTreeBuilder.pop();
    }
}