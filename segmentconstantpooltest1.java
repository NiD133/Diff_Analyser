package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("SegmentConstantPool Test")
class SegmentConstantPoolTest {

    /**
     * A test-only subclass of SegmentConstantPool to make the protected method
     * `matchSpecificPoolEntryIndex` accessible for testing.
     */
    private static class TestableSegmentConstantPool extends SegmentConstantPool {
        TestableSegmentConstantPool() {
            super(new CpBands(new Segment()));
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, final String[] methodNameArray,
            final String desiredClassName, final String desiredMethodRegex, final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, methodNameArray, desiredClassName, desiredMethodRegex,
                desiredIndex);
        }
    }

    @Nested
    @DisplayName("when matching entries with two arrays")
    class MatchSpecificPoolEntryIndexWithTwoArrays {

        // Test data representing a segment's constant pool entries.
        // CLASS_NAMES[i] corresponds to METHOD_NAMES[i].
        //
        // Index | Class Name         | Method Name
        // ------|--------------------|------------
        // 0     | Object             | <init>()
        // 1     | Object             | clone()
        // 2     | java/lang/String   | equals()
        // 3     | java/lang/String   | <init>
        // 4     | Object             | isNull()
        // 5     | Other              | Other
        private final String[] CLASS_NAMES = {"Object", "Object", "java/lang/String", "java/lang/String", "Object", "Other"};
        private final String[] METHOD_NAMES = {"<init>()", "clone()", "equals()", "<init>", "isNull()", "Other"};

        private TestableSegmentConstantPool constantPool;

        @BeforeEach
        void setUp() {
            constantPool = new TestableSegmentConstantPool();
        }

        @ParameterizedTest(name = "Find {3}th match for class={1}, methodRegex={2} -> should be at index={0}")
        @CsvSource({
            // expectedIndex, className,          methodRegex,  desiredMatchCount
            "0,             'Object',            '^<init>.*',  0",
            "2,             'java/lang/String',  '.*',         0",
            "3,             'java/lang/String',  '^<init>.*',  0",
            "5,             'Other',             '.*',         0"
        })
        @DisplayName("should find the correct index for various successful search criteria")
        void shouldFindCorrectIndexOfMatchingEntry(int expectedIndex, String className, String methodRegex, int desiredMatchCount) {
            final int actualIndex = constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, METHOD_NAMES, className, methodRegex, desiredMatchCount);
            assertEquals(expectedIndex, actualIndex);
        }

        @Test
        @DisplayName("should return -1 when the desired class name is not found")
        void shouldReturnNotFoundForNonExistentClass() {
            final int index = constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, METHOD_NAMES, "NonExistentClass", ".*", 0);
            assertEquals(-1, index, "Should return -1 for a class name that does not exist in the pool");
        }

        @Test
        @DisplayName("should return -1 when the desired match count is not met")
        void shouldReturnNotFoundWhenDesiredIndexIsTooHigh() {
            // There is only one "java/lang/String" with an init method (at index 3).
            // Asking for the second one (desiredIndex = 1) should not find a match.
            final int index = constantPool.matchSpecificPoolEntryIndex(CLASS_NAMES, METHOD_NAMES, "java/lang/String", "^<init>.*", 1);
            assertEquals(-1, index, "Should return -1 when desired match count exceeds available matches");
        }
    }
}