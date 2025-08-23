package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

// The original test class name and inheritance are preserved as they might be part of a larger, generated test suite.
public class JsonIgnoreProperties_ESTestTest18 extends JsonIgnoreProperties_ESTest_scaffolding {

    /**
     * Tests that calling `withoutAllowGetters()` on a `JsonIgnoreProperties.Value`
     * instance returns the canonical `Value.EMPTY` singleton if the resulting
     * configuration becomes identical to the default empty configuration.
     * This verifies an optimization that avoids creating new objects unnecessarily.
     */
    @Test(timeout = 4000)
    public void withoutAllowGetters_whenResultIsEquivalentToEmpty_returnsEmptySingleton() {
        // Arrange: Create a Value instance that is identical to Value.EMPTY,
        // except that `allowGetters` is set to true.
        // The properties of Value.EMPTY are:
        // - ignored: empty set
        // - ignoreUnknown: false
        // - allowGetters: false
        // - allowSetters: false
        // - merge: true
        Set<String> noIgnoredProperties = Collections.emptySet();
        JsonIgnoreProperties.Value valueWithGettersAllowed = JsonIgnoreProperties.Value.construct(
                noIgnoredProperties,
                false, // ignoreUnknown
                true,  // allowGetters (the only difference from EMPTY)
                false, // allowSetters
                true   // merge
        );

        // Sanity check the initial state.
        assertTrue("Precondition: The initial value should have allowGetters enabled.",
                valueWithGettersAllowed.getAllowGetters());

        // Act: Call the method under test, which should flip `allowGetters` to false,
        // making the configuration match the EMPTY instance.
        JsonIgnoreProperties.Value result = valueWithGettersAllowed.withoutAllowGetters();

        // Assert: The result of the operation should be the canonical EMPTY singleton,
        // not just a new instance with the same values.
        assertSame("The resulting value should be the canonical EMPTY instance.",
                JsonIgnoreProperties.Value.EMPTY, result);
    }
}