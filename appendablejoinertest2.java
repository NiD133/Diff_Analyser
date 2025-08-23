package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.StringWriter;
import java.util.Arrays;
import org.apache.commons.lang3.AppendableJoiner.Builder;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link AppendableJoiner}.
 */
class AppendableJoinerTest {

    /**
     * Tests that the joiner correctly appends elements with a delimiter
     * to various implementations of {@link Appendable}.
     *
     * @param clazz The Appendable implementation to test.
     */
    @DisplayName("Joining with a delimiter should work for various Appendable types")
    @ParameterizedTest(name = "with {0}")
    @ValueSource(classes = {StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class})
    @SuppressWarnings("deprecation") // For StrBuilder
    void joinWithDelimiterAppendsToVariousAppendableTypes(final Class<? extends Appendable> clazz) throws Exception {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final Appendable appendable = clazz.newInstance();
        appendable.append("A");

        // Act & Assert: Test joining with a varargs array
        joiner.joinA(appendable, "B", "C");
        assertEquals("AB.C", appendable.toString(), "Should join elements with a delimiter.");

        // Arrange for second join
        appendable.append("1");

        // Act & Assert: Test joining with a list to the same appendable
        joiner.joinA(appendable, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", appendable.toString(), "Should continue joining to the same appendable.");
    }

    /**
     * Verifies that the builder's get() method returns a new instance each time,
     * ensuring that builders can be safely reused to create multiple, distinct joiners.
     */
    @Test
    void builderGetShouldCreateNewInstance() {
        // Arrange
        final Builder<Object> builder = AppendableJoiner.builder();

        // Act & Assert
        // Each call to get() should produce a new, distinct AppendableJoiner instance.
        assertNotSame(builder.get(), builder.get());
    }

    /**
     * Tests that a joiner with default settings (no prefix, suffix, or delimiter)
     * simply concatenates the string representation of elements.
     */
    @Test
    void joinWithDefaultSettingsShouldConcatenateElements() {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().get();
        final StringBuilder stringBuilder = new StringBuilder("A");

        // Act: Join the first set of elements
        joiner.join(stringBuilder, "B", "C");

        // Assert
        assertEquals("ABC", stringBuilder.toString());

        // Arrange for second join
        stringBuilder.append("1");

        // Act: Join the second set of elements
        joiner.join(stringBuilder, "D", "E");

        // Assert
        assertEquals("ABC1DE", stringBuilder.toString());
    }
}