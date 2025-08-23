package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

/**
 * Test suite for the {@link Attribute} class.
 * This test focuses on the static {@code html} method.
 */
public class Attribute_ESTestTest33 extends Attribute_ESTest_scaffolding {

    /**
     * Verifies that the static Attribute.html() method throws a NullPointerException
     * when the provided Appendable (the destination for the output) is null.
     */
    @Test(expected = NullPointerException.class)
    public void htmlMethodThrowsNullPointerExceptionWhenAppendableIsNull() {
        // Arrange
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String key = "some-key";
        String value = "some-value";
        QuietAppendable nullAppendable = null;

        // Act & Assert
        // This call is expected to throw a NullPointerException because the appendable is null.
        Attribute.html(key, value, nullAppendable, outputSettings);
    }
}