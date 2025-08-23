package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;

import java.util.function.BiFunction;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that the {@code toSyntaxOption} method correctly uses a custom
     * syntax formatting function provided via the builder.
     */
    @Test
    public void toSyntaxOptionShouldUseCustomSyntaxFormatFunction() {
        // Arrange
        // 1. Define a simple Option to be formatted. Its specific attributes are not critical for this test.
        final Option option = new Option("a", "alpha", false, "An alpha option.");

        // 2. Create a mock function to serve as a custom syntax formatter.
        //    This allows us to control its behavior and verify it's being used.
        @SuppressWarnings("unchecked") // Necessary for mocking generic types
        final BiFunction<OptionFormatter, Boolean, String> mockSyntaxFormatFunction = mock(BiFunction.class);

        // 3. Configure the mock to return null when its 'apply' method is called.
        //    This is the specific outcome we will assert.
        when(mockSyntaxFormatFunction.apply(any(OptionFormatter.class), anyBoolean())).thenReturn(null);

        // 4. Build an OptionFormatter, injecting our custom mock function.
        final OptionFormatter formatter = OptionFormatter.builder()
                .setSyntaxFormatFunction(mockSyntaxFormatFunction)
                .build(option);

        // Act
        // Call the method under test. This should invoke our mock function internally.
        final String syntaxString = formatter.toSyntaxOption(false);

        // Assert
        // Verify that the result is null, as dictated by our mock's configuration.
        // This confirms that the custom function was used and its return value was passed through.
        assertNull("The result should be null as returned by the custom syntax function.", syntaxString);
    }
}