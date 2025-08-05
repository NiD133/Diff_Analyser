/*
 * Copyright (C) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

public final class LinkedTreeMapTest {
    private static final int RANDOM_KEYS_COUNT = 1000;
    private static final long RANDOM_SEED = 1367593214724L;

    // Tests for basic insertion and iteration order
    @Test
    public void insertionOrder_maintainedInKeySetAndValues() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        map.put("c", "cola");
        map.put("b", "bbq");
        
        assertThat(map.keySet()).containsExactly("a", "c", "b").inOrder();
        assertThat(map.values()).containsExactly("android", "cola", "bbq").inOrder();
    }

    // Tests for removal operations
    @Test
    public void removeLastNodeViaIterator_maintainsCorrectIterationOrder() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        map.put("c", "cola");
        map.put("b", "bbq");
        
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        it.next(); // "a"
        it.next(); // "c"
        it.next(); // "b"
        it.remove();
        
        assertThat(map.keySet()).containsExactly("a", "c").inOrder();
    }

    @Test
    public void clear_removesAllEntries() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        map.put("c", "cola");
        map.put("b", "bbq");
        
        map.clear();
        assertThat(map).isEmpty();
        assertThat(map.keySet()).isEmpty();
    }

    // Tests for null key handling
    @Test
    @SuppressWarnings("ModifiedButNotUsed")
    public void put_nullKey_throwsNullPointerException() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        var e = assertThrows(NullPointerException.class, () -> map.put(null, "android"));
        assertThat(e).hasMessageThat().isEqualTo("key == null");
    }

    @Test
    public void containsKey_nullKey_alwaysReturnsFalse() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        assertThat(map.containsKey(null)).isFalse();
        
        map.put("a", "android");
        assertThat(map.containsKey(null)).isFalse();
    }

    // Tests for non-comparable keys
    @Test
    @SuppressWarnings("ModifiedButNotUsed")
    public void put_nonComparableKey_throwsClassCastException() {
        LinkedTreeMap<Object, String> map = new LinkedTreeMap<>();
        assertThrows(ClassCastException.class, () -> map.put(new Object(), "android"));
    }

    @Test
    public void containsKey_nonComparableKey_returnsFalse() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "android");
        assertThat(map.containsKey(new Object())).isFalse();
    }

    // Tests for null value handling
    @Test
    public void put_nullValue_whenAllowed_succeeds() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", null);
        
        assertThat(map).hasSize(1);
        assertThat(map).containsKey("a");
        assertThat(map).containsValue(null);
        assertThat(map.get("a")).isNull();
    }

    @Test
    public void put_nullValue_whenDisallowed_throwsNullPointerException() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>(false);
        var e = assertThrows(NullPointerException.class, () -> map.put("a", null));
        assertThat(e).hasMessageThat().isEqualTo("value == null");
        
        assertThat(map).isEmpty();
        assertThat(map).doesNotContainKey("a");
    }

    @Test
    public void entrySet_setValueToNull_whenAllowed_succeeds() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "1");
        Entry<String, String> entry = map.entrySet().iterator().next();
        
        entry.setValue(null);
        assertThat(entry.getValue()).isNull();
        assertThat(map.get("a")).isNull();
    }

    @Test
    public void entrySet_setValueToNull_whenDisallowed_throwsNullPointerException() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>(false);
        map.put("a", "1");
        Entry<String, String> entry = map.entrySet().iterator().next();
        
        var e = assertThrows(NullPointerException.class, () -> entry.setValue(null));
        assertThat(e).hasMessageThat().isEqualTo("value == null");
        
        assertThat(entry.getValue()).isEqualTo("1");
        assertThat(map.get("a")).isEqualTo("1");
    }

    // Tests for value handling
    @Test
    public void put_emptyStringValue_succeeds() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        map.put("a", "");
        
        assertThat(map).containsKey("a");
        assertThat(map.get("a")).isEmpty();
    }

    // Tests for put/get operations
    @Test
    public void put_existingKey_overridesValue() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        assertThat(map.put("d", "donut")).isNull();
        assertThat(map.put("e", "eclair")).isNull();
        assertThat(map.put("f", "froyo")).isNull();
        assertThat(map).hasSize(3);
        
        assertThat(map.put("d", "done")).isEqualTo("donut");
        assertThat(map).hasSize(3);
        assertThat(map.get("d")).isEqualTo("done");
    }

    @Test
    public void putAndGet_withRandomKeys_succeeds() {
        Random random = new Random(RANDOM_SEED);
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        String[] keys = new String[RANDOM_KEYS_COUNT];
        
        // Insert random keys
        for (int i = 0; i < keys.length; i++) {
            keys[i] = Integer.toString(random.nextInt(), 36) + "-" + i;
            map.put(keys[i], "" + i);
        }
        
        // Verify all keys are present with correct values
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            assertThat(map).containsKey(key);
            assertThat(map.get(key)).isEqualTo("" + i);
        }
    }

    // Tests for equals and hashCode
    @Test
    public void equalsAndHashCode_consistentWithMapBehavior() {
        LinkedTreeMap<String, Integer> map1 = new LinkedTreeMap<>();
        map1.put("A", 1);
        map1.put("B", 2);
        map1.put("C", 3);
        map1.put("D", 4);
        
        // Different insertion order should still be equal
        LinkedTreeMap<String, Integer> map2 = new LinkedTreeMap<>();
        map2.put("C", 3);
        map2.put("B", 2);
        map2.put("D", 4);
        map2.put("A", 1);
        
        MoreAsserts.assertEqualsAndHashCode(map1, map2);
    }

    // Tests for serialization
    @Test
    public void javaSerialization_deserializedMapEqualsOriginal() 
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        
        Map<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);
        objOut.writeObject(map);
        objOut.close();
        
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        @SuppressWarnings("unchecked")
        Map<String, Integer> deserialized = (Map<String, Integer>) objIn.readObject();
        
        assertThat(deserialized).isEqualTo(Collections.singletonMap("a", 1));
    }
}