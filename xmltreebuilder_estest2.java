package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link XmlTreeBuilder}.
 * This class focuses on specific edge cases and state transitions.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that attempting to process a new start tag after a document has been
     * fully parsed results in a NullPointerException. This is because the builder's
     * internal state is finalized after parsing completes, and it is not designed
     * to accept new tokens.
     */
    @Test(timeout = 4000)
    public void shouldThrowExceptionWhenProcessingTagAfterParsingIsComplete() {
        // Arrange: Create a builder and execute a full parse operation, which leaves
        // the builder in a "finished" state.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<doc>content</doc>", "http://example.com");

        // Act & Assert: Attempting to process another tag should cause an exception.
        try {
            xmlTreeBuilder.processStartTag("newTag");
            fail("Expected a NullPointerException because the parser is not in a state to process new tags after parsing is complete.");
        } catch (NullPointerException e) {
            // This is the expected outcome. The test passes.
        }
    }
}