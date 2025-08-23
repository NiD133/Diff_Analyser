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

public class LoopingListIteratorTestTest12 {

    /**
     * Tests using the set method to change elements.
     */
    @Test
    void testSet() {
        final List<String> list = Arrays.asList("q", "r", "z");
        // <q> r z
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list);
        // q r <z>
        assertEquals("z", loop.previous());
        // q r <c>
        loop.set("c");
        // <q> r c
        loop.reset();
        // q <r> c
        assertEquals("q", loop.next());
        // a <r> c
        loop.set("a");
        // a r <c>
        assertEquals("r", loop.next());
        // a b <c>
        loop.set("b");
        // <a> b c
        loop.reset();
        // a <b> c
        assertEquals("a", loop.next());
        // a b <c>
        assertEquals("b", loop.next());
        // <a> b c
        assertEquals("c", loop.next());
    }
}
