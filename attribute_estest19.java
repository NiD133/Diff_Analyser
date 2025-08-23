package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

// The class name and inheritance are preserved from the original test structure.
public class Attribute_ESTestTest19 extends Attribute_ESTest_scaffolding {

    /**
     * Verifies that calling Attribute.htmlNoValidate with a null Document.OutputSettings
     * object throws a NullPointerException.
     * <p>
     * The OutputSettings object is required to determine how the attribute should be
     * formatted (e.g., for collapsing boolean attributes). Passing null is an
     * invalid state that should be handled with an exception.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void htmlNoValidateThrowsNullPointerExceptionForNullOutputSettings() {
        // Define dummy key and value for the method call. Their content is not
        // relevant for this test, as the exception is expected due to the null arguments.
        String key = "any-key";
        String value = "any-value";

        // The method is expected to throw a NullPointerException because it attempts
        // to access the syntax from the null OutputSettings object.
        Attribute.htmlNoValidate(key, value, (QuietAppendable) null, (Document.OutputSettings) null);
    }
}