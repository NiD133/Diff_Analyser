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
    public void equalObjects_haveSameHashCode() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("1");
        LazilyParsedNumber number2 = new LazilyParsedNumber("1");
        
        assertThat(number1.hashCode()).isEqualTo(number2.hashCode());
    }

    @Test
    public void equals_withEqualValues_returnsTrue() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("1");
        LazilyParsedNumber number2 = new LazilyParsedNumber("1");
        
        assertThat(number1).isEqualTo(number2);
    }

    @Test
    public void equals_withDifferentValues_returnsFalse() {
        LazilyParsedNumber number1 = new LazilyParsedNumber("1");
        LazilyParsedNumber number2 = new LazilyParsedNumber("2");
        
        assertThat(number1).isNotEqualTo(number2);
    }

    @Test
    public void equals_withNull_returnsFalse() {
        LazilyParsedNumber number = new LazilyParsedNumber("1");
        
        assertThat(number.equals(null)).isFalse();
    }

    @Test
    public void equals_withDifferentType_returnsFalse() {
        LazilyParsedNumber number = new LazilyParsedNumber("1");
        BigDecimal other = new BigDecimal("1");
        
        assertThat(number.equals(other)).isFalse();
    }

    @Test
    public void javaSerialization_deserializesToBigDecimal() throws IOException, ClassNotFoundException {
        LazilyParsedNumber number = new LazilyParsedNumber("123");
        byte[] serializedBytes;

        // Serialize the number
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream objOut = new ObjectOutputStream(out)) {
            objOut.writeObject(number);
            serializedBytes = out.toByteArray();
        }

        // Deserialize the number
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serializedBytes))) {
            Number deserialized = (Number) objIn.readObject();
            
            // Verify deserialization produces BigDecimal with correct value
            assertThat(deserialized)
                .isInstanceOf(BigDecimal.class)
                .isEqualTo(new BigDecimal("123"));
        }
    }
}