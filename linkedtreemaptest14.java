package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;

/**
 * Contains tests for the {@link LinkedTreeMap} class, focusing on the {@code clear} method.
 */
public final class LinkedTreeMapClearTest {

    /**
     * Verifies that the {@code clear()} method correctly removes all entries,
     * leaving the map empty.
     */
    @Test
    public void clear_whenMapIsPopulated_removesAllEntries() {
        // Arrange: Create a map and populate it with several entries.
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        map.put("c", "cola");
        map.put("b", "bbq");

        // Sanity check to ensure the map is not empty before the test action.
        assertThat(map).isNotEmpty();

        // Act: Call the method under test.
        map.clear();

        // Assert: The map should be empty after being cleared.
        assertThat(map).isEmpty();
    }
}