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

public class SequencesComparatorTestTest3 {

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
    void testMinimal() {
        final String[] shadokAlph = { "GA", "BU", "ZO", "MEU" };
        final List<String> sentenceBefore = new ArrayList<>();
        final List<String> sentenceAfter = new ArrayList<>();
        sentenceBefore.add(shadokAlph[0]);
        sentenceBefore.add(shadokAlph[2]);
        sentenceBefore.add(shadokAlph[3]);
        sentenceBefore.add(shadokAlph[1]);
        sentenceBefore.add(shadokAlph[0]);
        sentenceBefore.add(shadokAlph[0]);
        sentenceBefore.add(shadokAlph[2]);
        sentenceBefore.add(shadokAlph[1]);
        sentenceBefore.add(shadokAlph[3]);
        sentenceBefore.add(shadokAlph[0]);
        sentenceBefore.add(shadokAlph[2]);
        sentenceBefore.add(shadokAlph[1]);
        sentenceBefore.add(shadokAlph[3]);
        sentenceBefore.add(shadokAlph[2]);
        sentenceBefore.add(shadokAlph[2]);
        sentenceBefore.add(shadokAlph[0]);
        sentenceBefore.add(shadokAlph[1]);
        sentenceBefore.add(shadokAlph[3]);
        sentenceBefore.add(shadokAlph[0]);
        sentenceBefore.add(shadokAlph[3]);
        final Random random = new Random(4564634237452342L);
        for (int nbCom = 0; nbCom <= 40; nbCom += 5) {
            sentenceAfter.clear();
            sentenceAfter.addAll(sentenceBefore);
            for (int i = 0; i < nbCom; i++) {
                if (random.nextInt(2) == 0) {
                    sentenceAfter.add(random.nextInt(sentenceAfter.size() + 1), shadokAlph[random.nextInt(4)]);
                } else {
                    sentenceAfter.remove(random.nextInt(sentenceAfter.size()));
                }
            }
            final SequencesComparator<String> comparator = new SequencesComparator<>(sentenceBefore, sentenceAfter);
            assertTrue(comparator.getScript().getModifications() <= nbCom);
        }
    }
}
