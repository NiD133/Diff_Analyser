package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the Tokeniser.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class Tokeniser_ESTestTest24 extends Tokeniser_ESTest_scaffolding {

    /**
     * Tests that appropriateEndTagSeq() returns a sequence based on a null tag name
     * when no start tag has been processed by the tokeniser yet.
     */
    @Test
    public void appropriateEndTagSeqReturnsNullSequenceWhenNoStartTagEncountered() {
        // Arrange: Create a tokeniser that has not yet processed any tags.
        // In this initial state, its internal 'lastStartTag' field is null.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Tokeniser tokeniser = new Tokeniser(treeBuilder);

        // Act: Get the appropriate end tag sequence.
        String endTagSeq = tokeniser.appropriateEndTagSeq();

        // Assert: The method should construct the sequence from a null tag name.
        // The expected result is from the string concatenation of "</" + null,
        // which yields "</null".
        assertEquals("</null", endTagSeq);
    }
}