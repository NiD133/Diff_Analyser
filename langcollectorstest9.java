package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LangCollectors}.
 *
 * The original class name 'LangCollectorsTestTest9' was likely a typo and has been
 * renamed for clarity and convention.
 */
public class LangCollectorsTest {

    /**
     * Groups tests for the LangCollectors.joining() factory methods.
     * Using a @Nested class provides clear context for a group of related tests.
     */
    @Nested
    @DisplayName("joining collector")
    class JoiningTests {

        // A simple helper to reduce boilerplate in tests by calling the method under test.
        private String join(final Collector<Object, ?, String> collector, final Object... items) {
            return LangCollectors.collect(collector, items);
        }

        @Test
        @DisplayName("should join elements using a delimiter, prefix, and suffix")
        void joiningWithDelimiterPrefixAndSuffix() {
            // The collector is defined locally, making the test self-contained and easy to understand.
            // This collector uses the 4-argument version of `joining` with the standard Objects::toString.
            final Collector<Object, ?, String> collector = LangCollectors.joining("-", "<", ">", Objects::toString);

            assertEquals("<>", join(collector), "Should produce only prefix and suffix for empty input");
            assertEquals("<1>", join(collector, "1"), "Should correctly format a single element");
            assertEquals("<1-2-3>", join(collector, "1", "2", "3"), "Should join multiple elements with the delimiter");
            assertEquals("<1-null-3>", join(collector, "1", null, "3"), "Should convert null to the string 'null' by default");
        }

        @Test
        @DisplayName("should use a custom function to represent null elements")
        void joiningWithCustomNullRepresentation() {
            // This test focuses on a single behavior: handling nulls with a custom function.
            final Function<Object, String> customToString = o -> Objects.toString(o, "NUL");
            final Collector<Object, ?, String> collector = LangCollectors.joining("-", "<", ">", customToString);

            final String result = join(collector, "1", null, "3");

            assertEquals("<1-NUL-3>", result, "Should use the provided function to represent null");
        }
    }
}