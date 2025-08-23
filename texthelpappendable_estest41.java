package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on edge cases for its methods.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that makeColumnQueues throws an ArrayIndexOutOfBoundsException when the
     * list of styles is shorter than the list of column data. The method is expected
     * to access a style for each data item, which fails if the lists are mismatched.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void makeColumnQueuesShouldThrowExceptionWhenStylesListIsShorterThanDataList() {
        // Arrange: Create a TextHelpAppendable instance and prepare input lists where
        // the number of data columns (1) is greater than the number of styles (0).
        // A StringWriter is used to prevent the test from writing to System.out.
        TextHelpAppendable textHelpAppendable = new TextHelpAppendable(new StringWriter());
        List<String> columnData = Collections.singletonList("some-column-data");
        List<TextStyle> styles = Collections.emptyList();

        // Act: Call the method under test with the mismatched lists.
        textHelpAppendable.makeColumnQueues(columnData, styles);

        // Assert: The test will pass only if the expected ArrayIndexOutOfBoundsException
        // is thrown, which is handled by the @Test(expected=...) annotation.
    }
}