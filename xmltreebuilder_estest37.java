package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link XmlTreeBuilder}.
 * This class contains tests focusing on edge cases and malformed inputs.
 */
public class XmlTreeBuilderTest {

    /**
     * Tests that the XmlTreeBuilder throws a NullPointerException when processing an
     * XML declaration token that contains a malformed attribute with a null name.
     * This test ensures the system's behavior is understood when encountering
     * an internally inconsistent token, which might arise from a tokenizer bug.
     */
    @Test
    public void processShouldThrowNPEForXmlDeclarationWithNullAttributeName() {
        // Arrange: Create a builder and a malformed XML declaration token.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.XmlDecl malformedDeclToken = new Token.XmlDecl();

        // Simulate a malformed token state by calling an internal-use method.
        // The `newAttribute()` method, when called out of its normal sequence during tokenization,
        // adds an attribute to the token's internal list but leaves its name (key) as null.
        malformedDeclToken.newAttribute();

        // Act & Assert
        try {
            xmlTreeBuilder.process(malformedDeclToken);
            fail("A NullPointerException was expected but not thrown.");
        } catch (NullPointerException e) {
            // The NPE is expected. When the XmlTreeBuilder processes the token, it
            // attempts to copy its attributes. The underlying `Attributes.put()` method
            // calls `key.toLowerCase()`, which results in an NPE when the key is null.
            assertNull("The exception message should be null.", e.getMessage());

            // For more precise testing, verify the origin of the exception.
            // The original test used a custom verifier; here, we achieve the same
            // goal by inspecting the stack trace, making the test self-contained.
            StackTraceElement topOfStack = e.getStackTrace()[0];
            assertEquals("Exception should originate from the Attributes class.",
                "org.jsoup.nodes.Attributes", topOfStack.getClassName());
        }
    }
}