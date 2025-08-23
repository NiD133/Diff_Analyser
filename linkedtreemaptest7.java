package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import java.util.Map.Entry;
import org.junit.Test;

public class LinkedTreeMapTestTest7 {

    /**
     * Tests that updating an entry's value to null via {@link Entry#setValue(Object)}
     * correctly reflects the change in both the entry itself and the underlying map.
     * The map is created with the default constructor, which allows null values.
     */
    @Test
    public void setValueOnEntryToNull_updatesBothEntryAndMap() {
        // Arrange: Create a map with one entry and get a reference to that entry.
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "1");
        Entry<String, String> entry = map.entrySet().iterator().next();

        // Sanity check the initial state.
        assertThat(entry.getValue()).isEqualTo("1");
        assertThat(map.get("a")).isEqualTo("1");

        // Act: Set the entry's value to null.
        String previousValue = entry.setValue(null);

        // Assert: Verify the state after the update.
        assertThat(previousValue).isEqualTo("1");
        assertThat(entry.getValue()).isNull();

        // Verify that the map correctly reflects the change.
        assertThat(map.get("a")).isNull();
        assertThat(map.containsKey("a")).isTrue();
        assertThat(map.containsValue(null)).isTrue();
    }
}