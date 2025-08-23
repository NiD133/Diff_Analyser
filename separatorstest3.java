package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeparatorsTestTest3 {

    @Test
    void withObjectFieldValueSeparatorWithDigit() {
        Separators separators = new Separators('5', '5', '5');
        Separators separatorsTwo = separators.withObjectFieldValueSeparator('5');
        assertEquals('5', separatorsTwo.getArrayValueSeparator());
        assertSame(separatorsTwo, separators);
        assertEquals('5', separatorsTwo.getObjectEntrySeparator());
        assertEquals('5', separatorsTwo.getObjectFieldValueSeparator());
        separatorsTwo = separators.withObjectFieldValueSeparator('6');
        assertEquals('5', separatorsTwo.getArrayValueSeparator());
        assertNotSame(separatorsTwo, separators);
        assertEquals('5', separatorsTwo.getObjectEntrySeparator());
        assertEquals('6', separatorsTwo.getObjectFieldValueSeparator());
    }
}
