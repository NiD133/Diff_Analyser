package com.itextpdf.text.error_messages;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// The original test class was run with EvoRunner and extended a scaffolding class.
// These are retained to match the original test's structure, though in a typical
// hand-written test, they might not be necessary.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the {@link MessageLocalization} class.
 * Note: The original class name 'MessageLocalization_ESTestTest3' was likely
 * auto-generated. A more conventional name would be 'MessageLocalizationTest'.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MessageLocalization_ESTestTest3 extends MessageLocalization_ESTest_scaffolding {

    /**
     * Verifies that getComposedMessage() returns a standard fallback message
     * when the requested message key does not exist in the resource files.
     */
    @Test
    public void getComposedMessage_withNonExistentKey_returnsFallbackMessage() {
        // Arrange: Define a key that is guaranteed not to exist and the expected fallback message.
        String nonExistentKey = "a.key.that.is.not.real";
        Object[] irrelevantParams = {"parameter1", 123};
        String expectedMessage = "No message found for " + nonExistentKey;

        // Act: Call the method under test with the non-existent key.
        String actualMessage = MessageLocalization.getComposedMessage(nonExistentKey, irrelevantParams);

        // Assert: Verify that the actual message matches the expected fallback message.
        assertEquals("The method should return a standard 'not found' message for an unknown key.",
                expectedMessage, actualMessage);
    }
}