package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link LangCollectors#joining()}.
 *
 * This test class focuses on the behavior of the no-argument joining collector
 * with streams of various non-String objects.
 */
@DisplayName("LangCollectors.joining()")
class LangCollectorsJoiningTest {

    private static final Long ONE = 1L;
    private static final Long TWO = 2L;
    private static final Long THREE = 3L;

    /** The collector instance under test. */
    private static final Collector<Object, ?, String> JOINING_COLLECTOR = LangCollectors.joining();

    /**
     * A simple fixture class for testing with custom objects.
     */
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

    /**
     * Provides test cases for the joining collector. Each case includes a descriptive name,
     * the expected string result, and the input elements for the stream.
     *
     * @return A stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> provideJoiningTestCases() {
        final Fixture fixture1 = new Fixture(1);
        final Fixture fixture2 = new Fixture(2);
        final AtomicLong atomic1 = new AtomicLong(1);
        final AtomicLong atomic2 = new AtomicLong(2);

        return Stream.of(
            Arguments.of("An empty stream", "", new Object[]{}),
            Arguments.of("A single Long element", ONE.toString(), new Object[]{ONE}),
            Arguments.of("Multiple Long elements", "123", new Object[]{ONE, TWO, THREE}),
            Arguments.of("Elements including a null", "1null3", new Object[]{ONE, null, THREE}),
            Arguments.of("Multiple AtomicLong elements", "12", new Object[]{atomic1, atomic2}),
            Arguments.of("Multiple custom Fixture objects", "12", new Object[]{fixture1, fixture2})
        );
    }

    @ParameterizedTest(name = "[{index}] should correctly join: {0}")
    @MethodSource("provideJoiningTestCases")
    void shouldConcatenateObjectStreamToString(final String caseName, final String expected, final Object[] elements) {
        // The original test duplicated logic for Stream.of() and Arrays.stream().
        // This is redundant as they produce equivalent streams. The logic has been
        // consolidated into this single parameterized test for clarity and maintainability.
        final String actual = Stream.of(elements).collect(JOINING_COLLECTOR);
        assertEquals(expected, actual);
    }
}