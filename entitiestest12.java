package org.jsoup.nodes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for HTML entity unescaping, specifically focusing on ambiguous ampersand cases.
 * This scenario is based on the HTML specification for parsing character references.
 *
 * @see <a href="https://github.com/jhy/jsoup/issues/2207">GitHub Issue #2207</a>
 * @see <a href="https://html.spec.whatwg.org/multipage/parsing.html#character-reference-state">HTML Spec: Character Reference State</a>
 */
public class EntitiesTest {

    @Test
    @DisplayName("Unescaping in non-strict mode should match the longest possible entity prefix")
    void unescapeMatchesLongestPrefixInNonStrictMode() {
        // This test case is based on an example from the HTML specification.
        // It checks the behavior for an "ambiguous ampersand".
        // For '&notit;', the longest prefix that is a valid named entity is '&not;'.
        // Therefore, it should be unescaped to '¬it;'.
        // '&notin;' is a complete entity match and should be unescaped to '∉'.
        String textWithAmbiguousEntities = "I'm &notit; I tell you. I'm &notin; I tell you.";
        String expected = "I'm ¬it; I tell you. I'm ∉ I tell you.";

        // Non-strict mode (inAttribute=false) is used for general text content.
        boolean inAttribute = false;
        String unescapedText = Entities.unescape(textWithAmbiguousEntities, inAttribute);

        assertEquals(expected, unescapedText);
    }

    @Test
    @DisplayName("Unescaping in strict mode should only unescape fully-formed, known entities")
    void unescapeRequiresFullMatchInStrictMode() {
        // Strict mode is used for attribute values, where entities must be well-formed and known.
        // '&notit;' is not a valid named entity, so it should be left untouched.
        // '&notin;' is a valid, semicolon-terminated entity and should be unescaped.
        String textWithAmbiguousEntities = "I'm &notit; I tell you. I'm &notin; I tell you.";
        String expected = "I'm &notit; I tell you. I'm ∉ I tell you.";

        // Strict mode (inAttribute=true) requires a full entity match.
        boolean inAttribute = true;
        String unescapedText = Entities.unescape(textWithAmbiguousEntities, inAttribute);

        assertEquals(expected, unescapedText);
    }
}