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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SerializedCache Test")
class SerializedCacheTest {

  private static final String DEFAULT_CACHE_ID = "default";
  private static final int ITEM_COUNT = 5;

  private SerializedCache cache;

  @BeforeEach
  void setUp() {
    PerpetualCache perpetualCache = new PerpetualCache(DEFAULT_CACHE_ID);
    cache = new SerializedCache(perpetualCache);
  }

  @Test
  @DisplayName("Should correctly store and retrieve serializable objects")
  void shouldStoreAndRetrieveSerializableObject() {
    // Arrange: Put several serializable objects into the cache.
    for (int i = 0; i < ITEM_COUNT; i++) {
      cache.putObject(i, new SerializableObject(i));
    }

    // Act & Assert: Retrieve each object and verify it's equal to the original.
    for (int i = 0; i < ITEM_COUNT; i++) {
      Object retrievedObject = cache.getObject(i);
      assertEquals(new SerializableObject(i), retrievedObject);
    }
  }

  @Test
  @DisplayName("Should correctly store and retrieve null values")
  void shouldStoreAndRetrieveNullValue() {
    // Arrange: Put several null values into the cache.
    for (int i = 0; i < ITEM_COUNT; i++) {
      cache.putObject(i, null);
    }

    // Act & Assert: Retrieve each entry and verify it is null.
    for (int i = 0; i < ITEM_COUNT; i++) {
      Object retrievedObject = cache.getObject(i);
      assertNull(retrievedObject);
    }
  }

  @Test
  @DisplayName("Should throw CacheException when caching a non-serializable object")
  void shouldThrowCacheExceptionWhenPuttingNonSerializableObject() {
    // Arrange
    Object nonSerializableObject = new NonSerializableObject(0);
    int key = 0;

    // Act & Assert
    assertThrows(CacheException.class, () -> cache.putObject(key, nonSerializableObject),
        "Should throw CacheException for non-serializable objects");
  }

  private static class SerializableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int value;

    public SerializableObject(int value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SerializableObject that = (SerializableObject) o;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  private static class NonSerializableObject {
    private final int value;

    public NonSerializableObject(int value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      NonSerializableObject that = (NonSerializableObject) o;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }
}