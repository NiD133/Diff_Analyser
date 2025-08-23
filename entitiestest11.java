package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for {@link Entities} unescaping logic.
 */
public class EntitiesTest {

    @Test
    void unescapeDifferentiatesBetweenStrictAndNonStrictModes() {
        // Arrange
        // This string contains one entity-like pattern without a trailing semicolon (&amp=)
        // and one valid entity with a trailing semicolon (&amp;).
        String textWithAmbiguousEntity = "Hello &amp= &amp;";

        // 1. Test strict unescaping (strict=true)
        // Strict mode is used for attributes and requires a trailing semicolon.
        // It should only unescape "&amp;" and leave "&amp=" as is.
        String unescapedStrict = Entities.unescape(textWithAmbiguousEntity, true);
        assertEquals("Hello &amp= &", unescapedStrict,
            "In strict mode, only entities with a trailing semicolon should be unescaped.");

        // 2. Test non-strict unescaping (the default behavior)
        // The default unescape() is non-strict and more lenient.
        // It unescapes "&amp;" and also correctly interprets "&amp=" as an entity.
        String unescapedDefault = Entities.unescape(textWithAmbiguousEntity);
        assertEquals("Hello &= &", unescapedDefault,
            "Default unescape (non-strict) should process entities even without a trailing semicolon.");

        // 3. Test explicitly non-strict unescaping (strict=false)
        // This should behave identically to the default method.
        String unescapedNonStrict = Entities.unescape(textWithAmbiguousEntity, false);
        assertEquals("Hello &= &", unescapedNonStrict,
            "Explicitly non-strict unescape should behave the same as the default.");
    }
}