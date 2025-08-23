package org.joda.time.convert;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * This test class contains tests related to the ConverterSet.
 * The following test, originally from a generated suite, does not test ConverterSet directly.
 * Instead, it verifies the behavior of a Joda-Time component (MutablePeriod) that relies on the
 * ConverterManager, which in turn uses a ConverterSet to find the correct converter.
 * This specific test covers the scenario of handling a null input object.
 */
public class ConverterSet_ESTestTest3 {

    /**
     * Tests that the MutablePeriod constructor can successfully handle a null object
     * when a PeriodType is specified.
     * <p>
     * This indirectly verifies that the underlying ConverterManager's ConverterSet
     * correctly selects the NullConverter to parse the null input, preventing a
     * NullPointerException.
     */
    @Test
    public void mutablePeriodConstructorShouldHandleNullObjectGracefully() {
        // Arrange: Define a standard period type and a null object to be converted.
        final PeriodType periodType = PeriodType.standard();
        final Object nullPeriodObject = null;

        // Act: Attempt to create a MutablePeriod from the null object.
        // This action triggers the ConverterManager to find a suitable converter.
        final MutablePeriod period = new MutablePeriod(nullPeriodObject, periodType);

        // Assert: The test succeeds if no exception is thrown. We add an explicit
        // assertion to confirm that the resulting period object is not null.
        assertNotNull("The resulting MutablePeriod should not be null.", period);
    }
}