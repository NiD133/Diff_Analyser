package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The class name and inheritance from scaffolding are kept from the original code,
// but the test method inside has been completely rewritten for clarity.
public class Tokeniser_ESTestTest1 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that the transition() method correctly updates the Tokeniser's internal state.
     */
    @Test
    public void transitionShouldUpdateTokeniserState() {
        // Arrange: Create a Tokeniser instance.
        // A TreeBuilder is needed for the constructor. We'll use an XmlTreeBuilder.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        // The parse method initializes an internal reader, which is required by the Tokeniser.
        // The input content is irrelevant for this test.
        treeBuilder.parse("", "");
        Tokeniser tokeniser = new Tokeniser(treeBuilder);

        // A new tokeniser should start in the 'Data' state.
        assertEquals("Initial state should be Data", TokeniserState.Data, tokeniser.state);

        // Act: Transition the tokeniser to a new state.
        TokeniserState newState = TokeniserState.DoctypeSystemIdentifier_singleQuoted;
        tokeniser.transition(newState);

        // Assert: Verify the state has been updated correctly.
        // The 'state' field is package-private, allowing direct access from this test class
        // in the same package.
        assertEquals("Tokeniser state should be updated after transition", newState, tokeniser.state);
    }
}