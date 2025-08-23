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

public class LoopingListIteratorTestTest4 {

    /**
     * Tests jogging back and forth between two elements over the
     * begin/end boundary of the list.
     */
    @Test
    void testJoggingOverBoundary() {
        final List<String> list = Arrays.asList("a", "b");
        // <a> b
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list);
        // Try jogging back and forth between the elements, but not
        // over the begin/end boundary.
        // a <b>
        assertEquals("b", loop.previous());
        // <a> b
        assertEquals("b", loop.next());
        // a <b>
        assertEquals("b", loop.previous());
        // <a> b
        assertEquals("a", loop.previous());
        // a <b>
        assertEquals("a", loop.next());
        // <a> b
        assertEquals("a", loop.previous());
    }
}
