package org.apache.commons.io.function;

import org.junit.Test;
import java.util.function.Supplier;

/**
 * Contains tests for the {@link Uncheck} utility class.
 * Note: The original class name and inheritance were preserved for context.
 * In a typical project, a more descriptive name like "UncheckTest" would be preferred.
 */
public class Uncheck_ESTestTest51 extends Uncheck_ESTest_scaffolding {

    /**
     * Tests that calling {@link Uncheck#getAsLong(IOLongSupplier, Supplier)} with a null
     * {@link IOLongSupplier} throws a {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void getAsLongWithNullSupplierShouldThrowNullPointerException() {
        // Arrange: The IOLongSupplier is null. The message supplier is not relevant
        // for this test and can also be null.
        IOLongSupplier nullSupplier = null;
        Supplier<String> nullMessageSupplier = null;

        // Act & Assert: Call the method under test.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        Uncheck.getAsLong(nullSupplier, nullMessageSupplier);
    }
}