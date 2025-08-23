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

import static org.apache.commons.lang3.CharSetUtils.containsAny;
import static org.apache.commons.lang3.CharSetUtils.count;
import static org.apache.commons.lang3.CharSetUtils.delete;
import static org.apache.commons.lang3.CharSetUtils.keep;
import static org.apache.commons.lang3.CharSetUtils.squeeze;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for CharSetUtils.
 *
 * Notes on argument forms:
 * - Passing (String) null means a single null pattern element (varargs array of length 1, with null inside).
 * - Passing (String[]) null means the entire varargs array itself is null (no patterns provided).
 * - Passing no patterns at all means an empty varargs array.
 *
 * Char set syntax examples:
 * - "a-e" denotes a range a..e.
 * - "el" denotes the set {e, l}.
 * - "^l" denotes any character except 'l'.
 */
class CharSetUtilsTest extends AbstractLangTest {

    private static final String HELLO = "hello";
    private static final String EMPTY = "";
    private static final String RANGE_A_E = "a-e";
    private static final String RANGE_L_P = "l-p";
    private static final String EL = "el";

    @Test
    void constructorCharacteristics() {
        assertNotNull(new CharSetUtils(), "Public no-arg constructor should exist for tools");
        final Constructor<?>[] constructors = CharSetUtils.class.getDeclaredConstructors();
        assertEquals(1, constructors.length, "Utility class should have a single constructor");
        assertTrue(Modifier.isPublic(constructors[0].getModifiers()), "Constructor should be public (deprecated, for tools)");
        assertTrue(Modifier.isPublic(CharSetUtils.class.getModifiers()), "Class should be public");
        assertFalse(Modifier.isFinal(CharSetUtils.class.getModifiers()), "Class should not be final");
    }

    @Nested
    class ContainsAny {

        @Test
        void singlePatternArgument() {
            // null/empty inputs
            assertFalse(containsAny(null, (String) null));
            assertFalse(containsAny(null, EMPTY));

            assertFalse(containsAny(EMPTY, (String) null));
            assertFalse(containsAny(EMPTY, EMPTY));
            assertFalse(containsAny(EMPTY, RANGE_A_E));

            // regular inputs
            assertFalse(containsAny(HELLO, (String) null));
            assertFalse(containsAny(HELLO, EMPTY));
            assertTrue(containsAny(HELLO, RANGE_A_E));
            assertTrue(containsAny(HELLO, RANGE_L_P));
        }

        @Test
        void arrayArgumentAndVarargsBehaviors() {
            // Entire pattern array is null or empty
            assertFalse(containsAny(null, (String[]) null));
            assertFalse(containsAny(null)); // no patterns
            assertFalse(containsAny(null, (String) null));
            assertFalse(containsAny(null, RANGE_A_E));

            assertFalse(containsAny(EMPTY, (String[]) null));
            assertFalse(containsAny(EMPTY)); // no patterns
            assertFalse(containsAny(EMPTY, (String) null));
            assertFalse(containsAny(EMPTY, RANGE_A_E));

            assertFalse(containsAny(HELLO, (String[]) null));
            assertFalse(containsAny(HELLO)); // no patterns
            assertFalse(containsAny(HELLO, (String) null));
            assertTrue(containsAny(HELLO, RANGE_A_E));

            // multiple/typical cases
            assertTrue(containsAny(HELLO, EL));
            assertFalse(containsAny(HELLO, "x"));
            assertTrue(containsAny(HELLO, "e-i"));
            assertTrue(containsAny(HELLO, "a-z"));
            assertFalse(containsAny(HELLO, EMPTY));
        }
    }

    @Nested
    class Count {

