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

@DisplayName("AppendableJoiner Tests")
public class AppendableJoinerTest {

    private static final String INITIAL_VALUE = "A";
    private static final String SEPARATOR = ".";

    @DisplayName("Should join elements to various Appendable implementations")
    @ParameterizedTest(name = "with {0}")
    @ValueSource(classes = {StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class})
    @SuppressWarnings("deprecation") // For StrBuilder
    void shouldJoinElementsToVariousAppendables(final Class<? extends Appendable> clazz) throws Exception {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(SEPARATOR).get();
        final Appendable appendable = clazz.newInstance();
        appendable.append(INITIAL_VALUE);

        // Act 1: Join a varargs array
        joiner.joinA(appendable, "B", "C");

        // Assert 1
        assertEquals("A.B.C", appendable.toString(), "Should correctly join varargs to existing content.");

        // Act 2: Append a value and then join an Iterable
        appendable.append("1");
        joiner.joinA(appendable, Arrays.asList("D", "E"));

        // Assert 2
        assertEquals("A.B.C1.D.E", appendable.toString(), "Should correctly join an iterable after another operation.");
    }

    @Test
    @DisplayName("Should join elements to a StringBuilder using all builder properties")
    void shouldJoinElementsToStringBuilderWithAllProperties() {
        // Arrange: Create a joiner with a prefix, suffix, delimiter, and the default element appender.
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
            .setPrefix("<")
            .setDelimiter(SEPARATOR)
            .setSuffix(">")
            .setElementAppender((a, e) -> a.append(String.valueOf(e)))
            .get();
        
        final StringBuilder builder = new StringBuilder(INITIAL_VALUE);

        // Act 1: Join a varargs array
        joiner.join(builder, "B", "C");

        // Assert 1
        assertEquals("A<B.C>", builder.toString(), "Should correctly join varargs with all properties set.");

        // Act 2: Append a value and then join an Iterable
        builder.append("1");
        joiner.join(builder, Arrays.asList("D", "E"));

        // Assert 2
        assertEquals("A<B.C>1<D.E>", builder.toString(), "Should correctly join an iterable with all properties set after another operation.");
    }
}