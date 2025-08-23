package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests for the {@link Tokeniser} class, focusing on specific initialization scenarios.
 *
 * Note: The original class name and inheritance were artifacts of a test generation tool.
 * In a full refactoring, this class would be integrated into a more cohesively named test suite for Tokeniser.
 */
public class Tokeniser_ESTestTest38 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that createTempBuffer() can be called without throwing an exception after the Tokeniser is initialized.
     * This serves as a basic smoke test for the method's initialization logic.
     */
    @Test
    public void createTempBufferShouldNotThrowExceptionAfterInitialization() {
        // Arrange: Create a Tokeniser instance.
        // A Tokeniser requires a TreeBuilder to be instantiated. The parse() call
        // initializes the builder's internal state (like the CharacterReader), which the
        // Tokeniser's constructor depends on. The specific content being parsed is not
        // relevant for this test.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<root/>", "http://example.com/");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Call the method under test.
        tokeniser.createTempBuffer();

        // Assert: The test passes if no exception is thrown.
        // No explicit assertions are needed for this type of "does not throw" test.
    }
}