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

public class LoopingListIteratorTestTest11 {

    /**
     * Tests the reset method.
     */
    @Test
    void testReset() {
        final List<String> list = Arrays.asList("a", "b", "c");
        // <a> b c
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list);
        // a <b> c
        assertEquals("a", loop.next());
        // a b <c>
        assertEquals("b", loop.next());
        // <a> b c
        loop.reset();
        // a <b> c
        assertEquals("a", loop.next());
        // <a> b c
        loop.reset();
        // a <b> c
        assertEquals("a", loop.next());
        // a b <c>
        assertEquals("b", loop.next());
        // <a> b c
        assertEquals("c", loop.next());
        // <a> b c
        loop.reset();
        // a b <c>
        assertEquals("c", loop.previous());
        // a <b> c
        assertEquals("b", loop.previous());
        // <a> b c
        loop.reset();
        // a b <c>
        assertEquals("c", loop.previous());
        // <a> b c
        loop.reset();
        // a b <c>
        assertEquals("c", loop.previous());
        // a <b> c
        assertEquals("b", loop.previous());
        // <a> b c
        assertEquals("a", loop.previous());
    }
}
