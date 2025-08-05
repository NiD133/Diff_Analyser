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
@DisplayName("Tests for AppendableJoiner")
class AppendableJoinerTest {

    /**
     * A fixture class for testing custom element rendering.
     */
    static class Fixture {
        private final String name;

        Fixture(final String name) {
            this.name = name;
        }

        /**
         * Renders this fixture onto an Appendable without creating intermediary strings.
         */
        void render(final Appendable appendable) throws IOException {
            appendable.append(name);
            appendable.append('!');
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {
        @Test
        @DisplayName("builder() should return a new Builder instance on each call")
        void builder_should_returnNewInstanceOnEachCall() {
            // when & then
            assertNotSame(AppendableJoiner.builder(), AppendableJoiner.builder());
        }
    }

    @Nested
    @DisplayName("Default Joiner Behavior")
    class DefaultJoinerTests {
        @Test
        @DisplayName("join() should concatenate elements with no delimiter, prefix, or suffix")
        void join_shouldConcatenateElements_withDefaultSettings() {
            // given
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder().get();
            final StringBuilder builder = new StringBuilder("A");

            // when
            joiner.join(builder, "B", "C");
            // then
            assertEquals("ABC", builder.toString());

            // and when: joining more elements to the same builder
            builder.append("1");
            joiner.join(builder, "D", "E");
            // then
            assertEquals("ABC1DE", builder.toString());
        }
    }

    @Nested
    @DisplayName("Custom Joiner Behavior")
    class CustomJoinerTests {

        @Test
        @DisplayName("join() should append elements with a custom delimiter")
        void join_shouldAppendWithCustomDelimiter() {
            // given
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
            final StringBuilder builder = new StringBuilder("A");

            // when
            joiner.join(builder, "B", "C");
            // then
            assertEquals("AB.C", builder.toString());

            // and when: joining more elements to the same builder
            builder.append("1");
            joiner.join(builder, Arrays.asList("D", "E"));
            // then
            assertEquals("AB.C1D.E", builder.toString());
        }

        @Test
        @DisplayName("join() should respect all custom properties (prefix, suffix, delimiter)")
        void join_shouldRespectAllCustomProperties() {
            // given
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .get();
            final StringBuilder builder = new StringBuilder("A");

            // when
            joiner.join(builder, "B", "C");
            // then
            assertEquals("A<B.C>", builder.toString());

            // and when: joining more elements to the same builder
            builder.append("1");
            joiner.join(builder, Arrays.asList("D", "E"));
            // then
            assertEquals("A<B.C>1<D.E>", builder.toString());
        }

        @Test
        @DisplayName("join() should use a custom element appender")
        void join_shouldUseCustomElementAppender() {
            // given
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder()
                .setPrefix("<")
                .setDelimiter(".")
                .setSuffix(">")
                .setElementAppender((a, e) -> a.append("|").append(Objects.toString(e)))
                .get();
            final StringBuilder builder = new StringBuilder("A");

            // when
            joiner.join(builder, "B", "C");
            // then
            assertEquals("A<|B.|C>", builder.toString());

            // and when: joining more elements to the same builder
            builder.append("1");
            joiner.join(builder, Arrays.asList("D", "E"));
            // then
            assertEquals("A<|B.|C>1<|D.|E>", builder.toString());
        }

        @Test
        @DisplayName("join() should use a custom appender that calls a method on the element")
        void join_shouldUseCustomAppenderWithElementMethod() {
            // given
            final AppendableJoiner<Fixture> joiner = AppendableJoiner.<Fixture>builder()
                .setElementAppender((a, e) -> e.render(a))
                .get();
            final StringBuilder builder = new StringBuilder("[");

            // when
            joiner.join(builder, new Fixture("B"), new Fixture("C"));
            // then
            assertEquals("[B!C!", builder.toString());

            // and when: joining more elements to the same builder
            builder.append("]");
            joiner.join(builder, Arrays.asList(new Fixture("D"), new Fixture("E")));
            // then
            assertEquals("[B!C!]D!E!", builder.toString());
        }
    }

    @Nested
    @DisplayName("Appendable Type Compatibility")
    class AppendableTypeCompatibilityTests {

        @SuppressWarnings("deprecation") // Test own StrBuilder for compatibility
        @ParameterizedTest(name = "for {0}")
        @ValueSource(classes = { StringBuilder.class, StringBuffer.class, StringWriter.class, StrBuilder.class, TextStringBuilder.class })
        @DisplayName("joinA() should work correctly with various Appendable implementations")
        void joinA_should_workWithVariousAppendableTypes(final Class<? extends Appendable> clazz) throws Exception {
            // given
            final AppendableJoiner<Object> joiner = AppendableJoiner.builder().setDelimiter(".").get();
            final Appendable appendable = clazz.getDeclaredConstructor().newInstance();
            appendable.append("A");

            // when
            joiner.joinA(appendable, "B", "C");
            // then
            assertEquals("AB.C", appendable.toString());

            // and when: joining more elements to the same appendable
            appendable.append("1");
            joiner.joinA(appendable, Arrays.asList("D", "E"));
            // then
            assertEquals("AB.C1D.E", appendable.toString());
        }
    }
}