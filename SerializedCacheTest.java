/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.Objects;

import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

class SerializedCacheTest {

    @Test
    void serializedObjects_ShouldRemainEqual_AfterCaching() {
        // Arrange: Initialize cache with serialization support
        SerializedCache cache = new SerializedCache(new PerpetualCache("default"));
        SerializableObject originalObject = new SerializableObject(42);

        // Act: Store and retrieve the object
        cache.putObject(1, originalObject);
        Object retrievedObject = cache.getObject(1);

        // Assert: Deserialized object equals the original
        assertEquals(originalObject, retrievedObject);
    }

    @Test
    void nullValues_ShouldBeStoredAndRetrieved_Successfully() {
        // Arrange: Initialize cache
        SerializedCache cache = new SerializedCache(new PerpetualCache("default"));

        // Act & Assert: Store null and verify retrieval
        cache.putObject(1, null);
        assertNull(cache.getObject(1));
    }

    @Test
    void nonSerializableObject_ShouldThrowException_WhenCached() {
        // Arrange: Initialize cache
        SerializedCache cache = new SerializedCache(new PerpetualCache("default"));
        NonSerializableObject invalidObject = new NonSerializableObject(99);

        // Act & Assert: Verify exception on cache attempt
        assertThrows(CacheException.class, () -> cache.putObject(1, invalidObject));
    }

    // Helper class: Serializable object for testing
    static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private final int value;

        SerializableObject(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SerializableObject that = (SerializableObject) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    // Helper class: Non-serializable object for exception testing
    static class NonSerializableObject {
        private final int value;

        NonSerializableObject(int value) {
            this.value = value;
        }
    }
}