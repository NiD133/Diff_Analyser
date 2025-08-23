package org.jsoup.nodes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A more understandable test suite for the {@link Entities} class.
 */
class EntitiesTest {

    @Nested
    @DisplayName("getByName")
    class GetByName {

        @Test
        @DisplayName("should resolve a common named entity")
        void resolvesCommonEntity() {
            // "copy" is a well-known entity for the copyright symbol.
            assertEquals("©", Entities.getByName("copy"));
        }

        @Test
        @DisplayName("should resolve a symbolic named entity")
        void resolvesSymbolicEntity() {
            // "gg" is an entity for the "much greater-than" symbol.
            assertEquals("≫", Entities.getByName("gg"));
        }

        @Test
        @DisplayName("should resolve a multi-codepoint named entity")
        void resolvesMultiCodepointEntity() {
            // "nGt" ("not Greater-Than") is an example of an entity that resolves to multiple characters.
            assertEquals("≫⃒", Entities.getByName("nGt"));
        }

        @Test
        @DisplayName("should resolve a ligature named entity")
        void resolvesLigatureEntity() {
            // "fjlig" is an example of a ligature entity, which represents two characters.
            assertEquals("fj", Entities.getByName("fjlig"));
        }
    }
}