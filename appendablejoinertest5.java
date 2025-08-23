package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link AppendableJoiner}.
 */
// Renamed from AppendableJoinerTestTest5 for clarity and conciseness.
public class AppendableJoinerTest {

    /**
     * Tests that the joiner correctly appends to various implementations of {@link Appendable}.
     * This test verifies joining both a varargs array and an Iterable.
     *
     * @param appendableClass The class of the Appendable to test.
     */
    @DisplayName("Joining should work with different Appendable types")
    @ParameterizedTest(name = "with {0}")
    @ValueSource(classes = {StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class})
    @SuppressWarnings("deprecation") // For the legacy StrBuilder class
    void testJoinWithDelimiterAppendsToVariousAppendables(final Class<? extends Appendable> appendableClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        // Arrange: Create a joiner with a simple delimiter and an instance of the Appendable.
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final Appendable appendable = appendableClass.getConstructor().newInstance();
        appendable.append("A");

        // Act 1: Join a varargs array of elements.
        joiner.joinA(appendable, "B", "C");

        // Assert 1: The elements should be joined with the delimiter.
        assertEquals("AB.C", appendable.toString());

        // Arrange 2: Append more content to test continued joining.
        appendable.append("1");

        // Act 2: Join an Iterable of elements to the same Appendable.
        joiner.joinA(appendable, Arrays.asList("D", "E"));

        // Assert 2: The new elements should be appended after the intermediate content.
        assertEquals("AB.C1D.E", appendable.toString());
    }

    /**
     * Tests a complex joiner with a prefix, suffix, delimiter, and a custom element appender.
     * It verifies that joining to a StringBuilder works correctly in multiple steps.
     */
    @Test
    @DisplayName("Joining with all options and a custom appender should produce the correct string")
    void testJoinWithAllOptionsAndCustomAppender() {
        // Arrange: Create a joiner with a prefix, suffix, delimiter, and a custom appender
        // that prepends a "|" to each element.
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
            .setPrefix("<")
            .setDelimiter(".")
            .setSuffix(">")
            .setElementAppender((app, element) -> app.append("|").append(Objects.toString(element)))
            .get();
        final StringBuilder stringBuilder = new StringBuilder("A");

        // Act 1: Join a varargs array of elements.
        joiner.join(stringBuilder, "B", "C");

        // Assert 1: The result should include the initial content, followed by the
        // prefix, the custom-appended elements separated by the delimiter, and the suffix.
        final String expectedAfterFirstJoin = "A<|B.|C>";
        assertEquals(expectedAfterFirstJoin, stringBuilder.toString());

        // Arrange 2: Append more content to test joining onto an existing string.
        stringBuilder.append("1");

        // Act 2: Join an Iterable of elements to the same StringBuilder.
        joiner.join(stringBuilder, Arrays.asList("D", "E"));

        // Assert 2: The new joined content should be appended after the intermediate "1".
        final String expectedAfterSecondJoin = "A<|B.|C>1<|D.|E>";
        assertEquals(expectedAfterSecondJoin, stringBuilder.toString());
    }
}