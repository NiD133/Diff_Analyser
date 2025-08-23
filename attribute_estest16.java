package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the static helper methods in {@link Attribute}.
 */
public class AttributeTest {

    /**
     * Verifies that calling shouldCollapseAttribute with null OutputSettings throws a NullPointerException.
     * The method requires OutputSettings to determine the syntax (HTML or XML) and is not
     * designed to handle a null settings object.
     */
    @Test(expected = NullPointerException.class)
    public void shouldCollapseAttributeThrowsExceptionForNullOutputSettings() {
        // The method call is expected to fail immediately when accessing the null OutputSettings.
        // The key and value arguments are irrelevant for this specific failure case.
        Attribute.shouldCollapseAttribute("key", null, null);
    }
}