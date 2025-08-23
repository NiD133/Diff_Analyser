package org.apache.commons.cli.help;

import org.apache.commons.cli.help.TextStyle;
import org.apache.commons.cli.help.TextHelpAppendable;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Test suite for {@link TextHelpAppendable}.
 * This specific test was improved for clarity.
 */
// The original test class name and inheritance are preserved to maintain context.
public class TextHelpAppendable_ESTestTest26 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that calling writeColumnQueues with a null list of queues
     * results in a NullPointerException, as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void writeColumnQueuesShouldThrowNullPointerExceptionWhenQueuesListIsNull() throws IOException {
        // Arrange: Create an instance of the class under test and required parameters.
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        List<TextStyle> styles = new ArrayList<>(); // An empty list of styles is valid.
        List<Queue<String>> nullColumnQueues = null;

        // Act & Assert: Call the method with a null argument.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        helpAppendable.writeColumnQueues(nullColumnQueues, styles);
    }
}