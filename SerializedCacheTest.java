/*
 * Copyright 2009-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 */
package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.Objects;

import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for SerializedCache.
 *
 * This cache serializes values on write and deserializes them on read.
 * These tests focus on:
 * - round-trip of Serializable values,
 * - preservation of null values,
 * - rejection of non-Serializable values.
 */
class SerializedCacheTest {

  private static final String CACHE_ID = "default";
  private static final int ENTRY_COUNT = 5;

  private static SerializedCache newCache() {
    // PerpetualCache is a simple in-memory cache used as a delegate.
    return new SerializedCache(new PerpetualCache(CACHE_ID));
  }

  @Test
  @DisplayName("Serializable values should round-trip through serialization")
  void serializableValuesAreEqualAfterRoundTrip() {
    SerializedCache cache = newCache();

    // Given: cache populated with serializable values
    for (int key = 0; key < ENTRY_COUNT; key++) {
      cache.putObject(key, new SerializableValue(key));
    }

    // Then: retrieved values are equal to the originals
    for (int key = 0; key < ENTRY_COUNT; key++) {
      assertEquals(new SerializableValue(key), cache.getObject(key), "Round-trip failed for key " + key);
    }
  }

  @Test
  @DisplayName("Null values are supported and preserved")
  void nullValuesAreSerializable() {
    SerializedCache cache = newCache();

    // Given: cache populated with null values
    for (int key = 0; key < ENTRY_COUNT; key++) {
      cache.putObject(key, null);
    }

    // Then: retrieved values are null
    for (int key = 0; key < ENTRY_COUNT; key++) {
      assertNull(cache.getObject(key), "Expected null for key " + key);
    }
  }

  @Test
  @DisplayName("Putting a non-Serializable value should throw CacheException")
  void nonSerializableValueCausesException() {
    SerializedCache cache = newCache();

    assertThrows(
        CacheException.class,
        () -> cache.putObject(0, new NonSerializableValue(0)),
        "Values must implement java.io.Serializable"
    );
  }

  // ---- Test fixtures ----

  /**
   * Simple value object that is Serializable for round-trip tests.
   */
  static class SerializableValue implements Serializable {
    private static final long serialVersionUID = 1L;

    final int x;

    SerializableValue(int x) {
      this.x = x;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SerializableValue that = (SerializableValue) o;
      return x == that.x;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x);
    }
  }

  /**
   * Similar to SerializableValue but intentionally not Serializable,
   * to verify that the cache rejects such values.
   */
  static class NonSerializableValue {
    final int x;

    NonSerializableValue(int x) {
      this.x = x;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      NonSerializableValue that = (NonSerializableValue) o;
      return x == that.x;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x);
    }
  }
}