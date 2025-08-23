package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartesianProductIteratorTestTest4 extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;

    private List<Character> numbers;

    private List<Character> symbols;

    private List<Character> emptyList;

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    @BeforeEach
    public void setUp() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
        emptyList = Collections.emptyList();
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    /**
     * test checking that no tuples are returned when first of the lists is empty
     */
    @Test
    void testExhaustivityWithEmptyFirstList() {
        final List<Character[]> resultsList = new ArrayList<>();
        final CartesianProductIterator<Character> it = new CartesianProductIterator<>(emptyList, numbers, symbols);
        while (it.hasNext()) {
            final List<Character> tuple = it.next();
            resultsList.add(tuple.toArray(new Character[0]));
        }
        assertThrows(NoSuchElementException.class, it::next);
        assertEquals(0, resultsList.size());
    }
}
