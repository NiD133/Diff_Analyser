package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SeparatorsTestTest2 {

    @Test
    void withObjectEntrySeparator() {
        Separators separators = new Separators('5', '5', '5');
        Separators separatorsTwo = separators.withObjectEntrySeparator('!');
        Separators separatorsThree = separatorsTwo.withObjectEntrySeparator('!');
        assertEquals('!', separatorsThree.getObjectEntrySeparator());
        assertEquals('5', separatorsThree.getObjectFieldValueSeparator());
        assertSame(separatorsThree, separatorsTwo);
        assertEquals('5', separators.getArrayValueSeparator());
        assertEquals('5', separatorsThree.getArrayValueSeparator());
        assertEquals('5', separators.getObjectFieldValueSeparator());
    }
}
