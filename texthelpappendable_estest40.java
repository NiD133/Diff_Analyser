package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that makeColumnQueues throws an IndexOutOfBoundsException when the number of
     * data columns is greater than the number of provided styles.
     */
    @Test
    public void makeColumnQueuesShouldThrowExceptionWhenColumnsExceedStyles() {
        // Arrange: Create a formatter, one column of data, and an empty list of styles.
        TextHelpAppendable formatter = new TextHelpAppendable(new StringWriter());
        List<String> columnData = List.of("some data");
        List<TextStyle> styles = Collections.emptyList();

        // Act & Assert: Expect an exception because there is no style for the data column.
        assertThrows(IndexOutOfBoundsException.class, () -> {
            formatter.makeColumnQueues(columnData, styles);
        });
    }
}