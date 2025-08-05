/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gson.internal.bind;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.common.MoreAsserts;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

@SuppressWarnings("resource")
public class JsonTreeReaderTest {

  @Test
  public void skipValue_shouldHandleEmptyJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.skipValue();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_shouldHandleFilledJsonObject() throws IOException {
    JsonObject jsonObject = createFilledJsonObject();
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.skipValue();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_shouldSkipNameInJsonObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("a", "value");
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    reader.skipValue();
    assertThat(reader.peek()).isEqualTo(JsonToken.STRING);
    assertThat(reader.getPath()).isEqualTo("$.<skipped>");
    assertThat(reader.nextString()).isEqualTo("value");
  }

  @Test
  public void skipValue_shouldNotAffectAfterEndOfDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
    reader.skipValue();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_shouldHandleArrayEnd() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.beginArray();
    reader.skipValue();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void skipValue_shouldHandleObjectEnd() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.skipValue();
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    assertThat(reader.getPath()).isEqualTo("$");
  }

  @Test
  public void hasNext_shouldReturnFalseAtEndOfDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.beginObject();
    reader.endObject();
    assertThat(reader.hasNext()).isFalse();
  }

  @Test
  public void customJsonElementSubclass_shouldThrowException() throws IOException {
    class CustomSubclass extends JsonElement {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    }

    JsonArray array = new JsonArray();
    array.add(new CustomSubclass());

    JsonTreeReader reader = new JsonTreeReader(array);
    reader.beginArray();

    MalformedJsonException exception = assertThrows(MalformedJsonException.class, reader::peek);
    assertThat(exception)
        .hasMessageThat()
        .isEqualTo("Custom JsonElement subclass " + CustomSubclass.class.getName() + " is not supported");
  }

  @Test
  public void nestingLimit_shouldBeIgnored() throws IOException {
    int limit = 10;
    JsonArray json = createNestedJsonArray(limit);

    JsonTreeReader reader = new JsonTreeReader(json);
    reader.setNestingLimit(limit);
    assertThat(reader.getNestingLimit()).isEqualTo(limit);

    for (int i = 0; i < limit; i++) {
      reader.beginArray();
    }
    // Does not throw exception; limit is ignored
    reader.beginArray();

    reader.endArray();
    for (int i = 0; i < limit; i++) {
      reader.endArray();
    }
    assertThat(reader.peek()).isEqualTo(JsonToken.END_DOCUMENT);
    reader.close();
  }

  @Test
  public void shouldOverrideRelevantMethods() {
    List<String> ignoredMethods = Arrays.asList(
        "setLenient(boolean)",
        "isLenient()",
        "setStrictness(com.google.gson.Strictness)",
        "getStrictness()",
        "setNestingLimit(int)",
        "getNestingLimit()"
    );
    MoreAsserts.assertOverridesMethods(JsonReader.class, JsonTreeReader.class, ignoredMethods);
  }

  private JsonObject createFilledJsonObject() {
    JsonObject jsonObject = new JsonObject();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add('c');
    jsonArray.add("text");
    jsonObject.add("a", jsonArray);
    jsonObject.addProperty("b", true);
    jsonObject.addProperty("i", 1);
    jsonObject.add("n", JsonNull.INSTANCE);
    JsonObject jsonObject2 = new JsonObject();
    jsonObject2.addProperty("n", 2L);
    jsonObject.add("o", jsonObject2);
    jsonObject.addProperty("s", "text");
    return jsonObject;
  }

  private JsonArray createNestedJsonArray(int depth) {
    JsonArray jsonArray = new JsonArray();
    JsonArray current = jsonArray;
    for (int i = 0; i < depth; i++) {
      JsonArray nested = new JsonArray();
      current.add(nested);
      current = nested;
    }
    return jsonArray;
  }
}