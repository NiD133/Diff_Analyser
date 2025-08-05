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

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Unit tests for {@link LangCollectors}.
 */
public class LangCollectorsTest {

    @Test
    public void testJoiningWithNoArguments() {
        // Arrange
        final Collector<Object, ?, String> collector = LangCollectors.joining();
        final Stream<Object> stream = Stream.of("Apache", "Commons", 3);

        // Act
        final String result = stream.collect(collector);

        // Assert
        assertEquals("ApacheCommons3", result);
    }

    @Test
    public void testJoiningWithDelimiter() {
        // Arrange
        final Collector<Object, ?, String> collector = LangCollectors.joining("-");
        final Stream<Object> stream = Stream.of("Apache", "Commons", 3);

        // Act
        final String result = stream.collect(collector);

        // Assert
        assertEquals("Apache-Commons-3", result);
    }
    
    @Test
    public void testJoiningWithNullDelimiter() {
        // Arrange
        // A null delimiter should be treated as an empty string.
        final Collector<Object, ?, String> collector = LangCollectors.joining(null);
        final Stream<Object> stream = Stream.of("Apache", "Commons", 3);

        // Act
        final String result = stream.collect(collector);

        // Assert
        assertEquals("ApacheCommons3", result);
    }

    @Test
    public void testJoiningWithDelimiterPrefixAndSuffix() {
        // Arrange
        final Collector<Object, ?, String> collector = LangCollectors.joining("-", "[", "]");
        final Stream<Object> stream = Stream.of("Apache", "Commons", 3);

        // Act
        final String result = stream.collect(collector);

        // Assert
        assertEquals("[Apache-Commons-3]", result);
    }
    
    @Test
    public void testJoiningWithNullPrefixAndSuffix() {
        // Arrange
        // Null prefix and suffix should be treated as empty strings.
        final Collector<Object, ?, String> collector = LangCollectors.joining("-", null, null);
        final Stream<Object> stream = Stream.of("Apache", "Commons", 3);

        // Act
        final String result = stream.collect(collector);

        // Assert
        assertEquals("Apache-Commons-3", result);
    }

    @Test
    public void testJoiningWithCustomToStringFunction() {
        // Arrange
        final Function<Object, String> toString = obj -> obj == null ? "null" : obj.toString().toUpperCase();
        final Collector<Object, ?, String> collector = LangCollectors.joining(",", "{", "}", toString);
        final Stream<Object> stream = Stream.of("One", 2, null);

        // Act
        final String result = stream.collect(collector);

        // Assert
        assertEquals("{ONE,2,null}", result);
    }

    @Test
    public void testCollectAppliesCollectorToArray() {
        // Arrange
        final Collector<Object, ?, String> collector = LangCollectors.joining(", ");
        final String[] array = {"One", "Two", "Three"};

        // Act
        final String result = LangCollectors.collect(collector, array);

        // Assert
        assertEquals("One, Two, Three", result);
    }

    @Test(expected = NullPointerException.class)
    public void testCollectWithNullCollectorThrowsException() {
        // Act
        // Calling collect with a null collector should throw a NullPointerException.
        LangCollectors.collect(null, "a", "b", "c");
    }
}