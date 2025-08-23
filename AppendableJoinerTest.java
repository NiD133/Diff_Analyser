/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.AppendableJoiner.Builder;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.text.TextStringBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link AppendableJoiner}.
 */
class AppendableJoinerTest {

    private static final String DELIM_DOT = ".";
    private static final String PREFIX_LT = "<";
    private static final String SUFFIX_GT = ">";

    /**
     * Simple type used to verify custom element rendering without creating
     * intermediate strings.
     */
    static class Fixture {
        private final String name;

        Fixture(final String name) {
            this.name = name;
        }

        void render(final Appendable appendable) throws IOException {
            appendable.append(name);
            appendable.append('!');
        }
    }

    private static <A extends Appendable> A newAppendable(final Class<A> type) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return type.getDeclaredConstructor().newInstance();
    }

    @Nested
    @DisplayName("Builder behavior")
    class BuilderBehavior {

        @Test
        @DisplayName("builder() returns a new Builder each time")
        void builderReturnsNewInstances() {
            assertNotSame(AppendableJoiner.builder(), AppendableJoiner.builder());
        }

        @Test
        @DisplayName("Builder.get() returns a new AppendableJoiner each time")
        void getReturnsNewJoiners() {
            final Builder<Object> builder = AppendableJoiner.builder();
            assertNotSame(builder.get(), builder.get());
        }
    }

    @Nested
    @DisplayName("Joining into StringBuilder")
    class JoinIntoStringBuilder {

        @Test
        @DisplayName("Default builder: no prefix/suffix/delimiter")
        void defaultSettings() {
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder().get();

            final StringBuilder out = new StringBuilder("A");
            assertEquals("ABC", joiner.join(out, "B", "C").toString());

            out.append("1");
            assertEquals("ABC1DE", joiner.join(out, Arrays.asList("D", "E")).toString());
        }

        @Test
        @DisplayName("Custom prefix, delimiter, suffix, and explicit element appender")
        void allBuilderProperties() {
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                    .setPrefix(PREFIX_LT)
                    .setDelimiter(DELIM_DOT)
                    .setSuffix(SUFFIX_GT)
                    .setElementAppender((a, e) -> a.append(String.valueOf(e)))
                    .get();

            final StringBuilder out = new StringBuilder("A");
            assertEquals("A<B.C>", joiner.join(out, "B", "C").toString());

            out.append("1");
            assertEquals("A<B.C>1<D.E>", joiner.join(out, Arrays.asList("D", "E")).toString());
        }

        @Test
        @DisplayName("Custom delimiter only")
        void delimiterOnly() {
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                    .setDelimiter(DELIM_DOT)
                    .get();

            final StringBuilder out = new StringBuilder("A");
            assertEquals("AB.C", joiner.join(out, "B", "C").toString());

            out.append("1");
            assertEquals("AB.C1D.E", joiner.join(out, Arrays.asList("D", "E")).toString());
        }

        @Test
        @DisplayName("Custom element appender can prepend text and use Objects.toString")
        void customElementAppenderWithToString() {
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                    .setPrefix(PREFIX_LT)
                    .setDelimiter(DELIM_DOT)
                    .setSuffix(SUFFIX_GT)
                    .setElementAppender((a, e) -> a.append("|").append(Objects.toString(e)))
                    .get();

            final StringBuilder out = new StringBuilder("A");
            assertEquals("A<|B.|C>", joiner.join(out, "B", "C").toString());

            out.append("1");
            assertEquals("A<|B.|C>1<|D.|E>", joiner.join(out, Arrays.asList("D", "E")).toString());
        }

        @Test
        @DisplayName("Custom element type can render directly to Appendable")
        void customElementTypeRendersToAppendable() {
            final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder()
                    .setElementAppender((a, e) -> e.render(a))
                    .get();

            final StringBuilder out = new StringBuilder("[");
            assertEquals("[B!C!", joiner.join(out, new Fixture("B"), new Fixture("C")).toString());

            out.append("]");
            assertEquals("[B!C!]D!E!", joiner.join(out, Arrays.asList(new Fixture("D"), new Fixture("E"))).toString());
        }
    }

    @Nested
    @DisplayName("Joining into generic Appendable (joinA)")
    class JoinIntoAppendable {

        @SuppressWarnings("deprecation") // Intentionally testing StrBuilder
        @ParameterizedTest(name = "Appendable type: {0}")
        @ValueSource(classes = {
                StringBuilder.class,
                StringBuffer.class,
                StringWriter.class,
                StrBuilder.class,
                TextStringBuilder.class
        })
        @DisplayName("Custom delimiter works across common Appendable implementations")
        void delimiterAcrossAppendables(final Class<? extends Appendable> appendableType) throws Exception {
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                    .setDelimiter(DELIM_DOT)
                    .get();

            final Appendable out = newAppendable(appendableType);
            out.append("A");

            assertEquals("AB.C", joiner.joinA(out, "B", "C").toString());

            out.append("1");
            assertEquals("AB.C1D.E", joiner.joinA(out, Arrays.asList("D", "E")).toString());
        }
    }
}