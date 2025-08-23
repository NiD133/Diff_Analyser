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
 * Test for the Java serialization behavior of {@link LazilyParsedNumber}.
 */
public class LazilyParsedNumberTest {

    /**
     * The {@code writeReplace()} method in {@code LazilyParsedNumber} is designed to
     * serialize the object as a {@code BigDecimal}. This test verifies that after
     * a serialization-deserialization round trip, the resulting object is indeed a
     * {@code BigDecimal} with the correct value.
     */
    @Test
    public void javaSerialization_writesAsBigDecimal() throws IOException, ClassNotFoundException {
        // Arrange
        LazilyParsedNumber originalNumber = new LazilyParsedNumber("123");
        BigDecimal expectedValue = new BigDecimal("123");

        // Act
        Number deserializedNumber = serializeAndDeserialize(originalNumber);

        // Assert
        assertThat(deserializedNumber).isInstanceOf(BigDecimal.class);
        assertThat(deserializedNumber).isEqualTo(expectedValue);
    }

    /**
     * Serializes an object to a byte array and then deserializes it back.
     *
     * @return The deserialized object.
     */
    private static <T> T serializeAndDeserialize(T object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            @SuppressWarnings("unchecked")
            T deserialized = (T) ois.readObject();
            return deserialized;
        }
    }
}