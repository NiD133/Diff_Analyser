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

public class LoopingListIteratorTestTest8 {

    /**
     * Tests nextIndex and previousIndex.
     */
    @Test
    void testNextAndPreviousIndex() {
        final List<String> list = Arrays.asList("a", "b", "c");
        // <a> b c
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list);
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());
        // a <b> c
        assertEquals("a", loop.next());
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());
        // <a> b c
        assertEquals("a", loop.previous());
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());
        // a b <c>
        assertEquals("c", loop.previous());
        assertEquals(2, loop.nextIndex());
        assertEquals(1, loop.previousIndex());
        // a <b> c
        assertEquals("b", loop.previous());
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());
        // <a> b c
        assertEquals("a", loop.previous());
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());
    }
}
