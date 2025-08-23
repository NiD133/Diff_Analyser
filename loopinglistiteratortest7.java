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

public class LoopingListIteratorTestTest7 {

    /**
     * Tests whether a looping list iterator works on a list with two
     * elements.
     */
    @Test
    void testLooping2() {
        final List<String> list = Arrays.asList("a", "b");
        // <a> b
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list);
        assertTrue(loop.hasNext());
        // a <b>
        assertEquals("a", loop.next());
        assertTrue(loop.hasNext());
        // <a> b
        assertEquals("b", loop.next());
        assertTrue(loop.hasNext());
        // a <b>
        assertEquals("a", loop.next());
        // Reset the iterator and try using previous.
        // <a> b
        loop.reset();
        assertTrue(loop.hasPrevious());
        // a <b>
        assertEquals("b", loop.previous());
        assertTrue(loop.hasPrevious());
        // <a> b
        assertEquals("a", loop.previous());
        assertTrue(loop.hasPrevious());
        // a <b>
        assertEquals("b", loop.previous());
    }
}
