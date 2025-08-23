package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.common.MoreAsserts;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.Test;

public class LinkedTreeMapTestTest7 {

    @Test
    public void testEntrySetValueNull() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "1");
        assertThat(map.get("a")).isEqualTo("1");
        Entry<String, String> entry = map.entrySet().iterator().next();
        assertThat(entry.getKey()).isEqualTo("a");
        assertThat(entry.getValue()).isEqualTo("1");
        entry.setValue(null);
        assertThat(entry.getValue()).isNull();
        assertThat(map.containsKey("a")).isTrue();
        assertThat(map.containsValue(null)).isTrue();
        assertThat(map.get("a")).isNull();
    }
}
