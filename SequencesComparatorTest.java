package org.apache.commons.collections4.sequence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for SequencesComparator.
 * The tests focus on:
 * - Applying an edit script transforms the source into the expected target.
 * - Reported number of modifications matches expectations.
 * - The algorithm produces a script no longer than the number of random edits applied.
 * - Exhaustive checks on small “Shadok” sentences.
 */
class SequencesComparatorTest {

    /**
     * A visitor that applies an EditScript to an in-memory list, so we can verify
     * that applying the script to the source sequence yields the target sequence.
     */
    private static final class EditScriptApplyingVisitor<T> implements CommandVisitor<T> {

        private List<T> working;
        private int index;

        /**
         * Returns the current working list concatenated into a String using StringBuilder.append(T).
         * This works for Character (forming a word) and for String (forming a concatenation of tokens).
         */
        public String asString() {
            final StringBuilder buffer = new StringBuilder();
            for (final T c : working) {
                buffer.append(c);
            }
            return buffer.toString();
        }

        public void resetWith(final List<T> initial) {
            working = new ArrayList<>(initial);
            index = 0;
        }

        @Override
        public void visitDeleteCommand(final T object) {
            working.remove(index);
        }

        @Override
        public void visitInsertCommand(final T object) {
            working.add(index++, object);
        }

        @Override
        public void visitKeepCommand(final T object) {
            ++index;
        }
    }

    /**
     * Simple container for a string comparison fixture.
     */
    private static final class ComparisonFixture {
        final String source;
        final String target;
        final int expectedModifications;

        ComparisonFixture(final String source, final String target, final int expectedModifications) {
            this.source = source;
            this.target = target;
            this.expectedModifications = expectedModifications;
        }
    }

    // Test data
    private List<ComparisonFixture> fixtures;

    // Deterministic seed for the random-edit test
    private static final long RANDOM_SEED = 4564634237452342L;

    private static List<Character> toCharList(final String s) {
        final List<Character> list = new ArrayList<>(s.length());
        for (int i = 0; i < s.length(); ++i) {
            list.add(s.charAt(i));
        }
        return list;
    }

    @BeforeEach
    void setUp() {
        fixtures = Arrays.asList(
            new ComparisonFixture("bottle", "noodle", 6),
            new ComparisonFixture("nematode knowledge", "empty bottle", 16),
            new ComparisonFixture("", "", 0),
            new ComparisonFixture("aa", "C", 3),
            new ComparisonFixture("prefixed string", "prefix", 9),
            new ComparisonFixture("ABCABBA", "CBABAC", 5),
            new ComparisonFixture("glop glop", "pas glop pas glop", 8),
            new ComparisonFixture("coq", "ane", 6),
            new ComparisonFixture("spider-man", "klingon", 13)
        );
    }

    @AfterEach
    void tearDown() {
        fixtures = null;
    }

    @Test
    @DisplayName("Applying the edit script transforms the source string into the target string")
    void testEditScriptAppliesExpectedTransformation() {
        final EditScriptApplyingVisitor<Character> applier = new EditScriptApplyingVisitor<>();
        for (final ComparisonFixture f : fixtures) {
            applier.resetWith(toCharList(f.source));
            new SequencesComparator<>(toCharList(f.source), toCharList(f.target))
                .getScript()
                .visit(applier);

            assertEquals(f.target, applier.asString(), "Source should transform into target");
        }
    }

    @Test
    @DisplayName("Reported number of modifications matches the expected count")
    void testReportedModificationCount() {
        for (final ComparisonFixture f : fixtures) {
            final SequencesComparator<Character> comparator =
                new SequencesComparator<>(toCharList(f.source), toCharList(f.target));

            assertEquals(f.expectedModifications,
                         comparator.getScript().getModifications(),
                         "Unexpected number of modifications");
        }
    }

    @Test
    @DisplayName("Script length is no greater than the number of random edits applied")
    void testNoLongerThanRandomEdits() {
        // Shadok “alphabet” (short tokens to keep sequences small)
        final String[] SHADOK = {"GA", "BU", "ZO", "MEU"};

        // A baseline sentence we will randomly edit
        final List<String> baseline = new ArrayList<>();
        baseline.add(SHADOK[0]);
        baseline.add(SHADOK[2]);
        baseline.add(SHADOK[3]);
        baseline.add(SHADOK[1]);
        baseline.add(SHADOK[0]);
        baseline.add(SHADOK[0]);
        baseline.add(SHADOK[2]);
        baseline.add(SHADOK[1]);
        baseline.add(SHADOK[3]);
        baseline.add(SHADOK[0]);
        baseline.add(SHADOK[2]);
        baseline.add(SHADOK[1]);
        baseline.add(SHADOK[3]);
        baseline.add(SHADOK[2]);
        baseline.add(SHADOK[2]);
        baseline.add(SHADOK[0]);
        baseline.add(SHADOK[1]);
        baseline.add(SHADOK[3]);
        baseline.add(SHADOK[0]);
        baseline.add(SHADOK[3]);

        final Random random = new Random(RANDOM_SEED);

        // Apply 0, 5, 10, ..., 40 random edits (each is an insert or delete).
        for (int maxEdits = 0; maxEdits <= 40; maxEdits += 5) {
            final List<String> edited = new ArrayList<>(baseline);

            for (int i = 0; i < maxEdits; i++) {
                if (random.nextInt(2) == 0 || edited.isEmpty()) {
                    // Insert a random token at a random position
                    edited.add(random.nextInt(edited.size() + 1), SHADOK[random.nextInt(SHADOK.length)]);
                } else {
                    // Delete a random token
                    edited.remove(random.nextInt(edited.size()));
                }
            }

            final SequencesComparator<String> comparator = new SequencesComparator<>(baseline, edited);
            assertTrue(comparator.getScript().getModifications() <= maxEdits,
                "Script should not be longer than the number of applied edits");
        }
    }

    @Test
    @DisplayName("Exhaustive Shadok test: every sentence transforms into every other")
    void testShadokExhaustive() {
        final String[] SHADOK = {"GA", "BU", "ZO", "MEU"};
        final int maxSentenceLength = 5;

        // Generate all sentences up to length maxSentenceLength from the SHADOK tokens.
        List<List<String>> sentences = new ArrayList<>();
        sentences.add(new ArrayList<>()); // start with the empty sentence
        for (int length = 0; length < maxSentenceLength; ++length) {
            final List<List<String>> next = new ArrayList<>();
            next.addAll(sentences); // keep existing sentences
            for (final String token : SHADOK) {
                for (final List<String> s : sentences) {
                    final List<String> extended = new ArrayList<>(s);
                    extended.add(token);
                    next.add(extended);
                }
            }
            sentences = next;
        }

        final EditScriptApplyingVisitor<String> applier = new EditScriptApplyingVisitor<>();

        // For every pair (from, to), applying the script must produce the "to" sentence.
        for (final List<String> from : sentences) {
            for (final List<String> to : sentences) {
                applier.resetWith(from);

                new SequencesComparator<>(from, to)
                    .getScript()
                    .visit(applier);

                // Build expected concatenation of the "to" sentence.
                final StringBuilder expected = new StringBuilder();
                for (final String token : to) {
                    expected.append(token);
                }

                assertEquals(expected.toString(), applier.asString(),
                    "Applying script should transform source into target");
            }
        }
    }
}