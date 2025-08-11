/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import org.junit.Test;

/**
 * Tests for LazilyParsedNumber.
 *
 * Notes:
 * - Equality and hashCode are based on the wrapped numeric text.
 * - During Java serialization the instance is replaced with a BigDecimal (writeReplace),
 *   so deserialization yields a BigDecimal, not a LazilyParsedNumber.
 */
public class LazilyParsedNumberTest {

  @Test
  public void equals_returnsTrueForSameNumericText() {
    LazilyParsedNumber first = new LazilyParsedNumber("1");
    LazilyParsedNumber second = new LazilyParsedNumber("1");

    assertThat(first).isEqualTo(second);
  }

  @Test
  public void hashCode_matchesForEqualInstances() {
    LazilyParsedNumber first = new LazilyParsedNumber("1");
    LazilyParsedNumber second = new LazilyParsedNumber("1");

    // Equal objects must have the same hash code.
    assertThat(first).isEqualTo(second);
    assertThat(first.hashCode()).isEqualTo(second.hashCode());
  }

  @Test
  public void javaSerialization_replacesWithBigDecimal() throws IOException, ClassNotFoundException {
    // Given a LazilyParsedNumber
    LazilyParsedNumber original = new LazilyParsedNumber("123");

    // When serialized and then deserialized
    Object deserialized = serializeThenDeserialize(original);

    // Then we get a BigDecimal with the same numeric value
    assertThat(deserialized).isInstanceOf(BigDecimal.class);
    assertThat(deserialized).isEqualTo(new BigDecimal("123"));
  }

  private static Object serializeThenDeserialize(Object obj)
      throws IOException, ClassNotFoundException {
    byte[] bytes;
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
         ObjectOutputStream objOut = new ObjectOutputStream(out)) {
      objOut.writeObject(obj);
      bytes = out.toByteArray();
    }

    try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
      return objIn.readObject();
    }
  }
}