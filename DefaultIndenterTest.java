package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DefaultIndenter}.
 */
class DefaultIndenterTest {

    @Test
    void withLinefeedUpdatesEol() {
        // Setup: Create a default indenter instance
        DefaultIndenter originalIndenter = new DefaultIndenter();

        // Operation: Set a custom linefeed twice
        DefaultIndenter customLinefeedIndenter = originalIndenter.withLinefeed("-XG'#x");
        DefaultIndenter sameLinefeedIndenter = customLinefeedIndenter.withLinefeed("-XG'#x");

        // Verification:
        // - New EOL value is correctly set
        assertEquals("-XG'#x", sameLinefeedIndenter.getEol());
        // - Each withLinefeed call returns a new instance (not same as original)
        assertNotSame(originalIndenter, sameLinefeedIndenter);
        // - Repeated call with same linefeed returns same instance (object reuse)
        assertSame(customLinefeedIndenter, sameLinefeedIndenter);
    }

    @Test
    void withIndentUpdatesIndentation() {
        // Setup: Create a default indenter instance
        DefaultIndenter originalIndenter = new DefaultIndenter();

        // Operation: Set a custom indent twice
        DefaultIndenter customIndentIndenter = originalIndenter.withIndent("9Qh/6,~n");
        DefaultIndenter sameIndentIndenter = customIndentIndenter.withIndent("9Qh/6,~n");

        // Verification:
        // - EOL remains unchanged (system default)
        assertEquals(System.lineSeparator(), sameIndentIndenter.getEol());
        // - Each withIndent call returns a new instance (not same as original)
        assertNotSame(originalIndenter, sameIndentIndenter);
        // - Repeated call with same indent returns same instance (object reuse)
        assertSame(customIndentIndenter, sameIndentIndenter);
    }
}