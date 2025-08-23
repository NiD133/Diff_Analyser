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

public class CacheKeyTestTest4 {

    private static <T> T serialize(T object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(object);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return (T) new ObjectInputStream(bais).readObject();
    }

    @Test
    void shouldDemonstrateEmptyAndNullKeysAreEqual() {
        CacheKey key1 = new CacheKey();
        CacheKey key2 = new CacheKey();
        assertEquals(key1, key2);
        assertEquals(key2, key1);
        key1.update(null);
        key2.update(null);
        assertEquals(key1, key2);
        assertEquals(key2, key1);
        key1.update(null);
        key2.update(null);
        assertEquals(key1, key2);
        assertEquals(key2, key1);
    }
}
