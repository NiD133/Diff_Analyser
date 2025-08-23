package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.common.TestTypes.BagOfPrimitives;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.Strictness;
import java.io.StringReader;
import org.junit.Test;

public class JsonParserTestTest12 {

  /**
   * Tests that {@link JsonParser#parseReader(JsonReader)} can parse a stream
   * containing multiple top-level JSON objects when the reader is in lenient mode.
   * Strict JSON format only allows a single top-level value.
   */
  @Test
  public void parseReader_multipleJsonObjectsInStream_inLenientMode() {
    // Arrange
    BagOfPrimitives expected1 = new BagOfPrimitives(1, 1, true, "one");
    BagOfPrimitives expected2 = new BagOfPrimitives(2, 2, false, "two");

    // A stream containing two consecutive JSON objects. This is not valid in strict mode.
    String jsonStream =
        "{\"longValue\":1,\"intValue\":1,\"booleanValue\":true,\"stringValue\":\"one\"}"
      + "{\"longValue\":2,\"intValue\":2,\"booleanValue\":false,\"stringValue\":\"two\"}";

    JsonReader jsonReader = new JsonReader(new StringReader(jsonStream));
    jsonReader.setStrictness(Strictness.LENIENT);

    Gson gson = new Gson();

    // Act
    JsonElement element1 = JsonParser.parseReader(jsonReader);
    JsonElement element2 = JsonParser.parseReader(jsonReader);

    // Assert
    BagOfPrimitives actual1 = gson.fromJson(element1, BagOfPrimitives.class);
    BagOfPrimitives actual2 = gson.fromJson(element2, BagOfPrimitives.class);

    assertThat(actual1).isEqualTo(expected1);
    assertThat(actual2).isEqualTo(expected2);
  }
}