package org.jsoup.parser;

import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on the behavior of the XmlTreeBuilder.
 * The original test was auto-generated and tested an obscure, misleading scenario.
 * This version has been refactored to test a clear, meaningful behavior of the `popStackToClose` method.
 */
public class XmlTreeBuilder_ESTestTest18 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Verifies that calling popStackToClose on a builder with an empty element stack
     * is a safe operation and does not cause an exception.
     */
    @Test(timeout = 4000)
    public void popStackToCloseOnEmptyStackIsSafeAndDoesNothing() {
        // Arrange: Create a tree builder. Its internal stack of open elements is initially empty.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        
        // Create a standard end tag to attempt to close.
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("a");

        // Act: Attempt to close the 'a' tag. Since the stack is empty, this method
        // should simply do nothing and return safely.
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert: The primary assertion is that the 'Act' phase completed without
        // throwing an exception. We can also explicitly verify that the builder's
        // internal stack remains empty, confirming it was a no-op.
        // Note: 'stack' is a protected field, accessible within the same package.
        assertTrue("The stack should remain empty.", xmlTreeBuilder.stack.isEmpty());
    }
}