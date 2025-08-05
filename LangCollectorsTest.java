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

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link LangCollectors}
 */
class LangCollectorsTest {

    /**
     * Test fixture class to verify toString() behavior with custom objects
     */
    private static final class TestObject {
        final int value;

        private TestObject(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    // Test data constants
    private static final Long ONE = Long.valueOf(1);
    private static final Long TWO = Long.valueOf(2);
    private static final Long THREE = Long.valueOf(3);

    // Collector configurations for different joining scenarios
    private static final Collector<Object, ?, String> NO_DELIMITER_COLLECTOR = LangCollectors.joining();
    private static final Collector<Object, ?, String> DASH_DELIMITER_COLLECTOR = LangCollectors.joining("-");
    private static final Collector<Object, ?, String> WITH_PREFIX_SUFFIX_COLLECTOR = LangCollectors.joining("-", "<", ">");
    private static final Collector<Object, ?, String> WITH_CUSTOM_TO_STRING_COLLECTOR = 
        LangCollectors.joining("-", "<", ">", Objects::toString);
    private static final Collector<Object, ?, String> WITH_NULL_REPLACEMENT_COLLECTOR = 
        LangCollectors.joining("-", "<", ">", obj -> Objects.toString(obj, "NULL"));

    // Helper methods for testing different collector configurations
    private String joinWithNoDelimiter(final Object... objects) {
        return LangCollectors.collect(NO_DELIMITER_COLLECTOR, objects);
    }

    private String joinWithDashDelimiter(final Object... objects) {
        return LangCollectors.collect(DASH_DELIMITER_COLLECTOR, objects);
    }

    private String joinWithPrefixAndSuffix(final Object... objects) {
        return LangCollectors.collect(WITH_PREFIX_SUFFIX_COLLECTOR, objects);
    }

    private String joinWithCustomToString(final Object... objects) {
        return LangCollectors.collect(WITH_CUSTOM_TO_STRING_COLLECTOR, objects);
    }

    private String joinWithNullReplacement(final Object... objects) {
        return LangCollectors.collect(WITH_NULL_REPLACEMENT_COLLECTOR, objects);
    }

    // Tests for joining with no delimiter (concatenation only)
    @Test
    void testJoiningWithNoDelimiter_UsingCollectMethod() {
        assertEquals("", joinWithNoDelimiter());
        assertEquals("1", joinWithNoDelimiter("1"));
        assertEquals("12", joinWithNoDelimiter("1", "2"));
        assertEquals("123", joinWithNoDelimiter("1", "2", "3"));
        assertEquals("1null3", joinWithNoDelimiter("1", null, "3"));
    }

    @Test
    void testJoiningWithNoDelimiter_NonStringObjects() {
        assertEquals("", joinWithNoDelimiter());
        assertEquals("1", joinWithNoDelimiter(ONE));
        assertEquals("12", joinWithNoDelimiter(ONE, TWO));
        assertEquals("123", joinWithNoDelimiter(ONE, TWO, THREE));
        assertEquals("1null3", joinWithNoDelimiter(ONE, null, THREE));
        assertEquals("12", joinWithNoDelimiter(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("12", joinWithNoDelimiter(new TestObject(1), new TestObject(2)));
    }

    @Test
    void testJoiningWithNoDelimiter_UsingStreamDirectly() {
        assertEquals("", Stream.of().collect(NO_DELIMITER_COLLECTOR));
        assertEquals("1", Stream.of(ONE).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("12", Stream.of(ONE, TWO).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("123", Stream.of(ONE, TWO, THREE).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("1null3", Stream.of(ONE, null, THREE).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("12", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("12", Stream.of(new TestObject(1), new TestObject(2)).collect(NO_DELIMITER_COLLECTOR));
    }

    @Test
    void testJoiningWithNoDelimiter_UsingArrayStream() {
        assertEquals("", Arrays.stream(new Object[] {}).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("1", Arrays.stream(new Long[] { ONE }).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("12", Arrays.stream(new Long[] { ONE, TWO }).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("123", Arrays.stream(new Long[] { ONE, TWO, THREE }).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("1null3", Arrays.stream(new Long[] { ONE, null, THREE }).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("12", Arrays.stream(new AtomicLong[] { new AtomicLong(1), new AtomicLong(2) }).collect(NO_DELIMITER_COLLECTOR));
        assertEquals("12", Arrays.stream(new TestObject[] { new TestObject(1), new TestObject(2) }).collect(NO_DELIMITER_COLLECTOR));
    }

    // Tests for joining with delimiter
    @Test
    void testJoiningWithDelimiter_Strings() {
        assertEquals("", joinWithDashDelimiter());
        assertEquals("1", joinWithDashDelimiter("1"));
        assertEquals("1-2", joinWithDashDelimiter("1", "2"));
        assertEquals("1-2-3", joinWithDashDelimiter("1", "2", "3"));
        assertEquals("1-null-3", joinWithDashDelimiter("1", null, "3"));
    }

    @Test
    void testJoiningWithDelimiter_NonStringObjects() {
        assertEquals("", joinWithDashDelimiter());
        assertEquals("1", joinWithDashDelimiter(ONE));
        assertEquals("1-2", joinWithDashDelimiter(ONE, TWO));
        assertEquals("1-2-3", joinWithDashDelimiter(ONE, TWO, THREE));
        assertEquals("1-null-3", joinWithDashDelimiter(ONE, null, THREE));
        assertEquals("1-2", joinWithDashDelimiter(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("1-2", joinWithDashDelimiter(new TestObject(1), new TestObject(2)));
    }

    @Test
    void testJoiningWithDelimiter_UsingStreamDirectly() {
        assertEquals("", Stream.of().collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1", Stream.of(ONE).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2", Stream.of(ONE, TWO).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2-3", Stream.of(ONE, TWO, THREE).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-null-3", Stream.of(ONE, null, THREE).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2", Stream.of(new TestObject(1), new TestObject(2)).collect(DASH_DELIMITER_COLLECTOR));
    }

    @Test
    void testJoiningWithDelimiter_UsingArrayStream() {
        assertEquals("", Arrays.stream(new Object[] {}).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1", Arrays.stream(new Long[] { ONE }).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2", Arrays.stream(new Long[] { ONE, TWO }).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2-3", Arrays.stream(new Long[] { ONE, TWO, THREE }).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-null-3", Arrays.stream(new Long[] { ONE, null, THREE }).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2", Arrays.stream(new AtomicLong[] { new AtomicLong(1), new AtomicLong(2) }).collect(DASH_DELIMITER_COLLECTOR));
        assertEquals("1-2", Arrays.stream(new TestObject[] { new TestObject(1), new TestObject(2) }).collect(DASH_DELIMITER_COLLECTOR));
    }

    // Tests for joining with delimiter, prefix, and suffix
    @Test
    void testJoiningWithPrefixAndSuffix_Strings() {
        assertEquals("<>", joinWithPrefixAndSuffix());
        assertEquals("<1>", joinWithPrefixAndSuffix("1"));
        assertEquals("<1-2>", joinWithPrefixAndSuffix("1", "2"));
        assertEquals("<1-2-3>", joinWithPrefixAndSuffix("1", "2", "3"));
        assertEquals("<1-null-3>", joinWithPrefixAndSuffix("1", null, "3"));
    }

    @Test
    void testJoiningWithPrefixAndSuffix_NonStringObjects() {
        assertEquals("<>", joinWithPrefixAndSuffix());
        assertEquals("<1>", joinWithPrefixAndSuffix(ONE));
        assertEquals("<1-2>", joinWithPrefixAndSuffix(ONE, TWO));
        assertEquals("<1-2-3>", joinWithPrefixAndSuffix(ONE, TWO, THREE));
        assertEquals("<1-null-3>", joinWithPrefixAndSuffix(ONE, null, THREE));
        assertEquals("<1-2>", joinWithPrefixAndSuffix(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("<1-2>", joinWithPrefixAndSuffix(new TestObject(1), new TestObject(2)));
    }

    @Test
    void testJoiningWithPrefixAndSuffix_UsingStreamDirectly() {
        assertEquals("<>", Stream.of().collect(WITH_PREFIX_SUFFIX_COLLECTOR));
        assertEquals("<1>", Stream.of(ONE).collect(WITH_PREFIX_SUFFIX_COLLECTOR));
        assertEquals("<1-2>", Stream.of(ONE, TWO).collect(WITH_PREFIX_SUFFIX_COLLECTOR));
        assertEquals("<1-2-3>", Stream.of(ONE, TWO, THREE).collect(WITH_PREFIX_SUFFIX_COLLECTOR));
        assertEquals("<1-null-3>", Stream.of(ONE, null, THREE).collect(WITH_PREFIX_SUFFIX_COLLECTOR));
        assertEquals("<1-2>", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(WITH_PREFIX_SUFFIX_COLLECTOR));
        assertEquals("<1-2>", Stream.of(new TestObject(1), new TestObject(2)).collect(WITH_PREFIX_SUFFIX_COLLECTOR));
    }

    // Tests for joining with custom toString function
    @Test
    void testJoiningWithCustomToStringFunction_Strings() {
        assertEquals("<>", joinWithCustomToString());
        assertEquals("<1>", joinWithCustomToString("1"));
        assertEquals("<1-2>", joinWithCustomToString("1", "2"));
        assertEquals("<1-2-3>", joinWithCustomToString("1", "2", "3"));
        assertEquals("<1-null-3>", joinWithCustomToString("1", null, "3"));
        assertEquals("<1-NULL-3>", joinWithNullReplacement("1", null, "3"));
    }

    @Test
    void testJoiningWithCustomToStringFunction_NonStringObjects() {
        assertEquals("<>", joinWithCustomToString());
        assertEquals("<1>", joinWithCustomToString(ONE));
        assertEquals("<1-2>", joinWithCustomToString(ONE, TWO));
        assertEquals("<1-2-3>", joinWithCustomToString(ONE, TWO, THREE));
        assertEquals("<1-null-3>", joinWithCustomToString(ONE, null, THREE));
        assertEquals("<1-NULL-3>", joinWithNullReplacement(ONE, null, THREE));
        assertEquals("<1-2>", joinWithCustomToString(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("<1-2>", joinWithCustomToString(new TestObject(1), new TestObject(2)));
    }

    @Test
    void testJoiningWithCustomToStringFunction_UsingStreamDirectly() {
        assertEquals("<>", Stream.of().collect(WITH_CUSTOM_TO_STRING_COLLECTOR));
        assertEquals("<1>", Stream.of(ONE).collect(WITH_CUSTOM_TO_STRING_COLLECTOR));
        assertEquals("<1-2>", Stream.of(ONE, TWO).collect(WITH_CUSTOM_TO_STRING_COLLECTOR));
        assertEquals("<1-2-3>", Stream.of(ONE, TWO, THREE).collect(WITH_CUSTOM_TO_STRING_COLLECTOR));
        assertEquals("<1-null-3>", Stream.of(ONE, null, THREE).collect(WITH_CUSTOM_TO_STRING_COLLECTOR));
        assertEquals("<1-NULL-3>", Stream.of(ONE, null, THREE).collect(WITH_NULL_REPLACEMENT_COLLECTOR));
        assertEquals("<1-2>", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(WITH_CUSTOM_TO_STRING_COLLECTOR));
        assertEquals("<1-2>", Stream.of(new TestObject(1), new TestObject(2)).collect(WITH_CUSTOM_TO_STRING_COLLECTOR));
    }

    // Tests for edge cases
    @Test
    void testJoiningWithNullArrays() {
        assertEquals("", joinWithNoDelimiter((Object[]) null));
        assertEquals("", joinWithDashDelimiter((Object[]) null));
        assertEquals("<>", joinWithPrefixAndSuffix((Object[]) null));
        assertEquals("<>", joinWithNullReplacement((Object[]) null));
    }
}