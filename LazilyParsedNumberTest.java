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

public class LazilyParsedNumberTest {

  /**
   * Tests that two LazilyParsedNumber objects with the same value have the same hash code.
   */
  @Test
  public void testHashCode() {
    LazilyParsedNumber numberOne = new LazilyParsedNumber("1");
    LazilyParsedNumber numberOneDuplicate = new LazilyParsedNumber("1");

    assertThat(numberOneDuplicate.hashCode()).isEqualTo(numberOne.hashCode());
  }

  /**
   * Tests that two LazilyParsedNumber objects with the same value are equal.
   */
  @Test
  public void testEquals() {
    LazilyParsedNumber numberOne = new LazilyParsedNumber("1");
    LazilyParsedNumber numberOneDuplicate = new LazilyParsedNumber("1");

    assertThat(numberOne.equals(numberOneDuplicate)).isTrue();
  }

  /**
   * Tests that a LazilyParsedNumber object can be serialized and deserialized correctly.
   * The deserialized object should be equal to a BigDecimal representation of the original number.
   */
  @Test
  public void testJavaSerialization() throws IOException, ClassNotFoundException {
    // Serialize the LazilyParsedNumber object
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(new LazilyParsedNumber("123"));
    objectOutputStream.close();

    // Deserialize the object
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    Number deserializedNumber = (Number) objectInputStream.readObject();

    // Verify that the deserialized object matches the expected BigDecimal value
    assertThat(deserializedNumber).isEqualTo(new BigDecimal("123"));
  }
}