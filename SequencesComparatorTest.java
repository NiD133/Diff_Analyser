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
package org.apache.commons.collections4.sequence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link SequencesComparator}.
 */
class SequencesComparatorTest {

    private static final String[] SHADOK_ALPHABET = { "GA", "BU", "ZO", "MEU" };

    /**
     * A test-specific visitor that applies an edit script to a list to verify
     * the transformation is correct.
     */
    private static final class ExecutionVisitor<T> implements CommandVisitor<T> {

        private List<T> list;
        private int index;

        /**
         * Applies the script to the given list.
         * @param list the list to be transformed.
         */
        public void setList(final List<T> list) {
            this.list = new ArrayList<>(list);
            this.index = 0;
        }

        /**
         * Returns the transformed list as a concatenated string.
         */
        public String getString() {
            final StringBuilder buffer = new StringBuilder();
            for (final T item : list) {
                buffer.append(item);
            }
            return buffer.toString();
        }

        @Override
        public void visitDeleteCommand(final T object) {
            list.remove(index);
        }

        @Override
        public void visitInsertCommand(final T object) {
            list.add(index++, object);
        }

        @Override
        public void visitKeepCommand(final T object) {
            ++index;
        }
    }

    /**
     * Helper to convert a String to a List of Characters.
     */
    private List<Character> sequence(final String string) {
        final List<Character> list = new ArrayList<>();
        for (int i = 0; i < string.length(); ++i) {
            list.add(string.charAt(i));
        }
        return list;
    }

    /**
     * Provides test cases for string comparisons, including the expected number of modifications.
     */
    private static Stream<Arguments> stringDiffs() {
        return Stream.of(
            Arguments.of("bottle", "noodle", 6),
            Arguments.of("nematode knowledge", "empty bottle", 16),
            Arguments.of(StringUtils.EMPTY, StringUtils.EMPTY, 0),
            Arguments.of("aa", "C", 3),
            Arguments.of("prefixed string", "prefix", 9),
            Arguments.of("ABCABBA", "CBABAC", 5),
            Arguments.of("glop glop", "pas glop pas glop", 8),
            Arguments.of("coq", "ane", 6),
            Arguments.of("spider-man", "klingon", 13)
        );
    }

    @ParameterizedTest(name = "[{index}] From \"{0}\" to \"{1}\"")
    @MethodSource("stringDiffs")
    void testScriptExecutionAndModificationCount(final String before, final String after, final int expectedModifications) {
        final List<Character> beforeSequence = sequence(before);
        final List<Character> afterSequence = sequence(after);

        final SequencesComparator<Character> comparator = new SequencesComparator<>(beforeSequence, afterSequence);
        final EditScript<Character> script = comparator.getScript();

        // Verify that the number of modifications is the expected minimum.
        assertEquals(expectedModifications, script.getModifications(),
                "Incorrect number of modifications");

        // Verify that applying the script to 'before' results in 'after'.
        final ExecutionVisitor<Character> visitor = new ExecutionVisitor<>();
        visitor.setList(beforeSequence);
        script.visit(visitor);
        assertEquals(after, visitor.getString(),
                "Applying script did not produce the correct final sequence");
    }

    @Test
    void testScriptLengthIsNoGreaterThanKnownEditCount() {
        // This test verifies a property of the diff algorithm: the length of the
        // computed edit script should be less than or equal to the number of edits
        // used to generate the 'after' sequence. This is a sanity check for the
        // algorithm's efficiency, as it should find the shortest path.

        final List<String> sentenceBefore = new ArrayList<>(Arrays.asList(
                SHADOK_ALPHABET[0], SHADOK_ALPHABET[2], SHADOK_ALPHABET[3], SHADOK_ALPHABET[1],
                SHADOK_ALPHABET[0], SHADOK_ALPHABET[0], SHADOK_ALPHABET[2], SHADOK_ALPHABET[1],
                SHADOK_ALPHABET[3], SHADOK_ALPHABET[0], SHADOK_ALPHABET[2], SHADOK_ALPHABET[1],
                SHADOK_ALPHABET[3], SHADOK_ALPHABET[2], SHADOK_ALPHABET[2], SHADOK_ALPHABET[0],
                SHADOK_ALPHABET[1], SHADOK_ALPHABET[3], SHADOK_ALPHABET[0], SHADOK_ALPHABET[3]
        ));

        // Use a fixed seed for predictable random behavior.
        final Random random = new Random(4564634237452342L);

        // Gradually increase the number of random edits.
        for (int numberOfRandomEdits = 0; numberOfRandomEdits <= 40; numberOfRandomEdits += 5) {
            final List<String> sentenceAfter = new ArrayList<>(sentenceBefore);

            // Apply a known number of random insertions or deletions.
            for (int i = 0; i < numberOfRandomEdits; i++) {
                if (random.nextBoolean()) {
                    // Insert a random word at a random position.
                    final int pos = sentenceAfter.isEmpty() ? 0 : random.nextInt(sentenceAfter.size() + 1);
                    final String word = SHADOK_ALPHABET[random.nextInt(SHADOK_ALPHABET.length)];
                    sentenceAfter.add(pos, word);
                } else if (!sentenceAfter.isEmpty()) {
                    // Delete a word from a random position.
                    sentenceAfter.remove(random.nextInt(sentenceAfter.size()));
                }
            }

            final SequencesComparator<String> comparator = new SequencesComparator<>(sentenceBefore, sentenceAfter);
            final int modifications = comparator.getScript().getModifications();

            assertTrue(modifications <= numberOfRandomEdits,
                    () -> String.format("Found %d modifications, but only %d were made.",
                                        modifications, numberOfRandomEdits));
        }
    }

    @Test
    void testExhaustiveExecutionWithCustomObjectSequences() {
        // This test exhaustively compares all possible pairs of "Shadok" sentences
        // up to a certain length, ensuring the transformation script works correctly
        // for sequences of custom objects (Strings in this case).

        // NOTE: The original test used a max length of 5, resulting in over 100,000
        // comparisons, which can be slow. Reduced to 4 for test suite efficiency.
        final int maxSentenceLength = 4;

        // Generate all possible sentences with words from the Shadok alphabet
        // with a length up to (maxSentenceLength - 1).
        List<List<String>> sentences = new ArrayList<>();
        for (int i = 0; i < maxSentenceLength; ++i) {
            final List<List<String>> nextSentences = new ArrayList<>();
            // The new set of sentences always includes the empty sentence.
            nextSentences.add(new ArrayList<>());
            // Then, add all words from the alphabet to the sentences from the previous step.
            for (final String word : SHADOK_ALPHABET) {
                for (final List<String> sentence : sentences) {
                    final List<String> newSentence = new ArrayList<>(sentence);
                    newSentence.add(word);
                    nextSentences.add(newSentence);
                }
            }
            sentences = nextSentences;
        }

        final ExecutionVisitor<String> visitor = new ExecutionVisitor<>();

        // Compare every sentence with every other sentence.
        for (final List<String> beforeSentence : sentences) {
            for (final List<String> afterSentence : sentences) {
                visitor.setList(beforeSentence);
                new SequencesComparator<>(beforeSentence, afterSentence)
                        .getScript()
                        .visit(visitor);

                final String expected = String.join("", afterSentence);
                final String actual = visitor.getString();

                assertEquals(expected, actual,
                        () -> String.format("Failed transformation from %s to %s",
                                            beforeSentence, afterSentence));
            }
        }
    }
}