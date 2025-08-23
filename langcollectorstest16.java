package org.apache.commons.lang3.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class LangCollectorsTestTest16 {

    private static final Long _1L = Long.valueOf(1);

    private static final Long _2L = Long.valueOf(2);

    private static final Long _3L = Long.valueOf(3);

    private static final Function<Object, String> TO_STRING = Objects::toString;

    private static final Collector<Object, ?, String> JOINING_0 = LangCollectors.joining();

    private static final Collector<Object, ?, String> JOINING_1 = LangCollectors.joining("-");

    private static final Collector<Object, ?, String> JOINING_3 = LangCollectors.joining("-", "<", ">");

    private static final Collector<Object, ?, String> JOINING_4 = LangCollectors.joining("-", "<", ">", TO_STRING);

    private static final Collector<Object, ?, String> JOINING_4_NUL = LangCollectors.joining("-", "<", ">", o -> Objects.toString(o, "NUL"));

    private String join0(final Object... objects) {
        return LangCollectors.collect(JOINING_0, objects);
    }

    private String join1(final Object... objects) {
        return LangCollectors.collect(JOINING_1, objects);
    }

    private String join3(final Object... objects) {
        return LangCollectors.collect(JOINING_3, objects);
    }

    private String join4(final Object... objects) {
        return LangCollectors.collect(JOINING_4, objects);
    }

    private String join4NullToString(final Object... objects) {
        return LangCollectors.collect(JOINING_4_NUL, objects);
    }

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

    @Test
    void testJoiningStrings3Args() {
        assertEquals("<>", Stream.of().collect(JOINING_3));
        assertEquals("<1>", Stream.of("1").collect(JOINING_3));
        assertEquals("<1-2>", Stream.of("1", "2").collect(JOINING_3));
        assertEquals("<1-2-3>", Stream.of("1", "2", "3").collect(JOINING_3));
        assertEquals("<1-null-3>", Stream.of("1", null, "3").collect(JOINING_3));
    }
}
