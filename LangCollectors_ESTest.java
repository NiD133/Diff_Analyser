package org.apache.commons.lang3.stream;

import static org.junit.Assert.assertEquals;

import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * Readable tests for LangCollectors that focus on documented behavior.
 * These tests avoid EvoSuite scaffolding and use descriptive names and assertions.
 */
public class LangCollectorsTest {

    @Test
    public void joining_noArgs_concatenatesObjectsUsingToString() {
        // From Javadoc: Stream.of(1, 2, 3) -> "123"
        String result = Stream.of(1L, 2L, 3L).collect(LangCollectors.joining());
        assertEquals("123", result);
    }

    @Test
    public void joining_withDelimiter_insertsDelimiterBetweenElements() {
        // From Javadoc: Stream.of(1, 2, 3) with "-" -> "1-2-3"
        String result = Stream.of(1L, 2L, 3L).collect(LangCollectors.joining("-"));
        assertEquals("1-2-3", result);
    }

    @Test
    public void joining_withDelimiterPrefixSuffix_wrapsAndSeparates() {
        // From Javadoc: Stream.of(1, 2, 3) with "-", "[", "]" -> "[1-2-3]"
        String result = Stream.of(1L, 2L, 3L).collect(LangCollectors.joining("-", "[", "]"));
        assertEquals("[1-2-3]", result);
    }

    @Test
    public void joining_withCustomToString_allowsNullHandling() {
        // From Javadoc: custom toString handles nulls
        Collector<Object, ?, String> collector =
            LangCollectors.joining("-", "[", "]", o -> Objects.toString(o, "NUL"));

        String result = Stream.of(1L, null, 3L).collect(collector);
        assertEquals("[1-NUL-3]", result);
    }

    @Test(expected = NullPointerException.class)
    public void collect_withNullCollector_throwsNullPointerException() {
        // Passing a null Collector must raise NPE
        LangCollectors.collect(null, "a", "b");
    }

    @Test
    public void collect_withArray_usesCollector() {
        // collect(T...) delegates to Stream.collect with the given Collector
        String result = LangCollectors.collect(LangCollectors.joining(","), 1, 2, 3);
        assertEquals("1,2,3", result);
    }

    @Test
    public void collect_withNullArray_isTreatedAsEmpty_forSimpleJoining() {
        // Javadoc: a null array is treated as empty; joining(delimiter) on empty yields ""
        String result = LangCollectors.collect(LangCollectors.joining(","), (Object[]) null);
        assertEquals("", result);
    }

    @Test
    public void collect_withNullArray_andPrefixSuffix_returnsOnlyPrefixAndSuffix() {
        // Behavior consistent with StringJoiner: empty input -> prefix + suffix
        String result = LangCollectors.collect(LangCollectors.joining("-", "[", "]"), (Object[]) null);
        assertEquals("[]", result);
    }
}