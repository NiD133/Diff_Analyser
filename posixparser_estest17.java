package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Test case for the PosixParser class, focusing on the burstToken method.
 */
public class PosixParserTest {

    /**
     * Tests that burstToken correctly handles a token containing a non-option character
     * when the 'stopAtNonOption' flag is enabled.
     *
     * <p>The original test called {@code burstToken(";-", true)} after an initial setup.
     * This improved version clarifies the purpose and verifies the correct outcome.</p>
     *
     * <p>The expected behavior is that when {@code burstToken} encounters a character that is not a
     * registered option (in this case, the second character '-'), it should stop parsing,
     * add a "--" token, and then add the remainder of the original token ("-") to its
     * internal list of tokens.</p>
     */
    @Test
    public void burstTokenShouldStopAtNonOptionCharacterWhenFlagIsEnabled() throws Exception {
        // Arrange: Set up the parser and its required state.
        PosixParser parser = new PosixParser();
        Options options = new Options();

        // The burstToken method is protected and depends on the parser's internal 'options'
        // field being initialized. The public 'flatten' method is the standard way to
        // initialize the parser, so we call it here as part of the setup.
        parser.flatten(options, new String[0], false);

        String tokenContainingNonOption = ";-";
        boolean stopAtNonOption = true;

        // Act: Execute the method under test.
        parser.burstToken(tokenContainingNonOption, stopAtNonOption);

        // Assert: Verify the internal state of the parser reflects the expected outcome.
        // The 'tokens' list inside the parser is private, so we use reflection to access
        // it for verification. This is acceptable for testing legacy or hard-to-test code.
        Field tokensField = PosixParser.class.getDeclaredField("tokens");
        tokensField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> actualTokens = (List<String>) tokensField.get(parser);

        // Expect ["--", "-"] because parsing stops at the second char '-', which is not a
        // defined option. The parser then adds "--" and the rest of the token.
        String[] expectedTokens = {"--", "-"};
        assertArrayEquals(expectedTokens, actualTokens.toArray(new String[0]));
    }
}