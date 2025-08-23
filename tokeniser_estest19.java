package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link Tokeniser} class, focusing on error handling.
 */
public class TokeniserErrorHandlingTest {

    /**
     * Verifies that the error() method throws a NullPointerException when the varargs
     * array for format arguments is null. This is the current behavior because the
     * arguments are passed directly to String.format(), which does not permit a null
     * arguments array.
     */
    @Test(expected = NullPointerException.class)
    public void errorWithNullArgumentsThrowsNPE() {
        // Arrange: Create a Tokeniser instance.
        // The XmlTreeBuilder is used to set up a valid internal state for the Tokeniser.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.parse("", "http://example.com"); // Initialise the reader
        Tokeniser tokeniser = new Tokeniser(treeBuilder);
        String errorMessage = "Test error message without format specifiers";

        // Act: Call the method under test with a null varargs array.
        tokeniser.error(errorMessage, (Object[]) null);

        // Assert: The test passes if a NullPointerException is thrown, as expected.
    }
}