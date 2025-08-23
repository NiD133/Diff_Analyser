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

class SequencesComparatorTest {

    private static final class ExecutionVisitor<T> implements CommandVisitor<T> {

        private List<T> elements;
        private int currentIndex;

        public String getConcatenatedString() {
            final StringBuilder buffer = new StringBuilder();
            for (final T element : elements) {
                buffer.append(element);
            }
            return buffer.toString();
        }

        public void setElements(final List<T> elements) {
            this.elements = new ArrayList<>(elements);
            this.currentIndex = 0;
        }

        @Override
        public void visitDeleteCommand(final T object) {
            elements.remove(currentIndex);
        }

        @Override
        public void visitInsertCommand(final T object) {
            elements.add(currentIndex++, object);
        }

        @Override
        public void visitKeepCommand(final T object) {
            ++currentIndex;
        }
    }

    private List<String> initialStrings;
    private List<String> targetStrings;
    private int[] expectedModificationCounts;

    @BeforeEach
    public void setUp() {
        initialStrings = Arrays.asList(
            "bottle",
            "nematode knowledge",
            StringUtils.EMPTY,
            "aa",
            "prefixed string",
            "ABCABBA",
            "glop glop",
            "coq",
            "spider-man"
        );

        targetStrings = Arrays.asList(
            "noodle",
            "empty bottle",
            StringUtils.EMPTY,
            "C",
            "prefix",
            "CBABAC",
            "pas glop pas glop",
            "ane",
            "klingon"
        );

        expectedModificationCounts = new int[] {
            6,
            16,
            0,
            3,
            9,
            5,
            8,
            6,
            13
        };
    }

    @AfterEach
    public void tearDown() {
        initialStrings = null;
        targetStrings = null;
        expectedModificationCounts = null;
    }

    private List<Character> convertStringToCharacterList(final String string) {
        final List<Character> characterList = new ArrayList<>();
        for (int i = 0; i < string.length(); ++i) {
            characterList.add(Character.valueOf(string.charAt(i)));
        }
        return characterList;
    }

    @Test
    void testTransformationExecution() {
        final ExecutionVisitor<Character> visitor = new ExecutionVisitor<>();
        for (int i = 0; i < initialStrings.size(); ++i) {
            visitor.setElements(convertStringToCharacterList(initialStrings.get(i)));
            new SequencesComparator<>(
                convertStringToCharacterList(initialStrings.get(i)),
                convertStringToCharacterList(targetStrings.get(i))
            ).getScript().visit(visitor);
            assertEquals(targetStrings.get(i), visitor.getConcatenatedString());
        }
    }

    @Test
    void testModificationCount() {
        for (int i = 0; i < initialStrings.size(); ++i) {
            final SequencesComparator<Character> comparator = new SequencesComparator<>(
                convertStringToCharacterList(initialStrings.get(i)),
                convertStringToCharacterList(targetStrings.get(i))
            );
            assertEquals(expectedModificationCounts[i], comparator.getScript().getModifications());
        }
    }

    @Test
    void testMinimalModifications() {
        final String[] shadokAlphabet = {"GA", "BU", "ZO", "MEU"};
        final List<String> sentenceBefore = new ArrayList<>(Arrays.asList(
            "GA", "ZO", "MEU", "BU", "GA", "GA", "ZO", "BU", "MEU", "GA", "ZO", "BU", "MEU", "ZO", "ZO", "GA", "BU", "MEU", "GA", "MEU"
        ));

        final Random random = new Random(4564634237452342L);

        for (int maxModifications = 0; maxModifications <= 40; maxModifications += 5) {
            final List<String> sentenceAfter = new ArrayList<>(sentenceBefore);
            for (int i = 0; i < maxModifications; i++) {
                if (random.nextInt(2) == 0) {
                    sentenceAfter.add(random.nextInt(sentenceAfter.size() + 1), shadokAlphabet[random.nextInt(4)]);
                } else {
                    sentenceAfter.remove(random.nextInt(sentenceAfter.size()));
                }
            }

            final SequencesComparator<String> comparator = new SequencesComparator<>(sentenceBefore, sentenceAfter);
            assertTrue(comparator.getScript().getModifications() <= maxModifications);
        }
    }

    @Test
    void testShadokSentences() {
        final int maxLength = 5;
        final String[] shadokAlphabet = {"GA", "BU", "ZO", "MEU"};
        List<List<String>> shadokSentences = new ArrayList<>();
        for (int length = 0; length < maxLength; ++length) {
            final List<List<String>> newSentences = new ArrayList<>();
            newSentences.add(new ArrayList<>());
            for (final String element : shadokAlphabet) {
                for (final List<String> sentence : shadokSentences) {
                    final List<String> newSentence = new ArrayList<>(sentence);
                    newSentence.add(element);
                    newSentences.add(newSentence);
                }
            }
            shadokSentences = newSentences;
        }

        final ExecutionVisitor<String> visitor = new ExecutionVisitor<>();

        for (final List<String> sentence1 : shadokSentences) {
            for (final List<String> sentence2 : shadokSentences) {
                visitor.setElements(sentence1);
                new SequencesComparator<>(sentence1, sentence2).getScript().visit(visitor);

                final StringBuilder concatenatedSentence2 = new StringBuilder();
                for (final String s : sentence2) {
                    concatenatedSentence2.append(s);
                }
                assertEquals(concatenatedSentence2.toString(), visitor.getConcatenatedString());
            }
        }
    }
}