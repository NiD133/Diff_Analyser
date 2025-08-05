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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link CharSetUtils}.
 */
class CharSetUtilsTest extends AbstractLangTest {

    @Test
    void constructor_IsPublicAndNotFinal() throws NoSuchMethodException {
        // Verify the constructor is public and class is non-final
        final Constructor<?>[] cons = CharSetUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(CharSetUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(CharSetUtils.class.getModifiers()));
        
        // Ensure instantiation works
        assertNotNull(new CharSetUtils());
    }

    @Nested
    class ContainsAnyTest {
        @ParameterizedTest
        @MethodSource("testCases")
        void testContainsAny(String input, String[] set, boolean expected) {
            assertEquals(expected, CharSetUtils.containsAny(input, set));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                // Null and empty inputs
                arguments(null, null, false),
                arguments(null, new String[]{}, false),
                arguments(null, new String[]{""}, false),
                arguments(null, new String[]{"a-e"}, false),
                arguments("", null, false),
                arguments("", new String[]{}, false),
                arguments("", new String[]{""}, false),
                arguments("", new String[]{"a-e"}, false),
                // Valid inputs
                arguments("hello", null, false),
                arguments("hello", new String[]{}, false),
                arguments("hello", new String[]{""}, false),
                arguments("hello", new String[]{"a-e"}, true),
                arguments("hello", new String[]{"l-p"}, true),
                arguments("hello", new String[]{"el"}, true),
                arguments("hello", new String[]{"x"}, false),
                arguments("hello", new String[]{"e-i"}, true),
                arguments("hello", new String[]{"a-z"}, true),
                arguments("hello", new String[]{""}, false)
            );
        }
    }

    @Nested
    class CountTest {
        @ParameterizedTest
        @MethodSource("testCases")
        void testCount(String input, String[] set, int expected) {
            assertEquals(expected, CharSetUtils.count(input, set));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                // Null and empty inputs
                arguments(null, null, 0),
                arguments(null, new String[]{}, 0),
                arguments(null, new String[]{null}, 0),
                arguments(null, new String[]{"a-e"}, 0),
                arguments("", null, 0),
                arguments("", new String[]{}, 0),
                arguments("", new String[]{null}, 0),
                arguments("", new String[]{"a-e"}, 0),
                // Valid inputs
                arguments("hello", null, 0),
                arguments("hello", new String[]{}, 0),
                arguments("hello", new String[]{null}, 0),
                arguments("hello", new String[]{"a-e"}, 1),
                arguments("hello", new String[]{"l-p"}, 3),
                arguments("hello", new String[]{"el"}, 3),
                arguments("hello", new String[]{"x"}, 0),
                arguments("hello", new String[]{"e-i"}, 2),
                arguments("hello", new String[]{"a-z"}, 5),
                arguments("hello", new String[]{""}, 0)
            );
        }
    }

    @Nested
    class DeleteTest {
        @ParameterizedTest
        @MethodSource("testCases")
        void testDelete(String input, String[] set, String expected) {
            assertEquals(expected, CharSetUtils.delete(input, set));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                // Null and empty inputs
                arguments(null, null, null),
                arguments(null, new String[]{}, null),
                arguments(null, new String[]{null}, null),
                arguments(null, new String[]{"el"}, null),
                arguments("", null, ""),
                arguments("", new String[]{}, ""),
                arguments("", new String[]{null}, ""),
                arguments("", new String[]{"a-e"}, ""),
                // Valid inputs
                arguments("hello", null, "hello"),
                arguments("hello", new String[]{}, "hello"),
                arguments("hello", new String[]{null}, "hello"),
                arguments("hello", new String[]{"xyz"}, "hello"),
                arguments("hello", new String[]{"a-e"}, "hllo"),
                arguments("hello", new String[]{"l-p"}, "he"),
                arguments("hello", new String[]{"z"}, "hello"),
                arguments("hello", new String[]{"el"}, "ho"),
                arguments("hello", new String[]{"elho"}, ""),
                arguments("hello", new String[]{""}, "hello"),
                arguments("hello", new String[]{"a-z"}, ""),
                arguments("----", new String[]{"-"}, ""),
                arguments("hello", new String[]{"l"}, "heo")
            );
        }
    }

    @Nested
    class KeepTest {
        @ParameterizedTest
        @MethodSource("testCases")
        void testKeep(String input, String[] set, String expected) {
            assertEquals(expected, CharSetUtils.keep(input, set));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                // Null and empty inputs
                arguments(null, null, null),
                arguments(null, new String[]{}, null),
                arguments(null, new String[]{null}, null),
                arguments(null, new String[]{"a-e"}, null),
                arguments("", null, ""),
                arguments("", new String[]{}, ""),
                arguments("", new String[]{null}, ""),
                arguments("", new String[]{"a-e"}, ""),
                // Valid inputs
                arguments("hello", null, ""),
                arguments("hello", new String[]{}, ""),
                arguments("hello", new String[]{null}, ""),
                arguments("hello", new String[]{"xyz"}, ""),
                arguments("hello", new String[]{"a-e"}, "e"),
                arguments("hello", new String[]{"a-z"}, "hello"),
                arguments("hello", new String[]{"elho"}, "hello"),
                arguments("hello", new String[]{"el"}, "ell"),
                arguments("----", new String[]{"-"}, "----"),
                arguments("hello", new String[]{"l"}, "ll")
            );
        }
    }

    @Nested
    class SqueezeTest {
        @ParameterizedTest
        @MethodSource("testCases")
        void testSqueeze(String input, String[] set, String expected) {
            assertEquals(expected, CharSetUtils.squeeze(input, set));
        }

        private static Stream<Arguments> testCases() {
            return Stream.of(
                // Null and empty inputs
                arguments(null, null, null),
                arguments(null, new String[]{}, null),
                arguments(null, new String[]{""}, null),
                arguments(null, new String[]{"el"}, null),
                arguments("", null, ""),
                arguments("", new String[]{}, ""),
                arguments("", new String[]{""}, ""),
                arguments("", new String[]{"a-e"}, ""),
                // Valid inputs
                arguments("hello", null, "hello"),
                arguments("hello", new String[]{}, "hello"),
                arguments("hello", new String[]{""}, "hello"),
                arguments("hello", new String[]{"a-e"}, "hello"),
                arguments("hello", new String[]{"l-p"}, "helo"),
                arguments("helloo", new String[]{"l"}, "heloo"),
                arguments("helloo", new String[]{"^l"}, "hello"),
                arguments("hello", new String[]{"el"}, "helo"),
                arguments("hello", new String[]{"e"}, "hello"),
                arguments("fooffooff", new String[]{"of"}, "fofof"),
                arguments("fooooff", new String[]{"fo"}, "fof")
            );
        }
    }
}