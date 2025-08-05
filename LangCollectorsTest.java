package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LangCollectors}.
 */
class LangCollectorsTest {

    private static final class Fixture {
        int value;

        private Fixture(final int value) {
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

    private static final Collector<Object, ?, String> JOINING_NO_DELIMITER = LangCollectors.joining();
    private static final Collector<Object, ?, String> JOINING_WITH_HYPHEN = LangCollectors.joining("-");
    private static final Collector<Object, ?, String> JOINING_WITH_HYPHEN_AND_BRACKETS = LangCollectors.joining("-", "<", ">");
    private static final Collector<Object, ?, String> JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING = LangCollectors.joining("-", "<", ">", TO_STRING);
    private static final Collector<Object, ?, String> JOINING_WITH_HYPHEN_BRACKETS_AND_NULL_TO_STRING = LangCollectors.joining("-", "<", ">", o -> Objects.toString(o, "NUL"));

    private String collectWithNoDelimiter(final Object... objects) {
        return LangCollectors.collect(JOINING_NO_DELIMITER, objects);
    }

    private String collectWithHyphen(final Object... objects) {
        return LangCollectors.collect(JOINING_WITH_HYPHEN, objects);
    }

    private String collectWithHyphenAndBrackets(final Object... objects) {
        return LangCollectors.collect(JOINING_WITH_HYPHEN_AND_BRACKETS, objects);
    }

    private String collectWithHyphenBracketsAndToString(final Object... objects) {
        return LangCollectors.collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING, objects);
    }

    private String collectWithHyphenBracketsAndNullToString(final Object... objects) {
        return LangCollectors.collect(JOINING_WITH_HYPHEN_BRACKETS_AND_NULL_TO_STRING, objects);
    }

    @Test
    void testCollectStringsWithHyphen() {
        assertEquals("", collectWithHyphen());
        assertEquals("1", collectWithHyphen("1"));
        assertEquals("1-2", collectWithHyphen("1", "2"));
        assertEquals("1-2-3", collectWithHyphen("1", "2", "3"));
        assertEquals("1-null-3", collectWithHyphen("1", null, "3"));
    }

