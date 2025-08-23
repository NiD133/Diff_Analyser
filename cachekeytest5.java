package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class CacheKeyTestTest5 {

    private static <T> T serialize(T object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return (T) new ObjectInputStream(bais).readObject();
    }

    @Test
    void shouldTestCacheKeysWithBinaryArrays() {
        byte[] array1 = { 1 };
        byte[] array2 = { 1 };
        CacheKey key1 = new CacheKey(new Object[] { array1 });
        CacheKey key2 = new CacheKey(new Object[] { array2 });
        assertEquals(key1, key2);
    }
}
