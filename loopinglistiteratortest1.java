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

public class LoopingListIteratorTestTest1 {

    /**
     * Tests the add method.
     */
    @Test
    void testAdd() {
        List<String> list = new ArrayList<>(Arrays.asList("b", "e", "f"));
        // <b> e f
        LoopingListIterator<String> loop = new LoopingListIterator<>(list);
        // <a> b e f
        loop.add("a");
        // a <b> e f
        assertEquals("b", loop.next());
        // <a> b e f
        loop.reset();
        // a <b> e f
        assertEquals("a", loop.next());
        // a b <e> f
        assertEquals("b", loop.next());
        // a b c <e> f
        loop.add("c");
        // a b c e <f>
        assertEquals("e", loop.next());
        // a b c <e> f
        assertEquals("e", loop.previous());
        // a b <c> e f
        assertEquals("c", loop.previous());
        // a b c <e> f
        assertEquals("c", loop.next());
        // a b c d <e> f
        loop.add("d");
        // <a> b c d e f
        loop.reset();
        // a <b> c d e f
        assertEquals("a", loop.next());
        // a b <c> d e f
        assertEquals("b", loop.next());
        // a b c <d> e f
        assertEquals("c", loop.next());
        // a b c d <e> f
        assertEquals("d", loop.next());
        // a b c d e <f>
        assertEquals("e", loop.next());
        // <a> b c d e f
        assertEquals("f", loop.next());
        // a <b> c d e f
        assertEquals("a", loop.next());
        list = new ArrayList<>(Arrays.asList("b", "e", "f"));
        // <b> e f
        loop = new LoopingListIterator<>(list);
        // a <b> e f
        loop.add("a");
        // a b e <f>
        assertEquals("a", loop.previous());
        // <a> b e f
        loop.reset();
        // a b e <f>
        assertEquals("f", loop.previous());
        // a b <e> f
        assertEquals("e", loop.previous());
        // a b d <e> f
        loop.add("d");
        // a b <d> e f
        assertEquals("d", loop.previous());
        // a b c <d> e f
        loop.add("c");
        // a b <c> d e f
        assertEquals("c", loop.previous());
        loop.reset();
        // a <b> c d e f
        assertEquals("a", loop.next());
        // a b <c> d e f
        assertEquals("b", loop.next());
        // a b c <d> e f
        assertEquals("c", loop.next());
        // a b c d <e> f
        assertEquals("d", loop.next());
        // a b c d e <f>
        assertEquals("e", loop.next());
        // <a> b c d e f
        assertEquals("f", loop.next());
        // a <b> c d e f
        assertEquals("a", loop.next());
    }
}
