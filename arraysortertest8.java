package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ArraySorterTestTest8 extends AbstractLangTest {

    @Test
    void testSortObjects() {
        final String[] array1 = ArrayUtils.toArray("foo", "bar");
        final String[] array2 = array1.clone();
        Arrays.sort(array1);
        assertArrayEquals(array1, ArraySorter.sort(array2));
        assertNull(ArraySorter.sort((String[]) null));
    }
}