        @Test
        void singlePatternArgument() {
            // null/empty inputs
            assertEquals(0, count(null, (String) null));
            assertEquals(0, count(null, EMPTY));

            assertEquals(0, count(EMPTY, (String) null));
            assertEquals(0, count(EMPTY, EMPTY));
            assertEquals(0, count(EMPTY, RANGE_A_E));

            // regular inputs
            assertEquals(0, count(HELLO, (String) null));
            assertEquals(0, count(HELLO, EMPTY));
            assertEquals(1, count(HELLO, RANGE_A_E));
            assertEquals(3, count(HELLO, RANGE_L_P));
        }

        @Test
        void arrayArgumentAndVarargsBehaviors() {
            // Entire pattern array is null or empty
            assertEquals(0, count(null, (String[]) null));
            assertEquals(0, count(null)); // no patterns
            assertEquals(0, count(null, (String) null));
            assertEquals(0, count(null, RANGE_A_E));

            assertEquals(0, count(EMPTY, (String[]) null));
            assertEquals(0, count(EMPTY)); // no patterns
            assertEquals(0, count(EMPTY, (String) null));
            assertEquals(0, count(EMPTY, RANGE_A_E));

            assertEquals(0, count(HELLO, (String[]) null));
            assertEquals(0, count(HELLO)); // no patterns
            assertEquals(0, count(HELLO, (String) null));
            assertEquals(1, count(HELLO, RANGE_A_E));

            // multiple/typical cases
            assertEquals(3, count(HELLO, EL));
            assertEquals(0, count(HELLO, "x"));
            assertEquals(2, count(HELLO, "e-i"));
            assertEquals(5, count(HELLO, "a-z"));
            assertEquals(0, count(HELLO, EMPTY));
        }
    }

    @Nested
    class Delete {

        @Test
        void singlePatternArgument() {
            // null/empty inputs
            assertNull(delete(null, (String) null));
            assertNull(delete(null, EMPTY));

            assertEquals(EMPTY, delete(EMPTY, (String) null));
            assertEquals(EMPTY, delete(EMPTY, EMPTY));
            assertEquals(EMPTY, delete(EMPTY, RANGE_A_E));

            // regular inputs
            assertEquals(HELLO, delete(HELLO, (String) null));
            assertEquals(HELLO, delete(HELLO, EMPTY));
            assertEquals("hllo", delete(HELLO, RANGE_A_E));
            assertEquals("he", delete(HELLO, RANGE_L_P));
            assertEquals(HELLO, delete(HELLO, "z"));
        }

        @Test
        void arrayArgumentAndVarargsBehaviors() {
            // Entire pattern array is null or empty
            assertNull(delete(null, (String[]) null));
            assertNull(delete(null)); // no patterns
            assertNull(delete(null, (String) null));
            assertNull(delete(null, EL));

            assertEquals(EMPTY, delete(EMPTY, (String[]) null));
            assertEquals(EMPTY, delete(EMPTY)); // no patterns
            assertEquals(EMPTY, delete(EMPTY, (String) null));
            assertEquals(EMPTY, delete(EMPTY, RANGE_A_E));

            assertEquals(HELLO, delete(HELLO, (String[]) null));
            assertEquals(HELLO, delete(HELLO)); // no patterns
            assertEquals(HELLO, delete(HELLO, (String) null));
            assertEquals(HELLO, delete(HELLO, "xyz"));

            // multiple/typical cases
            assertEquals("ho", delete(HELLO, EL));
            assertEquals(EMPTY, delete(HELLO, "elho"));
            assertEquals(HELLO, delete(HELLO, EMPTY));
            assertEquals(HELLO, delete(HELLO, EMPTY)); // repeat to mirror original test
            assertEquals(EMPTY, delete(HELLO, "a-z"));
            assertEquals(EMPTY, delete("----", "-"));
            assertEquals("heo", delete(HELLO, "l"));
        }
    }

    @Nested
    class Keep {

