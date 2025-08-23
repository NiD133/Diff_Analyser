package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import java.util.Iterator;
import java.util.Map;
import org.junit.Test;

/**
 * Tests for {@link LinkedTreeMap}.
 */
public final class LinkedTreeMapTest {

    @Test
    public void iteratorRemove_whenRemovingNodeThatIsTreeRoot_maintainsIterationOrder() {
        // Arrange
        // LinkedTreeMap iterates by insertion order, but its internal tree is sorted by natural key order.
        // We choose an insertion order ("a", "c", "b") that makes "b" the root of the
        // internal AVL tree, creating a specific scenario to test.
        //
        // Insertion order: "a", "c", "b"
        // Natural key order: "a", "b", "c" -> Tree structure: root="b", left="a", right="c"
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        map.put("c", "cola");
        map.put("b", "bbq");

        // Act
        // The iterator follows insertion order. We advance it to the last-inserted element ("b").
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        iterator.next(); // "a"
        iterator.next(); // "c"
        iterator.next(); // "b"

        // Remove the last element returned by next(), which is the entry for "b".
        // This specifically tests the removal of the tree's root node via the iterator.
        iterator.remove();

        // Assert
        // The map should contain the remaining elements in their original insertion order.
        assertThat(map.size()).isEqualTo(2);
        assertThat(map.keySet()).containsExactly("a", "c").inOrder();
    }
}