package org.apache.ibatis.submitted.blocking_cache;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Test for the BlockingCache decorator to ensure it does not cause deadlocks.
 *
 * @see org.apache.ibatis.cache.decorators.BlockingCache
 */
class BlockingCacheTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    static void setup() throws Exception {
        // Create a shared SqlSessionFactory for all tests in this class.
        try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }

        // Populate the in-memory database once.
        BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
                "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
    }

    /**
     * Verifies that a cache miss following a cache clear does not cause a deadlock.
     *
     * This scenario was known to cause deadlocks in earlier versions (see MyBatis issue #593).
     * The test simulates this by:
     * 1. Flushing the cache using a mapper method configured with `flushCache=true`.
     * 2. Immediately querying the cache, causing a cache miss that engages the locking mechanism.
     *
     * The test passes if the sequence completes without a timeout or exception.
     */
    @Test
    void shouldNotDeadlockOnCacheMissAfterClear() {
        // The assertion is that the following block executes without throwing an exception,
        // which would indicate a deadlock or timeout in the BlockingCache.
        assertDoesNotThrow(() -> {
            try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
                // Arrange: Get a mapper instance.
                PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);

                // Act:
                // 1. The delete() operation is configured with flushCache="true" in PersonMapper.xml,
                //    effectively clearing the cache for this namespace.
                mapper.delete(-1);

                // 2. This query will now result in a cache miss. The BlockingCache will acquire a
                //    lock to prevent other threads from hitting the database for the same key.
                //    The test ensures this process doesn't lead to a self-deadlock.
                mapper.findAll();

                sqlSession.commit();
            }
        });
    }
}