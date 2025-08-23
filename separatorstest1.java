package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeparatorsTestTest1 {

    @Test
    void withArrayValueSeparatorWithDigit() {
        Separators separators = new Separators('5', '5', '5');
        Separators separatorsTwo = separators.withArrayValueSeparator('5');
        assertEquals('5', separatorsTwo.getObjectEntrySeparator());
        assertEquals('5', separatorsTwo.getObjectFieldValueSeparator());
        assertEquals('5', separatorsTwo.getArrayValueSeparator());
        assertSame(separatorsTwo, separators);
        separatorsTwo = separators.withArrayValueSeparator('6');
        assertEquals('5', separatorsTwo.getObjectEntrySeparator());
        assertEquals('5', separatorsTwo.getObjectFieldValueSeparator());
        assertEquals('6', separatorsTwo.getArrayValueSeparator());
        assertNotSame(separatorsTwo, separators);
    }
}
