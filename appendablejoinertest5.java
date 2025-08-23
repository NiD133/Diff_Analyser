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

public class AppendableJoinerTestTest5 {

    // Test own StrBuilder
    @SuppressWarnings("deprecation")
    @ParameterizedTest
    @ValueSource(classes = { StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class })
    void testDelimiterAppendable(final Class<? extends Appendable> clazz) throws Exception {
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
        final Appendable sbuilder = clazz.newInstance();
        sbuilder.append("A");
        // throws IOException
        assertEquals("AB.C", joiner.joinA(sbuilder, "B", "C").toString());
        sbuilder.append("1");
        // throws IOException
        assertEquals("AB.C1D.E", joiner.joinA(sbuilder, Arrays.asList("D", "E")).toString());
    }

    static class Fixture {

        private final String name;

        Fixture(final String name) {
            this.name = name;
        }

        /**
         * Renders myself onto an Appendable to avoid creating intermediary strings.
         */
        void render(final Appendable appendable) throws IOException {
            appendable.append(name);
            appendable.append('!');
        }
    }

    @Test
    void testToCharSequenceStringBuilder1() {
        // @formatter:off
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setPrefix("<").setDelimiter(".").setSuffix(">").setElementAppender((a, e) -> a.append("|").append(Objects.toString(e))).get();
        // @formatter:on
        final StringBuilder sbuilder = new StringBuilder("A");
        assertEquals("A<|B.|C>", joiner.join(sbuilder, "B", "C").toString());
        sbuilder.append("1");
        assertEquals("A<|B.|C>1<|D.|E>", joiner.join(sbuilder, Arrays.asList("D", "E")).toString());
    }
}
