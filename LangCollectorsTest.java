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

    private static final class Fixture {
        int value;

        Fixture(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    private static final Long ONE = 1L;
    private static final Long TWO = 2L;
    private static final Long THREE = 3L;

    private static final Function<Object, String> TO_STRING = Objects::toString;
    private static final Function<Object, String> NULL_TO_STRING = o -> Objects.toString(o, "NUL");

    private static final Collector<Object, ?, String> NO_DELIMITER = LangCollectors.joining();
    private static final Collector<Object, ?, String> DELIMITER_ONLY = LangCollectors.joining("-");
    private static final Collector<Object, ?, String> DELIMITER_PREFIX_SUFFIX = LangCollectors.joining("-", "<", ">");
    private static final Collector<Object, ?, String> DELIMITER_PREFIX_SUFFIX_MAPPER = LangCollectors.joining("-", "<", ">", TO_STRING);
    private static final Collector<Object, ?, String> DELIMITER_PREFIX_SUFFIX_NULL_MAPPER = LangCollectors.joining("-", "<", ">", NULL_TO_STRING);

    private static final String[] STRINGS = {"1", "2", "3"};
    private static final Long[] LONGS = {ONE, TWO, THREE};
    private static final Fixture[] FIXTURES = {new Fixture(1), new Fixture(2)};
    private static final AtomicLong[] ATOMIC_LONGS = {new AtomicLong(1), new AtomicLong(2)};

    private String collectWithNoDelimiter(final Object... objects) {
        return LangCollectors.collect(NO_DELIMITER, objects);
    }

    private String collectWithDelimiterOnly(final Object... objects) {
        return LangCollectors.collect(DELIMITER_ONLY, objects);
    }

    private String collectWithDelimiterPrefixSuffix(final Object... objects) {
        return LangCollectors.collect(DELIMITER_PREFIX_SUFFIX, objects);
    }

    private String collectWithDelimiterPrefixSuffixMapper(final Object... objects) {
        return LangCollectors.collect(DELIMITER_PREFIX_SUFFIX_MAPPER, objects);
    }

    private String collectWithDelimiterPrefixSuffixNullMapper(final Object... objects) {
        return LangCollectors.collect(DELIMITER_PREFIX_SUFFIX_NULL_MAPPER, objects);
    }

    @Test
    void testCollectorWithNoDelimiter_Strings() {
        assertEquals("", collectWithNoDelimiter());
        assertEquals("1", collectWithNoDelimiter(STRINGS[0]));
        assertEquals("12", collectWithNoDelimiter(STRINGS[0], STRINGS[1]));
        assertEquals("123", collectWithNoDelimiter(STRINGS));
        assertEquals("1null3", collectWithNoDelimiter("1", null, "3"));
    }

    @Test
    void testCollectorWithNoDelimiter_NonStrings() {
        assertEquals("", collectWithNoDelimiter());
        assertEquals("1", collectWithNoDelimiter(LONGS[0]));
        assertEquals("12", collectWithNoDelimiter(LONGS[0], LONGS[1]));
        assertEquals("123", collectWithNoDelimiter(LONGS));
        assertEquals("1null3", collectWithNoDelimiter(ONE, null, THREE));
        assertEquals("12", collectWithNoDelimiter(ATOMIC_LONGS));
        assertEquals("12", collectWithNoDelimiter(FIXTURES));
    }

    @Test
    void testCollectorWithDelimiterOnly_Strings() {
        assertEquals("", collectWithDelimiterOnly());
        assertEquals("1", collectWithDelimiterOnly(STRINGS[0]));
        assertEquals("1-2", collectWithDelimiterOnly(STRINGS[0], STRINGS[1]));
        assertEquals("1-2-3", collectWithDelimiterOnly(STRINGS));
        assertEquals("1-null-3", collectWithDelimiterOnly("1", null, "3"));
    }

    @Test
    void testCollectorWithDelimiterOnly_NonStrings() {
        assertEquals("", collectWithDelimiterOnly());
        assertEquals("1", collectWithDelimiterOnly(LONGS[0]));
        assertEquals("1-2", collectWithDelimiterOnly(LONGS[0], LONGS[1]));
        assertEquals("1-2-3", collectWithDelimiterOnly(LONGS));
        assertEquals("1-null-3", collectWithDelimiterOnly(ONE, null, THREE));
        assertEquals("1-2", collectWithDelimiterOnly(ATOMIC_LONGS));
        assertEquals("1-2", collectWithDelimiterOnly(FIXTURES));
    }

    @Test
    void testCollectorWithDelimiterPrefixSuffix_Strings() {
        assertEquals("<>", collectWithDelimiterPrefixSuffix());
        assertEquals("<1>", collectWithDelimiterPrefixSuffix(STRINGS[0]));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffix(STRINGS[0], STRINGS[1]));
        assertEquals("<1-2-3>", collectWithDelimiterPrefixSuffix(STRINGS));
        assertEquals("<1-null-3>", collectWithDelimiterPrefixSuffix("1", null, "3"));
    }

    @Test
    void testCollectorWithDelimiterPrefixSuffix_NonStrings() {
        assertEquals("<>", collectWithDelimiterPrefixSuffix());
        assertEquals("<1>", collectWithDelimiterPrefixSuffix(LONGS[0]));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffix(LONGS[0], LONGS[1]));
        assertEquals("<1-2-3>", collectWithDelimiterPrefixSuffix(LONGS));
        assertEquals("<1-null-3>", collectWithDelimiterPrefixSuffix(ONE, null, THREE));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffix(ATOMIC_LONGS));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffix(FIXTURES));
    }

    @Test
    void testCollectorWithDelimiterPrefixSuffixMapper_Strings() {
        assertEquals("<>", collectWithDelimiterPrefixSuffixMapper());
        assertEquals("<1>", collectWithDelimiterPrefixSuffixMapper(STRINGS[0]));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffixMapper(STRINGS[0], STRINGS[1]));
        assertEquals("<1-2-3>", collectWithDelimiterPrefixSuffixMapper(STRINGS));
        assertEquals("<1-null-3>", collectWithDelimiterPrefixSuffixMapper("1", null, "3"));
        assertEquals("<1-NUL-3>", collectWithDelimiterPrefixSuffixNullMapper("1", null, "3"));
    }

    @Test
    void testCollectorWithDelimiterPrefixSuffixMapper_NonStrings() {
        assertEquals("<>", collectWithDelimiterPrefixSuffixMapper());
        assertEquals("<1>", collectWithDelimiterPrefixSuffixMapper(LONGS[0]));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffixMapper(LONGS[0], LONGS[1]));
        assertEquals("<1-2-3>", collectWithDelimiterPrefixSuffixMapper(LONGS));
        assertEquals("<1-null-3>", collectWithDelimiterPrefixSuffixMapper(ONE, null, THREE));
        assertEquals("<1-NUL-3>", collectWithDelimiterPrefixSuffixNullMapper(ONE, null, THREE));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffixMapper(ATOMIC_LONGS));
        assertEquals("<1-2>", collectWithDelimiterPrefixSuffixMapper(FIXTURES));
    }

    @Test
    void testCollectorWithNullArrayInput() {
        assertEquals("", collectWithNoDelimiter((Object[]) null));
        assertEquals("", collectWithDelimiterOnly((Object[]) null));
        assertEquals("<>", collectWithDelimiterPrefixSuffix((Object[]) null));
        assertEquals("<>", collectWithDelimiterPrefixSuffixNullMapper((Object[]) null));
    }

    @Test
    void testStreamCollectorWithNoDelimiter_Strings() {
        assertEquals("", Stream.of().collect(NO_DELIMITER));
        assertEquals("1", Stream.of("1").collect(NO_DELIMITER));
        assertEquals("12", Stream.of("1", "2").collect(NO_DELIMITER));
        assertEquals("123", Stream.of(STRINGS).collect(NO_DELIMITER));
        assertEquals("1null3", Stream.of("1", null, "3").collect(NO_DELIMITER));
    }

    @Test
    void testStreamCollectorWithNoDelimiter_NonStrings() {
        assertEquals("", Stream.of().collect(NO_DELIMITER));
        assertEquals("1", Stream.of(ONE).collect(NO_DELIMITER));
        assertEquals("12", Stream.of(ONE, TWO).collect(NO_DELIMITER));
        assertEquals("123", Stream.of(LONGS).collect(NO_DELIMITER));
        assertEquals("1null3", Stream.of(ONE, null, THREE).collect(NO_DELIMITER));
        assertEquals("12", Stream.of(ATOMIC_LONGS).collect(NO_DELIMITER));
        assertEquals("12", Stream.of(FIXTURES).collect(NO_DELIMITER));
    }

    @Test
    void testStreamCollectorWithDelimiterOnly_Strings() {
        assertEquals("", Stream.of().collect(DELIMITER_ONLY));
        assertEquals("1", Stream.of("1").collect(DELIMITER_ONLY));
        assertEquals("1-2", Stream.of("1", "2").collect(DELIMITER_ONLY));
        assertEquals("1-2-3", Stream.of(STRINGS).collect(DELIMITER_ONLY));
        assertEquals("1-null-3", Stream.of("1", null, "3").collect(DELIMITER_ONLY));
    }

    @Test
    void testStreamCollectorWithDelimiterOnly_NonStrings() {
        assertEquals("", Stream.of().collect(DELIMITER_ONLY));
        assertEquals("1", Stream.of(ONE).collect(DELIMITER_ONLY));
        assertEquals("1-2", Stream.of(ONE, TWO).collect(DELIMITER_ONLY));
        assertEquals("1-2-3", Stream.of(LONGS).collect(DELIMITER_ONLY));
        assertEquals("1-null-3", Stream.of(ONE, null, THREE).collect(DELIMITER_ONLY));
        assertEquals("1-2", Stream.of(ATOMIC_LONGS).collect(DELIMITER_ONLY));
        assertEquals("1-2", Stream.of(FIXTURES).collect(DELIMITER_ONLY));
    }

    @Test
    void testStreamCollectorWithDelimiterPrefixSuffix_Strings() {
        assertEquals("<>", Stream.of().collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1>", Stream.of("1").collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-2>", Stream.of("1", "2").collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-2-3>", Stream.of(STRINGS).collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-null-3>", Stream.of("1", null, "3").collect(DELIMITER_PREFIX_SUFFIX));
    }

    @Test
    void testStreamCollectorWithDelimiterPrefixSuffix_NonStrings() {
        assertEquals("<>", Stream.of().collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1>", Stream.of(ONE).collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-2>", Stream.of(ONE, TWO).collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-2-3>", Stream.of(LONGS).collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-null-3>", Stream.of(ONE, null, THREE).collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-2>", Stream.of(ATOMIC_LONGS).collect(DELIMITER_PREFIX_SUFFIX));
        assertEquals("<1-2>", Stream.of(FIXTURES).collect(DELIMITER_PREFIX_SUFFIX));
    }

    @Test
    void testStreamCollectorWithDelimiterPrefixSuffixMapper_Strings() {
        assertEquals("<>", Stream.of().collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1>", Stream.of("1").collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-2>", Stream.of("1", "2").collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-2-3>", Stream.of(STRINGS).collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-null-3>", Stream.of("1", null, "3").collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-NUL-3>", Stream.of("1", null, "3").collect(DELIMITER_PREFIX_SUFFIX_NULL_MAPPER));
    }

    @Test
    void testStreamCollectorWithDelimiterPrefixSuffixMapper_NonStrings() {
        assertEquals("<>", Stream.of().collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1>", Stream.of(ONE).collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-2>", Stream.of(ONE, TWO).collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-2-3>", Stream.of(LONGS).collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-null-3>", Stream.of(ONE, null, THREE).collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-NUL-3>", Stream.of(ONE, null, THREE).collect(DELIMITER_PREFIX_SUFFIX_NULL_MAPPER));
        assertEquals("<1-2>", Stream.of(ATOMIC_LONGS).collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
        assertEquals("<1-2>", Stream.of(FIXTURES).collect(DELIMITER_PREFIX_SUFFIX_MAPPER));
    }
}