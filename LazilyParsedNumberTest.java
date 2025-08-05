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

  @Test
  public void testHashCode_sameStringValue_producesEqualHashCodes() {
    // Given: Two LazilyParsedNumber instances with the same string value
    String numberValue = "1";
    LazilyParsedNumber firstNumber = new LazilyParsedNumber(numberValue);
    LazilyParsedNumber secondNumber = new LazilyParsedNumber(numberValue);

    // When: Computing hash codes for both instances
    int firstHashCode = firstNumber.hashCode();
    int secondHashCode = secondNumber.hashCode();

    // Then: Hash codes should be equal (required by equals/hashCode contract)
    assertThat(secondHashCode).isEqualTo(firstHashCode);
  }

  @Test
  public void testEquals_sameStringValue_returnsTrue() {
    // Given: Two LazilyParsedNumber instances with the same string value
    String numberValue = "1";
    LazilyParsedNumber firstNumber = new LazilyParsedNumber(numberValue);
    LazilyParsedNumber secondNumber = new LazilyParsedNumber(numberValue);

    // When: Comparing the instances for equality
    boolean areEqual = firstNumber.equals(secondNumber);

    // Then: They should be considered equal
    assertThat(areEqual).isTrue();
  }

  @Test
  public void testJavaSerialization_deserializesAsBigDecimal() throws IOException, ClassNotFoundException {
    // Given: A LazilyParsedNumber with a numeric string value
    String originalValue = "123";
    LazilyParsedNumber originalNumber = new LazilyParsedNumber(originalValue);

    // When: Serializing and then deserializing the number
    Number deserializedNumber = serializeAndDeserialize(originalNumber);

    // Then: The deserialized object should be a BigDecimal with the same value
    // (LazilyParsedNumber converts to BigDecimal during serialization to avoid 
    // requiring Gson on the deserializing side)
    BigDecimal expectedBigDecimal = new BigDecimal(originalValue);
    assertThat(deserializedNumber).isEqualTo(expectedBigDecimal);
  }

  /**
   * Helper method to perform Java serialization round-trip for testing.
   * 
   * @param object the object to serialize and deserialize
   * @return the deserialized object
   */
  private Number serializeAndDeserialize(LazilyParsedNumber object) throws IOException, ClassNotFoundException {
    // Serialize the object to a byte array
    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    try (ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput)) {
      objectOutput.writeObject(object);
    }

    // Deserialize the object from the byte array
    ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
    try (ObjectInputStream objectInput = new ObjectInputStream(byteInput)) {
      return (Number) objectInput.readObject();
    }
  }
}