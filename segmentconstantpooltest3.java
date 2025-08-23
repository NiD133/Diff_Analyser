package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * This test class focuses on verifying the behavior of the custom
 * {@link SegmentConstantPool#regexMatches(String, String)} method.
 *
 * It uses a test-specific subclass to gain access to the protected static method.
 */
@DisplayName("SegmentConstantPool.regexMatches()")
class SegmentConstantPoolRegexMatchesTest {

    private TestableSegmentConstantPool pool;

    /**
     * A test-specific subclass of {@link SegmentConstantPool} to expose the protected
     * static {@code regexMatches} method for testing.
     */
    private static class TestableSegmentConstantPool extends SegmentConstantPool {
        TestableSegmentConstantPool() {
            // The super constructor requires a CpBands instance, which is not used by the method under test.
            // We provide a minimal, non-null instance to ensure safe construction.
            super(new CpBands(new Segment()));
        }

        public boolean callRegexMatches(final String regexString, final String compareString) {
            return SegmentConstantPool.regexMatches(regexString, compareString);
        }
    }

    @BeforeEach
    void setUp() {
        pool = new TestableSegmentConstantPool();
    }

    @Nested
    @DisplayName("with wildcard pattern '.*'")
    class WildcardPattern {
        @Test
        void shouldMatchAnyString() {
            assertTrue(pool.callRegexMatches(".*", "anything"), "Should match a non-empty string.");
        }

        @Test
        void shouldMatchEmptyString() {
            assertTrue(pool.callRegexMatches(".*", ""), "Should match an empty string.");
        }
    }

    @Nested
    @DisplayName("with init pattern '^<init>.*'")
    class InitPattern {
        private static final String INIT_PATTERN = "^<init>.*";

        @Test
        void shouldMatchStringsStartingWithInit() {
            assertTrue(pool.callRegexMatches(INIT_PATTERN, "<init>"), "Should match the exact '<init>' string.");
            assertTrue(pool.callRegexMatches(INIT_PATTERN, "<init>withSuffix"), "Should match '<init>' followed by other characters.");
        }

        @Test
        void shouldNotMatchIncorrectOrPartialStrings() {
            assertFalse(pool.callRegexMatches(INIT_PATTERN, "init>stuff"), "Should not match if it does not start with '<'.");
            assertFalse(pool.callRegexMatches(INIT_PATTERN, "<init"), "Should not match an incomplete '<init>' string.");
            assertFalse(pool.callRegexMatches(INIT_PATTERN, ""), "Should not match an empty string.");
        }
    }
}