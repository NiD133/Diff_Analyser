package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link AppendableJoiner}.
 * This suite focuses on verifying the joining behavior with various Appendable types
 * and the use of custom element appenders.
 */
@SuppressWarnings("deprecation") // For StrBuilder which is used as a test case
public class AppendableJoinerTest {

    /**
     * A test fixture class used to verify the custom element appender functionality.
     * Its {@link #render(Appendable)} method provides a custom way to append the object's state.
     */
    private static class Fixture {
        private final String name;

        Fixture(final String name) {
            this.name = name;
        }

        /**
         * Renders this fixture onto an Appendable by appending its name followed by an exclamation mark.
         *
         * @param appendable The Appendable to render to.
         * @throws IOException if an I/O error occurs.
         */
        void render(final Appendable appendable) throws IOException {
            appendable.append(name).append('!');
        }
    }

    @DisplayName("joinA should append varargs to various Appendable types")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(classes = {StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class})
    void joinWithDelimiterAppendsToExistingAppendableUsingVarArgs(final Class<? extends Appendable> appendableClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final Appendable appendable = appendableClass.getConstructor().newInstance();
        appendable.append("EXISTING");

        // Act
        joiner.joinA(appendable, "A", "B");

        // Assert
        assertEquals("EXISTINGA.B", appendable.toString());
    }

    @DisplayName("joinA should append an Iterable to various Appendable types")
    @ParameterizedTest(name = "for {0}")
    @ValueSource(classes = {StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class})
    void joinWithDelimiterAppendsToExistingAppendableUsingIterable(final Class<? extends Appendable> appendableClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        // Arrange
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final Appendable appendable = appendableClass.getConstructor().newInstance();
        appendable.append("EXISTING");
        final List<String> elements = Arrays.asList("A", "B");

        // Act
        joiner.joinA(appendable, elements);

        // Assert
        assertEquals("EXISTINGA.B", appendable.toString());
    }

    @Test
    @DisplayName("join should append varargs to a StringBuilder using a custom element appender")
    void joinWithCustomElementAppenderAppendsToStringBuilderUsingVarArgs() {
        // Arrange
        final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder()
                .setDelimiter(",")
                .setElementAppender(Fixture::render)
                .get();

        final StringBuilder builder = new StringBuilder("PREFIX:");
        final Fixture fixture1 = new Fixture("B");
        final Fixture fixture2 = new Fixture("C");

        // Act
        joiner.join(builder, fixture1, fixture2);

        // Assert
        final String expected = "PREFIX:B!,C!";
        assertEquals(expected, builder.toString());
    }

    @Test
    @DisplayName("join should append an Iterable to a StringBuilder using a custom element appender")
    void joinWithCustomElementAppenderAppendsToStringBuilderUsingIterable() {
        // Arrange
        final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder()
                .setDelimiter(",")
                .setElementAppender(Fixture::render)
                .get();

        final StringBuilder builder = new StringBuilder("PREFIX:");
        final List<Fixture> elements = Arrays.asList(new Fixture("D"), new Fixture("E"));

        // Act
        joiner.join(builder, elements);

        // Assert
        final String expected = "PREFIX:D!,E!";
        assertEquals(expected, builder.toString());
    }
}