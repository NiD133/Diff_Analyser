package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.AppendableJoiner.Builder;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link AppendableJoiner}.
 */
class AppendableJoinerTest {

    /**
     * A simple fixture class for testing purposes.
     */
    static class Fixture {
        private final String name;

        Fixture(final String name) {
            this.name = name;
        }

        /**
         * Appends the fixture's name followed by an exclamation mark to the given Appendable.
         */
        void render(final Appendable appendable) throws IOException {
            appendable.append(name).append('!');
        }
    }

    @Test
    void testJoinerWithCustomPrefixSuffixDelimiter() {
        // Create a joiner with custom prefix, suffix, and delimiter
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((a, e) -> a.append(String.valueOf(e)))
                .get();

        final StringBuilder sbuilder = new StringBuilder("A");
        assertEquals("A<B.C>", joiner.join(sbuilder, "B", "C").toString());

        sbuilder.append("1");
        assertEquals("A<B.C>1<D.E>", joiner.join(sbuilder, Arrays.asList("D", "E")).toString());
    }

    @Test
    void testDefaultJoinerCreatesNewInstances() {
        final Builder<Object> builder = AppendableJoiner.builder();
        assertNotSame(builder.get(), builder.get());

        final AppendableJoiner<Object> joiner = builder.get();
        final StringBuilder sbuilder = new StringBuilder("A");
        assertEquals("ABC", joiner.join(sbuilder, "B", "C").toString());

        sbuilder.append("1");
        assertEquals("ABC1DE", joiner.join(sbuilder, "D", "E").toString());
    }

    @Test
    void testBuilderCreatesDistinctInstances() {
        assertNotSame(AppendableJoiner.builder(), AppendableJoiner.builder());
    }

    @ParameterizedTest
    @ValueSource(classes = { StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class })
    void testJoinerWithDifferentAppendableTypes(final Class<? extends Appendable> clazz) throws Exception {
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final Appendable appendable = clazz.getDeclaredConstructor().newInstance();
        appendable.append("A");

        assertEquals("AB.C", joiner.joinA(appendable, "B", "C").toString());

        appendable.append("1");
        assertEquals("AB.C1D.E", joiner.joinA(appendable, Arrays.asList("D", "E")).toString());
    }

    @Test
    void testJoinerWithDelimiterOnly() {
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final StringBuilder sbuilder = new StringBuilder("A");

        assertEquals("AB.C", joiner.join(sbuilder, "B", "C").toString());

        sbuilder.append("1");
        assertEquals("AB.C1D.E", joiner.join(sbuilder, Arrays.asList("D", "E")).toString());
    }

    @Test
    void testJoinerWithCustomElementAppender() {
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((a, e) -> a.append("|").append(Objects.toString(e)))
                .get();

        final StringBuilder sbuilder = new StringBuilder("A");
        assertEquals("A<|B.|C>", joiner.join(sbuilder, "B", "C").toString());

        sbuilder.append("1");
        assertEquals("A<|B.|C>1<|D.|E>", joiner.join(sbuilder, Arrays.asList("D", "E")).toString());
    }

    @Test
    void testJoinerWithFixtureElementAppender() {
        final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder()
                .setElementAppender((a, e) -> e.render(a))
                .get();

        final StringBuilder sbuilder = new StringBuilder("[");
        assertEquals("[B!C!", joiner.join(sbuilder, new Fixture("B"), new Fixture("C")).toString());

        sbuilder.append("]");
        assertEquals("[B!C!]D!E!", joiner.join(sbuilder, Arrays.asList(new Fixture("D"), new Fixture("E"))).toString());
    }
}