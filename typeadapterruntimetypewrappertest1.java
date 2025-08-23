package com.google.gson.functional;

import static com.google.common.truth.Truth.assertThat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.Test;

public class TypeAdapterRuntimeTypeWrapperTestTest1 {

    private static class Base {
    }

    private static class Subclass extends Base {

        @SuppressWarnings("unused")
        String f = "test";
    }

    private static class Container {

        @SuppressWarnings("unused")
        Base b = new Subclass();
    }

    private static class Deserializer implements JsonDeserializer<Base> {

        @Override
        public Base deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            throw new AssertionError("not needed for this test");
        }
    }

    private static class CyclicBase {

        @SuppressWarnings("unused")
        CyclicBase f;
    }

    private static class CyclicSub extends CyclicBase {

        @SuppressWarnings("unused")
        int i;

        CyclicSub(int i) {
            this.i = i;
        }
    }

    /**
     * When custom {@link JsonSerializer} is registered for Base should prefer that over reflective
     * adapter for Subclass for serialization.
     */
    @Test
    public void testJsonSerializer() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Base.class, (JsonSerializer<Base>) (src, typeOfSrc, context) -> new JsonPrimitive("serializer")).create();
        String json = gson.toJson(new Container());
        assertThat(json).isEqualTo("{\"b\":\"serializer\"}");
    }
}
