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
 * Tests {@link AppendableJoiner}.
 */
@DisplayName("AppendableJoiner Tests")
class AppendableJoinerTest {

    /**
     * A fixture class for testing custom element appending.
     */
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

        @Override
        public String toString() {
            return "Fixture{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {
        @Test
        @DisplayName("Test all builder properties with StringBuilder")
        void testAllBuilderPropertiesStringBuilder() {
            // Create a joiner with a prefix, delimiter, suffix, and custom element appender.
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                    .setPrefix("<")
                    .setDelimiter(".")
                    .setSuffix(">")
                    .setElementAppender((a, e) -> a.append(String.valueOf(e)))
                    .get();

            // Join elements using the configured joiner and verify the output.
            final StringBuilder sbuilder = new StringBuilder("A");
            assertEquals("A<B.C>", joiner.join(sbuilder, "B", "C").toString(), "Joining two strings with custom settings");

            sbuilder.append("1");
            assertEquals("A<B.C>1<D.E>", joiner.join(sbuilder, Arrays.asList("D", "E")).toString(), "Joining a list of strings with custom settings");
        }

        @Test
        @DisplayName("Test build default StringBuilder")
        void testBuildDefaultStringBuilder() {
            // Create a builder with default settings.
            final Builder<Object> builder = AppendableJoiner.builder();
            assertNotSame(builder.get(), builder.get(), "Each call to builder.get() should return a new instance.");

            // Join elements using the default joiner (no prefix, suffix, or delimiter).
            final AppendableJoiner<Object> joiner = builder.get();
            final StringBuilder sbuilder = new StringBuilder("A");
            assertEquals("ABC", joiner.join(sbuilder, "B", "C").toString(), "Joining two strings with default settings");

            sbuilder.append("1");
            assertEquals("ABC1DE", joiner.join(sbuilder, "D", "E").toString(), "Joining two more strings with default settings");
        }

        @Test
        @DisplayName("Test builder instance creation")
        void testBuilder() {
            // Verify that each call to AppendableJoiner.builder() returns a new builder instance.
            assertNotSame(AppendableJoiner.builder(), AppendableJoiner.builder(), "Each call to AppendableJoiner.builder() should return a new instance.");
        }

        @Test
        @DisplayName("Test delimiter appendable")
        void testDelimiterStringBuilder() {
            // Create a joiner with a delimiter.
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();

            // Join elements using the joiner with a delimiter.
            final StringBuilder sbuilder = new StringBuilder("A");
            // does not throw IOException
            assertEquals("AB.C", joiner.join(sbuilder, "B", "C").toString(), "Joining two strings with a delimiter.");

            sbuilder.append("1");
            // does not throw IOException
            assertEquals("AB.C1D.E", joiner.join(sbuilder, Arrays.asList("D", "E")).toString(), "Joining a list of strings with a delimiter.");
        }
    }

    @Nested
    @DisplayName("Appendable Tests")
    class AppendableTests {
        @SuppressWarnings("deprecation") // Test own StrBuilder
        @ParameterizedTest
        @ValueSource(classes = { StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class })
        @DisplayName("Test delimiter with different Appendable types")
        void testDelimiterAppendable(final Class<? extends Appendable> clazz) throws Exception {
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
            final Appendable sbuilder = clazz.getDeclaredConstructor().newInstance();
            sbuilder.append("A");
            // throws IOException
            assertEquals("AB.C", joiner.joinA(sbuilder, "B", "C").toString(), "Joining two strings with a delimiter using " + clazz.getSimpleName());
            sbuilder.append("1");
            // throws IOException
            assertEquals("AB.C1D.E", joiner.joinA(sbuilder, Arrays.asList("D", "E")).toString(), "Joining a list of strings with a delimiter using " + clazz.getSimpleName());
        }
    }

    @Nested
    @DisplayName("Join Tests")
    class JoinTests {
        @Test
        @DisplayName("Test join with custom element appender and prefix/suffix")
        void testToCharSequenceStringBuilder1() {
            // Create a joiner with a prefix, delimiter, suffix, and custom element appender.
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                    .setPrefix("<")
                    .setDelimiter(".")
                    .setSuffix(">")
                    .setElementAppender((a, e) -> a.append("|").append(Objects.toString(e)))
                    .get();

            // Join elements using the configured joiner and verify the output.
            final StringBuilder sbuilder = new StringBuilder("A");
            assertEquals("A<|B.|C>", joiner.join(sbuilder, "B", "C").toString(), "Joining two strings with custom element appender and prefix/suffix");

            sbuilder.append("1");
            assertEquals("A<|B.|C>1<|D.|E>", joiner.join(sbuilder, Arrays.asList("D", "E")).toString(), "Joining a list of strings with custom element appender and prefix/suffix");
        }

        @Test
        @DisplayName("Test join with Fixture and custom element appender")
        void testToCharSequenceStringBuilder2() {
            // Create a joiner with a custom element appender for the Fixture class.
            final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder()
                    .setElementAppender((a, e) -> e.render(a))
                    .get();

            // Join Fixture instances using the configured joiner and verify the output.
            final StringBuilder sbuilder = new StringBuilder("[");
            assertEquals("[B!C!", joiner.join(sbuilder, new Fixture("B"), new Fixture("C")).toString(), "Joining two Fixture objects with custom element appender");

            sbuilder.append("]");
            assertEquals("[B!C!]D!E!", joiner.join(sbuilder, Arrays.asList(new Fixture("D"), new Fixture("E"))).toString(), "Joining a list of Fixture objects with custom element appender");
        }
    }
}