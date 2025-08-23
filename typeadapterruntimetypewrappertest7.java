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

public class TypeAdapterRuntimeTypeWrapperTestTest7 {

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
     * Tests behavior when the type of a field refers to a type whose adapter is currently in the
     * process of being created. For these cases {@link Gson} uses a future adapter for the type. That
     * adapter later uses the actual adapter as delegate.
     */
    @Test
    public void testGsonFutureAdapter() {
        CyclicBase b = new CyclicBase();
        b.f = new CyclicSub(2);
        String json = new Gson().toJson(b);
        assertThat(json).isEqualTo("{\"f\":{\"i\":2}}");
    }
}
