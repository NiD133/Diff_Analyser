package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;

public class LinkedTreeMapTestTest16 {

    @Test
    public void serialization_deserializesAsEquivalentLinkedHashMap()
            throws IOException, ClassNotFoundException {
        // Arrange: Create a LinkedTreeMap with a specific insertion order.
        LinkedTreeMap<String, Integer> originalMap = new LinkedTreeMap<>();
        originalMap.put("c", 3);
        originalMap.put("a", 1);
        originalMap.put("b", 2);

        // Act: Serialize the map and then deserialize it back.
        byte[] serializedBytes;
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
            objOut.writeObject(originalMap);
            serializedBytes = byteOut.toByteArray();
        }

        Map<String, Integer> deserializedMap;
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(serializedBytes);
             ObjectInputStream objIn = new ObjectInputStream(byteIn)) {
            @SuppressWarnings("unchecked")
            Map<String, Integer> readMap = (Map<String, Integer>) objIn.readObject();
            deserializedMap = readMap;
        }

        // Assert
        // The custom writeReplace() method is designed to serialize a LinkedTreeMap
        // as a standard LinkedHashMap. This avoids a dependency on Gson for deserialization.
        assertThat(deserializedMap).isInstanceOf(LinkedHashMap.class);

        // Verify that the content and insertion order are preserved.
        assertThat(deserializedMap).containsExactly("c", 3, "a", 1, "b", 2).inOrder();
    }
}