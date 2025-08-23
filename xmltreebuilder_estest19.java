package org.jsoup.parser;

import org.junit.Test;
import java.util.NoSuchElementException;

public class XmlTreeBuilder_ESTestTest19 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that calling pop() on a builder with an empty element stack
     * throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void popFromEmptyStackThrowsNoSuchElementException() {
        // Arrange: Create a builder and parse a document to initialize its state.
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // The parse method pushes the root Document node onto the builder's internal stack.
        // The input content ("") is not important; we just need to establish the initial state.
        builder.parse("", "https://example.com");

        // Act 1: Pop the root Document element. This succeeds and empties the stack.
        builder.pop();

        // Act 2 & Assert: Attempting to pop again from the now-empty stack.
        // This call is expected to throw a NoSuchElementException, which is verified
        // by the @Test(expected) annotation.
        builder.pop();
    }
}