        @Test
        void singlePatternArgument() {
            // null/empty inputs
            assertNull(keep(null, (String) null));
            assertNull(keep(null, EMPTY));

            assertEquals(EMPTY, keep(EMPTY, (String) null));
            assertEquals(EMPTY, keep(EMPTY, EMPTY));
            assertEquals(EMPTY, keep(EMPTY, RANGE_A_E));

            // regular inputs
            assertEquals(EMPTY, keep(HELLO, (String) null));
            assertEquals(EMPTY, keep(HELLO, EMPTY));
            assertEquals(EMPTY, keep(HELLO, "xyz"));
            assertEquals(HELLO, keep(HELLO, "a-z"));
            assertEquals(HELLO, keep(HELLO, "oleh"));
            assertEquals("ell", keep(HELLO, EL));
        }

        @Test
        void arrayArgumentAndVarargsBehaviors() {
            // Entire pattern array is null or empty
            assertNull(keep(null, (String[]) null));
            assertNull(keep(null)); // no patterns
            assertNull(keep(null, (String) null));
            assertNull(keep(null, RANGE_A_E));

            assertEquals(EMPTY, keep(EMPTY, (String[]) null));
            assertEquals(EMPTY, keep(EMPTY)); // no patterns
            assertEquals(EMPTY, keep(EMPTY, (String) null));
            assertEquals(EMPTY, keep(EMPTY, RANGE_A_E));

            assertEquals(EMPTY, keep(HELLO, (String[]) null));
            assertEquals(EMPTY, keep(HELLO)); // no patterns
            assertEquals(EMPTY, keep(HELLO, (String) null));
            assertEquals("e", keep(HELLO, RANGE_A_E));

            // multiple/typical cases
            assertEquals("e", keep(HELLO, RANGE_A_E)); // duplicate to mirror original test
            assertEquals("ell", keep(HELLO, EL));
            assertEquals(HELLO, keep(HELLO, "elho"));
            assertEquals(HELLO, keep(HELLO, "a-z"));
            assertEquals("----", keep("----", "-"));
            assertEquals("ll", keep(HELLO, "l"));
        }
    }

    @Nested
    class Squeeze {

        @Test
        void singlePatternArgument() {
            // null/empty inputs
            assertNull(squeeze(null, (String) null));
            assertNull(squeeze(null, EMPTY));

            assertEquals(EMPTY, squeeze(EMPTY, (String) null));
            assertEquals(EMPTY, squeeze(EMPTY, EMPTY));
            assertEquals(EMPTY, squeeze(EMPTY, RANGE_A_E));

            // regular inputs
            assertEquals(HELLO, squeeze(HELLO, (String) null));
            assertEquals(HELLO, squeeze(HELLO, EMPTY));
            assertEquals(HELLO, squeeze(HELLO, RANGE_A_E));
            assertEquals("helo", squeeze(HELLO, RANGE_L_P));
            assertEquals("heloo", squeeze("helloo", "l"));
            assertEquals(HELLO, squeeze("helloo", "^l"));
        }

        @Test
        void arrayArgumentAndVarargsBehaviors() {
            // Entire pattern array is null or empty
            assertNull(squeeze(null, (String[]) null));
            assertNull(squeeze(null)); // no patterns
            assertNull(squeeze(null, (String) null));
            assertNull(squeeze(null, EL));

            assertEquals(EMPTY, squeeze(EMPTY, (String[]) null));
            assertEquals(EMPTY, squeeze(EMPTY)); // no patterns
            assertEquals(EMPTY, squeeze(EMPTY, (String) null));
            assertEquals(EMPTY, squeeze(EMPTY, RANGE_A_E));

            assertEquals(HELLO, squeeze(HELLO, (String[]) null));
            assertEquals(HELLO, squeeze(HELLO)); // no patterns
            assertEquals(HELLO, squeeze(HELLO, (String) null));
            assertEquals(HELLO, squeeze(HELLO, RANGE_A_E));

            // multiple/typical cases
            assertEquals("helo", squeeze(HELLO, EL));
            assertEquals(HELLO, squeeze(HELLO, "e"));
            assertEquals("fofof", squeeze("fooffooff", "of"));
            assertEquals("fof", squeeze("fooooff", "fo"));
        }
    }
}