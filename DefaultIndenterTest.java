package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link DefaultIndenter}.
 *
 * @date 2017-07-31
 * @see DefaultIndenter
 **/
class DefaultIndenterTest {

    @Test
    void testWithLinefeed() {
        // Setup: Create a DefaultIndenter instance and modify its linefeed
        DefaultIndenter originalIndenter = new DefaultIndenter();
        DefaultIndenter modifiedIndenter = originalIndenter.withLinefeed("-XG'#x");
        DefaultIndenter finalIndenter = modifiedIndenter.withLinefeed("-XG'#x");

        // Assertions: Verify the linefeed and object identity
        assertEquals("-XG'#x", finalIndenter.getEol(), "Linefeed should be '-XG'#x'");
        assertNotSame(finalIndenter, originalIndenter, "Final indenter should not be the same instance as the original");
        assertSame(finalIndenter, modifiedIndenter, "Final indenter should be the same instance as the modified one");
    }

    @Test
    void testWithIndent() {
        // Setup: Create a DefaultIndenter instance and modify its indent
        DefaultIndenter originalIndenter = new DefaultIndenter();
        DefaultIndenter modifiedIndenter = originalIndenter.withIndent("9Qh/6,~n");
        DefaultIndenter finalIndenter = modifiedIndenter.withIndent("9Qh/6,~n");

        // Assertions: Verify the end-of-line character and object identity
        assertEquals(System.lineSeparator(), finalIndenter.getEol(), "EOL should be the system's line separator");
        assertNotSame(finalIndenter, originalIndenter, "Final indenter should not be the same instance as the original");
        assertSame(finalIndenter, modifiedIndenter, "Final indenter should be the same instance as the modified one");
    }
}