package org.joda.time.convert;

import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;

/**
 * This test verifies the behavior of the period conversion system when an unsupported
 * object type is used to construct a period.
 */
public class PeriodConverterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that creating a MutablePeriod from a PeriodType object fails because
     * there is no registered converter that can handle this conversion.
     */
    @Test
    public void constructorShouldThrowExceptionForUnsupportedPeriodSourceType() {
        // Arrange: A PeriodType object is not a supported source for creating a Period.
        // The internal ConverterManager is expected to fail when searching for a suitable converter.
        final PeriodType unsupportedSourceType = PeriodType.seconds();

        // Assert: Configure the expected exception and its message.
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(is("No period converter found for type: org.joda.time.PeriodType"));

        // Act: Attempt to create a MutablePeriod using the unsupported source type.
        // This call is expected to throw the configured exception.
        new MutablePeriod(unsupportedSourceType, PeriodType.standard());
    }
}