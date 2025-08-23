package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CharSetUtils_ESTestTest18 extends CharSetUtils_ESTest_scaffolding {

    /**
     * Tests that CharSetUtils.count correctly handles a null element within the set array.
     * The method should ignore the null and count only the characters from the non-null sets.
     */
    @Test(timeout = 4000)
    public void testCountShouldIgnoreNullInSetArray() throws Throwable {
        // Arrange
        // The set of characters to search for, including a null element.
        // The count method should gracefully ignore the null and use the characters from the valid string.
        final String[] characterSet = {"h|Bv_9mUP7'&Y", null};
        final String stringToSearch = "^MPkb@$Zu";

        // The character 'P' is the only one from stringToSearch that is also present in the characterSet.
        final int expectedCount = 1;

        // Act
        final int actualCount = CharSetUtils.count(stringToSearch, characterSet);

        // Assert
        assertEquals("The count should be 1 because only 'P' is in the set, and the null in the set array should be ignored.",
                expectedCount, actualCount);
    }
}