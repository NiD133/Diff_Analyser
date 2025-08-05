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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import org.junit.Test;

/**
 * Tests for {@link LazilyParsedNumber}.
 */
public final class LazilyParsedNumberTest {

  @Test
  public void testEqualsContract() {
    LazilyParsedNumber num123 = new LazilyParsedNumber("123");
    LazilyParsedNumber anotherNum123 = new LazilyParsedNumber("123");
    LazilyParsedNumber num456 = new LazilyParsedNumber("456");

    // A LazilyParsedNumber is equal to another instance with the same string value.
    assertThat(num123).isEqualTo(anotherNum123);

    // It is not equal to an instance with a different value.
    assertThat(num123).isNotEqualTo(num456);

    // It is not equal to null.
    assertThat(num123).isNotEqualTo(null);

    // It is not equal to an object of a different type, even with the same numeric value.
    assertThat(num123).isNotEqualTo(new BigDecimal("123"));
  }

  @Test
  public void testHashCode_isConsistentWithEquals() {
    LazilyParsedNumber num123 = new LazilyParsedNumber("123");
    LazilyParsedNumber anotherNum123 = new LazilyParsedNumber("123");

    // The hashCode of two equal objects must be the same.
    assertThat(num123.hashCode()).isEqualTo(anotherNum123.hashCode());
  }

  @Test
  public void testSerialization_deserializesAsBigDecimal() throws Exception {
    LazilyParsedNumber original = new LazilyParsedNumber("123");

    // Serialize the object to a byte array.
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    try (ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
      objOut.writeObject(original);
    }

    // Deserialize the object from the byte array.
    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
    Number deserialized;
    try (ObjectInputStream objIn = new ObjectInputStream(byteIn)) {
      deserialized = (Number) objIn.readObject();
    }

    // The custom writeReplace() method serializes the object as a BigDecimal.
    // Therefore, the deserialized object should be a BigDecimal, not a LazilyParsedNumber.
    assertThat(deserialized).isInstanceOf(BigDecimal.class);
    assertThat(deserialized).isEqualTo(new BigDecimal("123"));
  }
}