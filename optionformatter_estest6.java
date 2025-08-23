package org.apache.commons.cli.help;

import org.apache.commons.cli.Option;
import org.junit.Test;
import java.util.function.BiFunction;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link OptionFormatter}.
 */
public class OptionFormatterTest {

    /**
     * Verifies that the toSyntaxOption() method correctly uses a custom syntax formatting function
     * provided via the builder and returns its result.
     */
    @Test
    public void toSyntaxOptionShouldReturnResultFromCustomSyntaxFormatFunction() {
        // Arrange
        // 1. Define a mock function that will be used to format the option's syntax.
        //    This mock is configured to always return null.
        @SuppressWarnings("unchecked") // Necessary for mocking generic types
        final BiFunction<OptionFormatter, Boolean, String> customSyntaxFunction = mock(BiFunction.class);
        when(customSyntaxFunction.apply(any(OptionFormatter.class), anyBoolean())).thenReturn(null);

        // 2. Create a sample option to be formatted. The specific details of this option
        //    are not critical for this test, as we are mocking the formatting logic.
        final Option option = new Option("a", "alpha", true, "An example option");

        // 3. Build an OptionFormatter, injecting our custom formatting function.
        final OptionFormatter formatter = OptionFormatter.builder()
                .setSyntaxFormatFunction(customSyntaxFunction)
                .build(option);

        // Act
        // Call the method under test, which should delegate to our custom function.
        final String actualSyntax = formatter.toSyntaxOption();

        // Assert
        // The result should be null, as dictated by our mock function's configuration.
        assertNull("The syntax string should be null as returned by the custom function.", actualSyntax);

        // Additionally, verify that our custom function was indeed called exactly once.
        verify(customSyntaxFunction).apply(any(OptionFormatter.class), anyBoolean());
    }
}