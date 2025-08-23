package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.Stream;
import org.apache.commons.lang3.AppendableJoiner.Builder;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link AppendableJoiner}.
 */
@DisplayName("Tests for AppendableJoiner")
public class AppendableJoinerTest {

    /**
     * Provides instances of various Appendable implementations for parameterized tests.
     *
     * @return a stream of Appendable instances.
     */
    @SuppressWarnings("deprecation") // For StrBuilder constructor, which is used for comprehensive testing.
    private static Stream<Appendable> appendableProvider() {
        return Stream.of(
            new StringBuilder(),
            new StringBuffer(),
            new StringWriter(),
            new StrBuilder(),
            new TextStringBuilder()
        );
    }

    @DisplayName("joinA should correctly append items to various Appendable types")
    @ParameterizedTest(name = "for {0}")
    @MethodSource("appendableProvider")
    void joinA_should_appendToVariousAppendableTypes(final Appendable appendable) throws IOException {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        appendable.append("A");

        // Act & Assert: First join with varargs
        joiner.joinA(appendable, "B", "C");
        assertEquals("AB.C", appendable.toString(), "Should join varargs to the existing content.");

        // Act & Assert: Second join with an Iterable on the same Appendable
        appendable.append("1");
        joiner.joinA(appendable, Arrays.asList("D", "E"));
        assertEquals("AB.C1D.E", appendable.toString(), "Should join an iterable to the modified content.");
    }

    @Test
    @DisplayName("builder() factory method should return a new Builder instance on each call")
    void builder_should_returnNewInstance_onEachCall() {
        // Act
        final Builder<Object> builder1 = AppendableJoiner.builder();
        final Builder<Object> builder2 = AppendableJoiner.builder();

        // Assert
        assertNotSame(builder1, builder2, "Each call to builder() should produce a new instance.");
    }
}