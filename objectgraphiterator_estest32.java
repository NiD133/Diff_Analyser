package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * An understandable test suite for {@link ObjectGraphIterator}.
 *
 * This class provides clear, focused tests for the core functionality of the
 * ObjectGraphIterator, emphasizing readability and maintainability over code coverage
 * of internal methods.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that the ObjectGraphIterator can traverse a simple path
     * defined in a Map using a MapTransformer.
     *
     * <p>The test sets up a map representing a path (A -> B -> C) and
     * verifies that the iterator correctly follows this path from the
     * starting root "A", returning each node in sequence.</p>
     */
    @Test
    public void testIterationWithMapTransformerNavigatesMapPath() {
        // Arrange: Define a graph structure where each key maps to the next node in the path.
        // The path to traverse is "A" -> "B" -> "C". The path ends at "C" because
        // it is not a key in the map, so the transformer will return null.
        final Map<String, String> graph = new HashMap<>();
        graph.put("A", "B");
        graph.put("B", "C");

        final Transformer<String, String> graphTransformer = MapTransformer.mapTransformer(graph);
        final ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>("A", graphTransformer);

        // Act: Collect all elements from the iterator into a list.
        final List<String> iteratedPath = new ArrayList<>();
        iterator.forEachRemaining(iteratedPath::add);

        // Assert: Verify that the collected path is correct.
        final List<String> expectedPath = List.of("A", "B", "C");
        assertEquals("The iterated path should match the expected path", expectedPath, iteratedPath);

        // Assert: Verify the iterator is exhausted after the traversal.
        assertFalse("Iterator should have no more elements", iterator.hasNext());
        assertThrows("Calling next() on an exhausted iterator should throw an exception",
                NoSuchElementException.class, iterator::next);
    }
}