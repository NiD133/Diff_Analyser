package org.jsoup.internal;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/*
 * Note: The class name `StringUtil_ESTestTest95` and the `extends` clause
 * are preserved from the original code. In a typical project, this would be
 * renamed to `StringUtilTest`.
 */
public class StringUtil_ESTestTest95 extends StringUtil_ESTest_scaffolding {

    /**
     * Tests that the Collector returned by StringUtil.joining() correctly
     * concatenates a stream of strings with a given delimiter.
     */
    @Test
    public void joiningCollectorShouldCorrectlyJoinStrings() {
        // Arrange: Set up the input data and the expected outcome.
        List<String> stringsToJoin = Arrays.asList("HTML", "CSS", "JavaScript");
        String delimiter = ", ";
        String expectedOutput = "HTML, CSS, JavaScript";

        // Act: Get the collector from the method under test.
        Collector<CharSequence, ?, String> joiningCollector = StringUtil.joining(delimiter);
        
        // Use the collector to process the stream of strings.
        String actualOutput = stringsToJoin.stream().collect(joiningCollector);

        // Assert: Verify that the collector works as expected.
        assertNotNull("The collector itself should not be null.", joiningCollector);
        assertEquals("The strings should be joined with the correct delimiter.", expectedOutput, actualOutput);
    }
}