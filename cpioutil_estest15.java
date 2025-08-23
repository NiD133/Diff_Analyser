package org.apache.commons.compress.archivers.cpio;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link CpioUtil} class.
 */
public class CpioUtilTest {

    /**
     * Verifies that an instance of the CpioUtil class can be created.
     * <p>
     * CpioUtil is a utility class containing only static methods and is not
     * typically instantiated. This test serves as a basic smoke test for the
     * constructor and helps achieve full code coverage.
     * </p>
     */
    @Test
    public void shouldCreateInstanceSuccessfully() {
        // Arrange & Act: Attempt to create an instance of the utility class.
        CpioUtil cpioUtilInstance = new CpioUtil();

        // Assert: Ensure the instance was created successfully.
        assertNotNull("The instance of CpioUtil should not be null.", cpioUtilInstance);
    }
}