package org.apache.commons.io.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * Tests for {@link Uncheck}.
 * This class focuses on the successful execution path of the getAsInt method.
 */
// The original test was part of an EvoSuite-generated suite.
// We keep the class name and inheritance for context, but focus on improving the test method itself.
public class Uncheck_ESTestTest10 extends Uncheck_ESTest_scaffolding {

    /**
     * Tests that {@link Uncheck#getAsInt(IOIntSupplier, Supplier)} successfully returns the integer
     * from the supplier when the supplier does not throw an {@link IOException}.
     */
    @Test
    public void getAsIntShouldReturnValueFromSupplierWhenNoExceptionIsThrown() throws IOException {
        // Arrange
        final int expectedResult = 0;
        final IOIntSupplier mockSupplier = mock(IOIntSupplier.class);

        // Configure the mock supplier to return a specific value.
        // The `getAsInt()` method in the IOIntSupplier interface is declared to throw IOException,
        // so the test method must also declare it to satisfy the mock setup.
        doReturn(expectedResult).when(mockSupplier).getAsInt();

        // Act
        // Call the method under test. The message supplier is null, which is a valid scenario.
        final int actualResult = Uncheck.getAsInt(mockSupplier, null);

        // Assert
        assertEquals("The returned value should match the one from the supplier.", expectedResult, actualResult);
    }
}