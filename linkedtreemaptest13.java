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

public class LinkedTreeMapTestTest13 {

    @Test
    public void testLargeSetOfRandomKeys() {
        Random random = new Random(1367593214724L);
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        String[] keys = new String[1000];
        for (int i = 0; i < keys.length; i++) {
            keys[i] = Integer.toString(random.nextInt(), 36) + "-" + i;
            map.put(keys[i], "" + i);
        }
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            assertThat(map.containsKey(key)).isTrue();
            assertThat(map.get(key)).isEqualTo("" + i);
        }
    }
}