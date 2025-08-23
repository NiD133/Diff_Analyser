package org.apache.ibatis.submitted.blocking_cache;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * This test suite verifies the lock-release behavior of the BlockingCache,
 * specifically ensuring that locks are correctly released when a transaction is rolled back.
 */
@DisplayName("BlockingCache: Lock Release on Rollback")
class BlockingCacheRollbackTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeAll
    static void setUp() throws Exception {
        // Create a SqlSessionFactory from the configuration file.
        try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }

        // Populate the in-memory database.
        BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
                "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
    }

    @Test
    @DisplayName("Should release cache lock when a transaction is rolled back")
    void shouldReleaseLockOnRollback() {
        // Arrange: Acquire a lock in the cache.
        // 1. Open a session and perform a write operation to ensure the cache is cleared.
        // 2. Execute a query ('findAll') which results in a cache miss, causing BlockingCache to acquire a lock.
        // 3. The session is kept open to hold the lock.
        try (SqlSession session1 = sqlSessionFactory.openSession()) {
            PersonMapper mapper1 = session1.getMapper(PersonMapper.class);
            mapper1.delete(-1); // This write operation clears the cache for this mapper.
            mapper1.findAll();  // This query misses the cache, so a lock is placed on the cache key.

            // Act: Roll back the transaction. This action should trigger the release of the cache lock.
            session1.rollback();
        }

        // Assert: Verify that the lock has been released.
        // A new session should be able to execute the same query without being blocked.
        // We use assertTimeoutPreemptively to confirm that the operation completes quickly
        // and doesn't wait for the cache's block timeout.
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            try (SqlSession session2 = sqlSessionFactory.openSession()) {
                PersonMapper mapper2 = session2.getMapper(PersonMapper.class);
                mapper2.findAll(); // This would time out if the lock from session1 was not released.
            }
        }, "The second session timed out, indicating the lock was not released on rollback.");
    }
}