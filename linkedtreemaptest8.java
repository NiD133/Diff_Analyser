package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.util.Map.Entry;
import org.junit.Test;

/**
 * Test for {@link LinkedTreeMap} focusing on entry behavior.
 * Note: Original class name was LinkedTreeMapTestTest8.
 */
public class LinkedTreeMapTest {

    @Test
    public void setValueOnEntry_whenNullsDisallowed_throwsExceptionAndDoesNotModifyMap() {
        // Arrange: Create a map configured to disallow null values and add an entry.
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>(/* allowNullValues= */ false);
        map.put("a", "1");
        Entry<String, String> entry = map.entrySet().iterator().next();

        // Act & Assert: Verify that attempting to set a null value throws a NullPointerException.
        NullPointerException exception =
            assertThrows(NullPointerException.class, () -> entry.setValue(null));
        assertThat(exception).hasMessageThat().isEqualTo("value == null");

        // Assert: Verify that the failed operation did not alter the state of the map or the entry.
        assertThat(map.get("a")).isEqualTo("1");
        assertThat(entry.getValue()).isEqualTo("1");
        assertThat(map.containsValue(null)).isFalse();
    }
}