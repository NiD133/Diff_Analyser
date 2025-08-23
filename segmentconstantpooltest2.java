package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the search utility method {@code SegmentConstantPool.matchSpecificPoolEntryIndex}.
 */
@DisplayName("SegmentConstantPool search utility")
public class SegmentConstantPoolSearchTest {

    private final String[] testClassArray = {
        "Object",             // 0
        "Object",             // 1
        "java/lang/String",   // 2
        "java/lang/String",   // 3
        "Object",             // 4
        "Other"               // 5
    };

    private TestableSegmentConstantPool constantPool;

    /**
     * A test-specific subclass of SegmentConstantPool that makes the protected
     * {@code matchSpecificPoolEntryIndex} method public for testing.
     */
    private static class TestableSegmentConstantPool extends SegmentConstantPool {
        TestableSegmentConstantPool() {
            super(new CpBands(new Segment()));
        }

        @Override
        public int matchSpecificPoolEntryIndex(final String[] classNameArray, final String desiredClassName, final int desiredIndex) {
            return super.matchSpecificPoolEntryIndex(classNameArray, desiredClassName, desiredIndex);
        }
    }

    @BeforeEach
    void setUp() {
        constantPool = new TestableSegmentConstantPool();
    }

    @DisplayName("should find the correct index for the Nth occurrence of an existing string")
    @ParameterizedTest(name = "finds {1} (occurrence {2}) at index {0}")
    @CsvSource({
        "0, Object, 0",              // 1st "Object" is at index 0
        "1, Object, 1",              // 2nd "Object" is at index 1
        "4, Object, 2",              // 3rd "Object" is at index 4
        "2, java/lang/String, 0",    // 1st "java/lang/String" is at index 2
        "3, java/lang/String, 1",    // 2nd "java/lang/String" is at index 3
        "5, Other, 0"                // 1st "Other" is at index 5
    })
    void shouldFindNthOccurrenceOfExistingString(int expectedIndex, String searchString, int occurrence) {
        // Act
        final int actualIndex = constantPool.matchSpecificPoolEntryIndex(testClassArray, searchString, occurrence);

        // Assert
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    @DisplayName("should return -1 if the target string is not in the array")
    void shouldReturnNegativeOneWhenStringIsNotFound() {
        // Act
        final int actualIndex = constantPool.matchSpecificPoolEntryIndex(testClassArray, "NonExistentString", 0);

        // Assert
        assertEquals(-1, actualIndex);
    }

    @Test
    @DisplayName("should return -1 if the Nth occurrence of a string does not exist")
    void shouldReturnNegativeOneWhenNthOccurrenceIsNotFound() {
        // There are only two instances of "java/lang/String", so searching for the third (index 2) should fail.
        // Act
        final int actualIndex = constantPool.matchSpecificPoolEntryIndex(testClassArray, "java/lang/String", 2);

        // Assert
        assertEquals(-1, actualIndex);
    }
}