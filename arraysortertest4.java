package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ArraySorterTestTest4 extends AbstractLangTest {

    @Test
    void testSortDoubleArray() {
        final double[] array1 = { 2, 1 };
        final double[] array2 = array1.clone();
        Arrays.sort(array1);
        assertArrayEquals(array1, ArraySorter.sort(array2));
        assertNull(ArraySorter.sort((double[]) null));
    }
}