    @Test
    void testCollectNonStringsNoDelimiter() {
        assertEquals("", collectWithNoDelimiter());
        assertEquals("1", collectWithNoDelimiter(ONE));
        assertEquals("12", collectWithNoDelimiter(ONE, TWO));
        assertEquals("123", collectWithNoDelimiter(ONE, TWO, THREE));
        assertEquals("1null3", collectWithNoDelimiter(ONE, null, THREE));
        assertEquals("12", collectWithNoDelimiter(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("12", collectWithNoDelimiter(new Fixture(1), new Fixture(2)));
    }

    @Test
    void testCollectNonStringsWithHyphen() {
        assertEquals("", collectWithHyphen());
        assertEquals("1", collectWithHyphen(ONE));
        assertEquals("1-2", collectWithHyphen(ONE, TWO));
        assertEquals("1-2-3", collectWithHyphen(ONE, TWO, THREE));
        assertEquals("1-null-3", collectWithHyphen(ONE, null, THREE));
        assertEquals("1-2", collectWithHyphen(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("1-2", collectWithHyphen(new Fixture(1), new Fixture(2)));
    }

    @Test
    void testCollectNonStringsWithHyphenAndBrackets() {
        assertEquals("<>", collectWithHyphenAndBrackets());
        assertEquals("<1>", collectWithHyphenAndBrackets(ONE));
        assertEquals("<1-2>", collectWithHyphenAndBrackets(ONE, TWO));
        assertEquals("<1-2-3>", collectWithHyphenAndBrackets(ONE, TWO, THREE));
        assertEquals("<1-null-3>", collectWithHyphenAndBrackets(ONE, null, THREE));
        assertEquals("<1-2>", collectWithHyphenAndBrackets(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("<1-2>", collectWithHyphenAndBrackets(new Fixture(1), new Fixture(2)));
    }

    @Test
    void testCollectNonStringsWithHyphenBracketsAndToString() {
        assertEquals("<>", collectWithHyphenBracketsAndToString());
        assertEquals("<1>", collectWithHyphenBracketsAndToString(ONE));
        assertEquals("<1-2>", collectWithHyphenBracketsAndToString(ONE, TWO));
        assertEquals("<1-2-3>", collectWithHyphenBracketsAndToString(ONE, TWO, THREE));
        assertEquals("<1-null-3>", collectWithHyphenBracketsAndToString(ONE, null, THREE));
        assertEquals("<1-NUL-3>", collectWithHyphenBracketsAndNullToString(ONE, null, THREE));
        assertEquals("<1-2>", collectWithHyphenBracketsAndToString(new AtomicLong(1), new AtomicLong(2)));
        assertEquals("<1-2>", collectWithHyphenBracketsAndToString(new Fixture(1), new Fixture(2)));
    }

    @Test
    void testCollectNullArgs() {
        assertEquals("", collectWithNoDelimiter((Object[]) null));
        assertEquals("", collectWithHyphen((Object[]) null));
        assertEquals("<>", collectWithHyphenAndBrackets((Object[]) null));
        assertEquals("<>", collectWithHyphenBracketsAndNullToString((Object[]) null));
    }

    @Test
    void testCollectStringsNoDelimiter() {
        assertEquals("", collectWithNoDelimiter());
        assertEquals("1", collectWithNoDelimiter("1"));
        assertEquals("12", collectWithNoDelimiter("1", "2"));
        assertEquals("123", collectWithNoDelimiter("1", "2", "3"));
        assertEquals("1null3", collectWithNoDelimiter("1", null, "3"));
    }

    @Test
    void testCollectStringsWithHyphenAndBrackets() {
        assertEquals("<>", collectWithHyphenAndBrackets());
        assertEquals("<1>", collectWithHyphenAndBrackets("1"));
        assertEquals("<1-2>", collectWithHyphenAndBrackets("1", "2"));
        assertEquals("<1-2-3>", collectWithHyphenAndBrackets("1", "2", "3"));
        assertEquals("<1-null-3>", collectWithHyphenAndBrackets("1", null, "3"));
    }

    @Test
    void testCollectStringsWithHyphenBracketsAndToString() {
        assertEquals("<>", collectWithHyphenBracketsAndToString());
        assertEquals("<1>", collectWithHyphenBracketsAndToString("1"));
        assertEquals("<1-2>", collectWithHyphenBracketsAndToString("1", "2"));
        assertEquals("<1-2-3>", collectWithHyphenBracketsAndToString("1", "2", "3"));
        assertEquals("<1-null-3>", collectWithHyphenBracketsAndToString("1", null, "3"));
        assertEquals("<1-NUL-3>", collectWithHyphenBracketsAndNullToString("1", null, "3"));
    }

    @Test
    void testStreamCollectNonStringsNoDelimiter() {
        assertEquals("", Stream.of().collect(JOINING_NO_DELIMITER));
        assertEquals("1", Stream.of(ONE).collect(JOINING_NO_DELIMITER));
        assertEquals("12", Stream.of(ONE, TWO).collect(JOINING_NO_DELIMITER));
        assertEquals("123", Stream.of(ONE, TWO, THREE).collect(JOINING_NO_DELIMITER));
        assertEquals("1null3", Stream.of(ONE, null, THREE).collect(JOINING_NO_DELIMITER));
        assertEquals("12", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(JOINING_NO_DELIMITER));
        assertEquals("12", Stream.of(new Fixture(1), new Fixture(2)).collect(JOINING_NO_DELIMITER));
    }

    @Test
    void testStreamCollectNonStringsWithHyphen() {
        assertEquals("", Stream.of().collect(JOINING_WITH_HYPHEN));
        assertEquals("1", Stream.of(ONE).collect(JOINING_WITH_HYPHEN));
        assertEquals("1-2", Stream.of(ONE, TWO).collect(JOINING_WITH_HYPHEN));
        assertEquals("1-2-3", Stream.of(ONE, TWO, THREE).collect(JOINING_WITH_HYPHEN));
        assertEquals("1-null-3", Stream.of(ONE, null, THREE).collect(JOINING_WITH_HYPHEN));
        assertEquals("1-2", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(JOINING_WITH_HYPHEN));
        assertEquals("1-2", Stream.of(new Fixture(1), new Fixture(2)).collect(JOINING_WITH_HYPHEN));
    }

    @Test
    void testStreamCollectNonStringsWithHyphenAndBrackets() {
        assertEquals("<>", Stream.of().collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1>", Stream.of(ONE).collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-2>", Stream.of(ONE, TWO).collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-2-3>", Stream.of(ONE, TWO, THREE).collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-null-3>", Stream.of(ONE, null, THREE).collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-2>", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-2>", Stream.of(new Fixture(1), new Fixture(2)).collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
    }

    @Test
    void testStreamCollectNonStringsWithHyphenBracketsAndToString() {
        assertEquals("<>", Stream.of().collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1>", Stream.of(ONE).collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-2>", Stream.of(ONE, TWO).collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-2-3>", Stream.of(ONE, TWO, THREE).collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-null-3>", Stream.of(ONE, null, THREE).collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-NUL-3>", Stream.of(ONE, null, THREE).collect(JOINING_WITH_HYPHEN_BRACKETS_AND_NULL_TO_STRING));
        assertEquals("<1-2>", Stream.of(new AtomicLong(1), new AtomicLong(2)).collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-2>", Stream.of(new Fixture(1), new Fixture(2)).collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
    }

    @Test
    void testStreamCollectStringsNoDelimiter() {
        assertEquals("", Stream.of().collect(JOINING_NO_DELIMITER));
        assertEquals("1", Stream.of("1").collect(JOINING_NO_DELIMITER));
        assertEquals("12", Stream.of("1", "2").collect(JOINING_NO_DELIMITER));
        assertEquals("123", Stream.of("1", "2", "3").collect(JOINING_NO_DELIMITER));
        assertEquals("1null3", Stream.of("1", null, "3").collect(JOINING_NO_DELIMITER));
    }

    @Test
    void testStreamCollectStringsWithHyphen() {
        assertEquals("", Stream.of().collect(JOINING_WITH_HYPHEN));
        assertEquals("1", Stream.of("1").collect(JOINING_WITH_HYPHEN));
        assertEquals("1-2", Stream.of("1", "2").collect(JOINING_WITH_HYPHEN));
        assertEquals("1-2-3", Stream.of("1", "2", "3").collect(JOINING_WITH_HYPHEN));
        assertEquals("1-null-3", Stream.of("1", null, "3").collect(JOINING_WITH_HYPHEN));
    }

    @Test
    void testStreamCollectStringsWithHyphenAndBrackets() {
        assertEquals("<>", Stream.of().collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1>", Stream.of("1").collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-2>", Stream.of("1", "2").collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-2-3>", Stream.of("1", "2", "3").collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
        assertEquals("<1-null-3>", Stream.of("1", null, "3").collect(JOINING_WITH_HYPHEN_AND_BRACKETS));
    }

    @Test
    void testStreamCollectStringsWithHyphenBracketsAndToString() {
        assertEquals("<>", Stream.of().collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1>", Stream.of("1").collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-2>", Stream.of("1", "2").collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-2-3>", Stream.of("1", "2", "3").collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-null-3>", Stream.of("1", null, "3").collect(JOINING_WITH_HYPHEN_BRACKETS_AND_TO_STRING));
        assertEquals("<1-NUL-3>", Stream.of("1", null, "3").collect(JOINING_WITH_HYPHEN_BRACKETS_AND_NULL_TO_STRING));
    }
}