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

package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link LangCollectors}.
 *
 * <p>This revised test suite uses a nested structure and parameterized tests
 * to improve readability and reduce code duplication.</p>
 */
class LangCollectorsTest {

    private static final class Fixture {
        private final int value;

        private Fixture(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    private static final Long ONE_L = 1L;
    private static final Long TWO_L = 2L;
    private static final Long THREE_L = 3L;

    @Nested
    @DisplayName("collect(Collector, T...) helper method")
    class CollectHelperTests {

        @Test
        @DisplayName("should return the collector's empty result when given a null array")
        void shouldReturnEmptyResultForNullArray() {
            final Collector<Object, ?, String> collector = LangCollectors.joining("-", "<", ">");
            // The cast is needed to resolve varargs ambiguity with a single null argument
            assertEquals("<>", LangCollectors.collect(collector, (Object[]) null));
        }

        @Test
        @DisplayName("should correctly collect elements from a non-null array")
        void shouldCollectFromNonNullArray() {
            final Collector<Object, ?, String> collector = LangCollectors.joining("-");
            final String result = LangCollectors.collect(collector, ONE_L, TWO_L, THREE_L);
            assertEquals("1-2-3", result);
        }
    }

    @Nested
    @DisplayName("joining() collector")
    class JoiningCollectorTests {

        @Nested
        @DisplayName("with no arguments")
        class WithNoArguments {
            private final Collector<Object, ?, String> collector = LangCollectors.joining();

            private static Stream<Arguments> joiningSource() {
                return Stream.of(
                    Arguments.of("empty stream", new Object[]{}, ""),
                    Arguments.of("single string", new Object[]{"1"}, "1"),
                    Arguments.of("multiple strings", new Object[]{"1", "2", "3"}, "123"),
                    Arguments.of("strings with null", new Object[]{"1", null, "3"}, "1null3"),
                    Arguments.of("multiple longs", new Object[]{ONE_L, TWO_L, THREE_L}, "123"),
                    Arguments.of("longs with null", new Object[]{ONE_L, null, THREE_L}, "1null3"),
                    Arguments.of("custom objects", new Object[]{new Fixture(1), new Fixture(2)}, "12")
                );
            }

            @ParameterizedTest(name = "for {0}")
            @MethodSource("joiningSource")
            void shouldJoinElementsWithoutDelimiter(final String description, final Object[] input, final String expected) {
                assertEquals(expected, Stream.of(input).collect(collector));
            }
        }

        @Nested
        @DisplayName("with delimiter")
        class WithDelimiter {
            private final Collector<Object, ?, String> collector = LangCollectors.joining("-");

            private static Stream<Arguments> joiningSource() {
                return Stream.of(
                    Arguments.of("empty stream", new Object[]{}, ""),
                    Arguments.of("single string", new Object[]{"1"}, "1"),
                    Arguments.of("multiple strings", new Object[]{"1", "2", "3"}, "1-2-3"),
                    Arguments.of("strings with null", new Object[]{"1", null, "3"}, "1-null-3"),
                    Arguments.of("multiple longs", new Object[]{ONE_L, TWO_L, THREE_L}, "1-2-3"),
                    Arguments.of("longs with null", new Object[]{ONE_L, null, THREE_L}, "1-null-3"),
                    Arguments.of("custom objects", new Object[]{new Fixture(1), new Fixture(2)}, "1-2"),
                    Arguments.of("atomic longs", new Object[]{new AtomicLong(1), new AtomicLong(2)}, "1-2")
                );
            }

            @ParameterizedTest(name = "for {0}")
            @MethodSource("joiningSource")
            void shouldJoinElementsWithDelimiter(final String description, final Object[] input, final String expected) {
                assertEquals(expected, Stream.of(input).collect(collector));
            }
        }

        @Nested
        @DisplayName("with delimiter, prefix, and suffix")
        class WithDelimiterPrefixAndSuffix {
            private final Collector<Object, ?, String> collector = LangCollectors.joining("-", "<", ">");

            private static Stream<Arguments> joiningSource() {
                return Stream.of(
                    Arguments.of("empty stream", new Object[]{}, "<>"),
                    Arguments.of("single string", new Object[]{"1"}, "<1>"),
                    Arguments.of("multiple strings", new Object[]{"1", "2", "3"}, "<1-2-3>"),
                    Arguments.of("strings with null", new Object[]{"1", null, "3"}, "<1-null-3>"),
                    Arguments.of("multiple longs", new Object[]{ONE_L, TWO_L, THREE_L}, "<1-2-3>"),
                    Arguments.of("longs with null", new Object[]{ONE_L, null, THREE_L}, "<1-null-3>"),
                    Arguments.of("custom objects", new Object[]{new Fixture(1), new Fixture(2)}, "<1-2>")
                );
            }

            @ParameterizedTest(name = "for {0}")
            @MethodSource("joiningSource")
            void shouldJoinElementsWithDelimiterPrefixAndSuffix(final String description, final Object[] input, final String expected) {
                assertEquals(expected, Stream.of(input).collect(collector));
            }
        }

        @Nested
        @DisplayName("with delimiter, prefix, suffix, and custom toString")
        class WithDelimiterPrefixSuffixAndToString {
            
            @Test
            @DisplayName("should behave like 3-argument version when using Objects::toString")
            void shouldBehaveAs3ArgVersionWithDefaultToString() {
                final Collector<Object, ?, String> collector = LangCollectors.joining("-", "<", ">", Objects::toString);
                assertEquals("<>", Stream.of().collect(collector));
                assertEquals("<1-2-3>", Stream.of(ONE_L, TWO_L, THREE_L).collect(collector));
                assertEquals("<1-null-3>", Stream.of(ONE_L, null, THREE_L).collect(collector));
            }

            @Test
            @DisplayName("should use custom function to represent null elements")
            void shouldUseCustomFunctionForNulls() {
                final Collector<Object, ?, String> collectorWithNullHandler = LangCollectors.joining("-", "<", ">", o -> Objects.toString(o, "NUL"));
                
                assertEquals("<1-NUL-3>", Stream.of(ONE_L, null, THREE_L).collect(collectorWithNullHandler));
                assertEquals("<1-NUL-3>", Stream.of("1", null, "3").collect(collectorWithNullHandler));
            }
        }
    }
}