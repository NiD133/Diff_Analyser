package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the serialization and deserialization behavior of the {@link CacheKey} class.
 */
@DisplayName("CacheKey Serialization")
class CacheKeySerializationTest {

  /**
   * Verifies that a CacheKey object remains equal to its original state after
   * being serialized and deserialized.
   * <p>
   * This ensures that the state of the CacheKey is correctly preserved across
   * serialization, which is crucial for distributed caching scenarios.
   */
  @Test
  void shouldPreserveStateWhenSerializedAndDeserialized() throws Exception {
    // Arrange: Create a CacheKey with some state.
    CacheKey originalKey = new CacheKey();
    originalKey.update("a serializable object");
    originalKey.update(12345);

    // Act: Serialize and then deserialize the original key.
    CacheKey deserializedKey = serializeAndDeserialize(originalKey);

    // Assert: The deserialized key should be a new instance but logically equal to the original.
    assertNotSame(originalKey, deserializedKey, "Serialization should produce a new object instance.");
    assertEquals(originalKey, deserializedKey, "Deserialized key should be equal to the original.");
    assertEquals(originalKey.hashCode(), deserializedKey.hashCode(), "Hashcodes of original and deserialized keys should be equal.");
  }

  /**
   * A utility method to perform a serialization-deserialization round trip on an object.
   *
   * @param <T>    the type of the object
   * @param object the object to serialize and deserialize
   * @return a new object instance created by deserialization
   * @throws IOException            if an I/O error occurs
   * @throws ClassNotFoundException if the class of the serialized object cannot be found
   */
  private static <T> T serializeAndDeserialize(T object) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(object);
    }

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    try (ObjectInputStream ois = new ObjectInputStream(bais)) {
      @SuppressWarnings("unchecked")
      T deserializedObject = (T) ois.readObject();
      return deserializedObject;
    }
  }
}