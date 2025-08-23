package org.apache.commons.collections4.sequence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SequencesComparatorTestTest2 {

    private List<String> before;

    private List<String> after;

    private int[] length;

    private List<Character> sequence(final String string) {
        final List<Character> list = new ArrayList<>();
        for (int i = 0; i < string.length(); ++i) {
            list.add(Character.valueOf(string.charAt(i)));
        }
        return list;
    }

    @BeforeEach
    public void setUp() {
        before = Arrays.asList("bottle", "nematode knowledge", StringUtils.EMPTY, "aa", "prefixed string", "ABCABBA", "glop glop", "coq", "spider-man");
        after = Arrays.asList("noodle", "empty bottle", StringUtils.EMPTY, "C", "prefix", "CBABAC", "pas glop pas glop", "ane", "klingon");
        length = new int[] { 6, 16, 0, 3, 9, 5, 8, 6, 13 };
    }

    @AfterEach
    public void tearDown() {
        before = null;
        after = null;
        length = null;
    }

    private static final class ExecutionVisitor<T> implements CommandVisitor<T> {

        private List<T> v;

        private int index;

        public String getString() {
            final StringBuilder buffer = new StringBuilder();
            for (final T c : v) {
                buffer.append(c);
            }
            return buffer.toString();
        }

        public void setList(final List<T> array) {
            v = new ArrayList<>(array);
            index = 0;
        }

        @Override
        public void visitDeleteCommand(final T object) {
            v.remove(index);
        }

        @Override
        public void visitInsertCommand(final T object) {
            v.add(index++, object);
        }

        @Override
        public void visitKeepCommand(final T object) {
            ++index;
        }
    }

    @Test
    void testLength() {
        for (int i = 0; i < before.size(); ++i) {
            final SequencesComparator<Character> comparator = new SequencesComparator<>(sequence(before.get(i)), sequence(after.get(i)));
            assertEquals(length[i], comparator.getScript().getModifications());
        }
    }
}
