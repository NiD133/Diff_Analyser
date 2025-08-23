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

public class LinkedTreeMapTestTest6 {

    @Test
    public void testPutNullValue_Forbidden() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>(false);
        var e = assertThrows(NullPointerException.class, () -> map.put("a", null));
        assertThat(e).hasMessageThat().isEqualTo("value == null");
        assertThat(map).hasSize(0);
        assertThat(map).doesNotContainKey("a");
        assertThat(map.containsValue(null)).isFalse();
    }
}