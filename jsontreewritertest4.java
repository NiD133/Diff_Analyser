package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link JsonTreeWriter} focusing on nested object creation.
 */
public final class JsonTreeWriterTest {

  /**
   * Verifies that the writer correctly constructs a JsonElement representing a nested JSON object.
   */
  @Test
  public void writeNestedObject_buildsCorrectJsonStructure() throws IOException {
    // Arrange: Define the expected JSON structure. This makes the test's goal
    // immediately clear and decouples it from the writer's implementation.
    JsonObject expectedJson = new JsonObject();
    JsonObject objectA = new JsonObject();
    objectA.add("B", new JsonObject());
    expectedJson.add("A", objectA);
    expectedJson.add("C", new JsonObject());

    // Act: Use the JsonTreeWriter to build the JSON structure.
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("A");
    writer.beginObject();
    writer.name("B");
    writer.beginObject();
    writer.endObject(); // End B
    writer.endObject(); // End A
    writer.name("C");
    writer.beginObject();
    writer.endObject(); // End C
    writer.endObject(); // End root

    JsonElement actualJson = writer.get();

    // Assert: Verify the generated JsonElement matches the expected structure.
    // Comparing JsonElement objects is more robust than string comparison because
    // it is not sensitive to key order or formatting.
    assertThat(actualJson).isEqualTo(expectedJson);
  }
}