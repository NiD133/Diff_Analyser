package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests the serialization and deserialization of the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Serializes and then deserializes the given {@link IOCase} instance.
     *
     * @param original The enum instance to serialize and deserialize.
     * @return The deserialized instance.
     * @throws Exception if an I/O or class-loading error occurs.
     */
    private IOCase serializeAndDeserialize(final IOCase original) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }

        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (IOCase) ois.readObject();
        }
    }

    /**
     * Verifies that deserializing an {@link IOCase} constant returns the canonical
     * singleton instance. This ensures that the custom {@code readResolve()} method
     * correctly preserves the identity of the enum constants.
     *
     * @param ioCase The IOCase constant provided by the EnumSource.
     */
    @ParameterizedTest
    @EnumSource(IOCase.class)
    void serializationPreservesSingletonInstance(final IOCase ioCase) throws Exception {
        // Act
        final IOCase deserialized = serializeAndDeserialize(ioCase);

        // Assert
        assertSame(ioCase, deserialized, "Deserialization should return the same singleton instance");
    }
}