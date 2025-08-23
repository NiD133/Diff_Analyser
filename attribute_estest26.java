package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that {@code Attribute.getValidKey()} throws a {@code NullPointerException}
     * when the provided key is null, as a null key is never valid.
     */
    @Test(expected = NullPointerException.class)
    public void getValidKeyShouldThrowExceptionForNullKey() {
        // The specific syntax (html or xml) does not affect the outcome for a null key,
        // but an instance is required for the method signature.
        Syntax syntax = Syntax.html;

        // This call is expected to throw a NullPointerException.
        // The assertion is handled by the `expected` parameter of the @Test annotation.
        Attribute.getValidKey(null, syntax);
    }
}