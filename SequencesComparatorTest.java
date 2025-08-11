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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;

class SequencesComparatorTest {

    /**
     * Applies edit commands to transform a sequence and track the result.
     */
    private static final class ScriptApplierVisitor<T> implements CommandVisitor<T> {

        private List<T> sequence;
        private int currentIndex;

        /**
         * @return Final transformed sequence as a concatenated string
         */
        public String getResultAsString() {
            final StringBuilder buffer = new StringBuilder();
            for (final T element : sequence) {
                buffer.append(element);
            }
            return buffer.toString();
        }

        /**
         * Initializes visitor with starting sequence.
         * @param sequence Initial sequence to transform
         */
        public void initialize(List<T> sequence) {
            this.sequence = new ArrayList<>(sequence);
            currentIndex = 0;
        }

        @Override
        public void visitDeleteCommand(final T object) {
            sequence.remove(currentIndex);
        }

        @Override
        public void visitInsertCommand(final T object) {
            sequence.add(currentIndex++, object);
        }

        @Override
        public void visitKeepCommand(final T object) {
            currentIndex++;
        }
    }

    /**
     * Converts a string to a list of characters.
     * @param string Input string
     * @return List of characters
     */
    private static List<Character> stringToCharacterList(final String string) {
        final List<Character> list = new ArrayList<>();
        for (final char c : string.toCharArray()) {
            list.add(c);
        }
        return list;
    }

    /**
     * Provides test cases for parameterized tests.
     * Each entry: [initial string, target string, expected edit count]
     */
    private static Stream<Arguments> provideStringPairsAndEditCount() {
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

    @ParameterizedTest
    @MethodSource("provideStringPairsAndEditCount")
    @DisplayName("Applies edit script and verifies final string matches target")
    void applyEditScript_TransformsInitialToTargetString(
            final String initial, final String target, final int expectedEditCount) {
        final ScriptApplierVisitor<Character> applier = new ScriptApplierVisitor<>();
        applier.initialize(stringToCharacterList(initial));
        new SequencesComparator<>(
            stringToCharacterList(initial),
            stringToCharacterList(target)
        ).getScript().visit(applier);

        assertEquals(target, applier.getResultAsString());
    }

    @ParameterizedTest
    @MethodSource("provideStringPairsAndEditCount")
    @DisplayName("Verifies correct number of edit operations")
    void getScriptModifications_ReturnsCorrectEditCount(
            final String initial, final String target, final int expectedEditCount) {
        final SequencesComparator<Character> comparator = new SequencesComparator<>(
            stringToCharacterList(initial),
            stringToCharacterList(target)
        );
        assertEquals(expectedEditCount, comparator.getScript().getModifications());
    }

    @Test
    @DisplayName("Verifies edit count doesn't exceed random modifications")
    void randomModifications_EditCountWithinBounds() {
        final String[] shadokAlphabet = {"GA", "BU", "ZO", "MEU"};
        final List<String> originalSentence = Arrays.asList(
            "GA", "ZO", "MEU", "BU", "GA", "GA", "ZO", "BU", "MEU", "GA",
            "ZO", "BU", "MEU", "ZO", "ZO", "GA", "BU", "MEU", "GA", "MEU"
        );

        final Random random = new Random(4564634237452342L);

        for (int numberOfModifications = 0; numberOfModifications <= 40; numberOfModifications += 5) {
            // Create a modified copy of the original sentence
            List<String> modifiedSentence = new ArrayList<>(originalSentence);
            
            // Apply random insertions/deletions
            for (int i = 0; i < numberOfModifications; i++) {
                if (random.nextBoolean()) {
                    // Insert random word at random position
                    modifiedSentence.add(
                        random.nextInt(modifiedSentence.size() + 1),
                        shadokAlphabet[random.nextInt(shadokAlphabet.length)]
                    );
                } else {
                    // Delete random word
                    modifiedSentence.remove(random.nextInt(modifiedSentence.size()));
                }
            }

            // Verify edit count doesn't exceed applied modifications
            final SequencesComparator<String> comparator = new SequencesComparator<>(
                originalSentence,
                modifiedSentence
            );
            assertTrue(comparator.getScript().getModifications() <= numberOfModifications,
                "Edit count should be <= " + numberOfModifications);
        }
    }

    @Test
    @DisplayName("Verifies all Shadok sentence transformations")
    void allShadokSentencePairs_TransformCorrectly() {
        final int maxLength = 5;
        final String[] shadokAlphabet = {"GA", "BU", "ZO", "MEU"};
        
        // Generate all possible Shadok sentences up to maxLength
        List<List<String>> shadokSentences = new ArrayList<>();
        shadokSentences.add(new ArrayList<>()); // Start with empty sentence
        
        for (int length = 1; length <= maxLength; length++) {
            final List<List<String>> nextLengthSentences = new ArrayList<>();
            for (final String word : shadokAlphabet) {
                for (final List<String> sentence : shadokSentences) {
                    final List<String> newSentence = new ArrayList<>(sentence);
                    newSentence.add(word);
                    nextLengthSentences.add(newSentence);
                }
            }
            shadokSentences.addAll(nextLengthSentences);
        }

        final ScriptApplierVisitor<String> applier = new ScriptApplierVisitor<>();

        // Verify every sentence can be transformed to every other sentence
        for (final List<String> sourceSentence : shadokSentences) {
            for (final List<String> targetSentence : shadokSentences) {
                applier.initialize(sourceSentence);
                new SequencesComparator<>(sourceSentence, targetSentence)
                    .getScript().visit(applier);

                // Build expected result by concatenating target words
                final StringBuilder expected = new StringBuilder();
                for (final String word : targetSentence) {
                    expected.append(word);
                }

                assertEquals(expected.toString(), applier.getResultAsString(),
                    "Transformation failed for source->target");
            }
        }
    }
}