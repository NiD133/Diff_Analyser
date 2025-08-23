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

public class LoopingListIteratorTestTest10 {

    /**
     * Tests removing an element from a wrapped ArrayList.
     */
    @Test
    void testRemovingElementsAndIteratingForward() {
        final List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        // <a> b c
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list);
        assertTrue(loop.hasNext());
        // a <b> c
        assertEquals("a", loop.next());
        // <b> c
        loop.remove();
        assertEquals(2, list.size());
        assertTrue(loop.hasNext());
        // b <c>
        assertEquals("b", loop.next());
        // <c>
        loop.remove();
        assertEquals(1, list.size());
        assertTrue(loop.hasNext());
        // <c>
        assertEquals("c", loop.next());
        // ---
        loop.remove();
        assertEquals(0, list.size());
        assertFalse(loop.hasNext());
        assertThrows(NoSuchElementException.class, () -> loop.next());
    }
}
