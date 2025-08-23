package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;
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

    private static final String DELIMITER = ".";

    @DisplayName("joinA should join elements to any Appendable, potentially throwing IOException")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(classes = {StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class})
    @SuppressWarnings("deprecation") // For StrBuilder
    void testJoinAWithVariousAppendableTypes(final Class<? extends Appendable> clazz) throws Exception {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(DELIMITER).get();
        final Appendable appendable = clazz.getDeclaredConstructor().newInstance();
        appendable.append("A");

        // Act & Assert: First join with varargs
        joiner.joinA(appendable, "B", "C");
        assertEquals("AB.C", appendable.toString(), "Should join varargs elements to the existing appendable.");

        // Act & Assert: Second join with an Iterable on the same Appendable
        appendable.append("1");
        joiner.joinA(appendable, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", appendable.toString(), "Should join iterable elements to the modified appendable.");
    }

    @Test
    @DisplayName("join should join elements to a StringBuilder without a checked IOException")
    void testJoinWithStringBuilder() {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(DELIMITER).get();
        final StringBuilder builder = new StringBuilder("A");

        // Act & Assert: First join with varargs
        joiner.join(builder, "B", "C");
        assertEquals("AB.C", builder.toString(), "Should join varargs elements to the existing StringBuilder.");

        // Act & Assert: Second join with an Iterable on the same StringBuilder
        builder.append("1");
        joiner.join(builder, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", builder.toString(), "Should join iterable elements to the modified StringBuilder.");
    }
}