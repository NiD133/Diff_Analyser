package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class LoopingListIteratorTestTest5 {

    /**
     * Tests whether an empty looping list iterator works.
     */
    @Test
    void testLooping0() {
        final List<Object> list = new ArrayList<>();
        final LoopingListIterator<Object> loop = new LoopingListIterator<>(list);
        assertFalse(loop.hasNext());
        assertFalse(loop.hasPrevious());
        assertThrows(NoSuchElementException.class, () -> loop.next());
        assertThrows(NoSuchElementException.class, () -> loop.previous());
    }
}
