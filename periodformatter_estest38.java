package org.joda.time.format;

import org.joda.time.PeriodType;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This test class contains the improved test case.
 * The original test class name and inheritance are kept for context.
 */
public class PeriodFormatter_ESTestTest38 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests the immutability contract of the withParseType() method.
     * <p>
     * It verifies that:
     * 1. Calling withParseType() with a new type returns a new formatter instance.
     * 2. Calling withParseType() again with the same type returns the same instance (an optimization).
     */
    @Test
    public void withParseType_shouldReturnNewInstanceForDifferentType_andSameInstanceForSameType() {
        // Arrange: Create an initial formatter with a null parse type.
        // A no-op parser is used simply to satisfy the PeriodFormatter constructor.
        List<Object> emptyCompositeList = Collections.emptyList();
        PeriodParser parser = new PeriodFormatterBuilder.Composite(emptyCompositeList);
        PeriodFormatter initialFormatter = new PeriodFormatter(null, parser);

        // Precondition check to ensure our starting state is as expected.
        assertNull("Precondition: Initial formatter's parse type should be null.", initialFormatter.getParseType());

        // Act: Set the parse type for the first time.
        PeriodType yearsPeriodType = PeriodType.years();
        PeriodFormatter formatterWithYearsType = initialFormatter.withParseType(yearsPeriodType);

        // Act: Re-apply the same parse type to the new formatter.
        PeriodFormatter reappliedFormatter = formatterWithYearsType.withParseType(yearsPeriodType);

        // Assert:
        // 1. Applying a new type should create a new instance.
        assertNotSame("Applying a new parse type should return a new formatter instance.",
                initialFormatter, formatterWithYearsType);
        assertEquals("The new formatter should have the correct parse type.",
                yearsPeriodType, formatterWithYearsType.getParseType());

        // 2. Re-applying the same type should return the same instance due to immutability optimization.
        assertSame("Re-applying the same parse type should return the same formatter instance.",
                formatterWithYearsType, reappliedFormatter);
    }
}