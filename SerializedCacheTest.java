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
import org.junit.jupiter.api.Test;

/**
 * Tests for SerializedCache to verify serialization and deserialization behavior.
 * SerializedCache is a decorator that serializes objects before storing them in cache
 * and deserializes them when retrieving from cache.
 */
class SerializedCacheTest {

  private static final String CACHE_ID = "test-cache";
  private static final int TEST_OBJECT_COUNT = 5;
  
  private SerializedCache serializedCache;

  @BeforeEach
  void setUp() {
    Cache underlyingCache = new PerpetualCache(CACHE_ID);
    serializedCache = new SerializedCache(underlyingCache);
  }

  @Test
  void shouldSerializeAndDeserializeObjectsCorrectly() {
    // Given: Multiple serializable objects to cache
    storeSerializableObjectsInCache();
    
    // When & Then: Retrieved objects should equal the original objects
    verifyStoredObjectsCanBeRetrievedCorrectly();
  }

  @Test
  void shouldHandleNullValuesCorrectly() {
    // Given: Null values stored in cache
    storeNullValuesInCache();
    
    // When & Then: Retrieved values should be null
    verifyStoredNullValuesAreRetrievedAsNull();
  }

  @Test
  void shouldThrowExceptionWhenCachingNonSerializableObject() {
    // Given: A non-serializable object
    NonSerializableTestObject nonSerializableObject = new NonSerializableTestObject(42);
    
    // When & Then: Attempting to cache it should throw CacheException
    assertThrows(CacheException.class, 
        () -> serializedCache.putObject("key", nonSerializableObject),
        "SerializedCache should throw CacheException when trying to cache non-serializable objects");
  }

  private void storeSerializableObjectsInCache() {
    for (int i = 0; i < TEST_OBJECT_COUNT; i++) {
      SerializableTestObject testObject = new SerializableTestObject(i);
      serializedCache.putObject(i, testObject);
    }
  }

  private void verifyStoredObjectsCanBeRetrievedCorrectly() {
    for (int i = 0; i < TEST_OBJECT_COUNT; i++) {
      SerializableTestObject expectedObject = new SerializableTestObject(i);
      SerializableTestObject actualObject = (SerializableTestObject) serializedCache.getObject(i);
      
      assertEquals(expectedObject, actualObject, 
          "Deserialized object should equal the original object for key: " + i);
    }
  }

  private void storeNullValuesInCache() {
    for (int i = 0; i < TEST_OBJECT_COUNT; i++) {
      serializedCache.putObject(i, null);
    }
  }

  private void verifyStoredNullValuesAreRetrievedAsNull() {
    for (int i = 0; i < TEST_OBJECT_COUNT; i++) {
      Object retrievedValue = serializedCache.getObject(i);
      assertNull(retrievedValue, "Retrieved value should be null for key: " + i);
    }
  }

  /**
   * Test object that implements Serializable for testing successful serialization.
   */
  static class SerializableTestObject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int value;

    public SerializableTestObject(int value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || getClass() != other.getClass()) {
        return false;
      }
      SerializableTestObject that = (SerializableTestObject) other;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public String toString() {
      return "SerializableTestObject{value=" + value + "}";
    }
  }

  /**
   * Test object that does NOT implement Serializable for testing serialization failures.
   */
  static class NonSerializableTestObject {
    private final int value;

    public NonSerializableTestObject(int value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || getClass() != other.getClass()) {
        return false;
      }
      NonSerializableTestObject that = (NonSerializableTestObject) other;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public String toString() {
      return "NonSerializableTestObject{value=" + value + "}";
    }
  }
}