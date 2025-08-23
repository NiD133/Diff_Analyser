package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the factory methods of {@link CopticChronology}.
 * This test focuses on instance creation with different time zones.
 */
@DisplayName("CopticChronology Factory Methods")
class CopticChronologyFactoryTest {

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDefaultZone;

    @BeforeEach
    void setUp() {
        // The test for getInstance(null) depends on the default time zone.
        // We save the original and set a known one for test predictability.
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    @AfterEach
    void tearDown() {
        // Restore the original default time zone to avoid side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    @DisplayName("getInstance(zone) should return a chronology with the specified zone")
    void getInstance_withSpecificZone_returnsChronologyWithThatZone() {
        // Act
        CopticChronology chronology = CopticChronology.getInstance(TOKYO);

        // Assert
        assertThat(chronology.getZone()).isEqualTo(TOKYO);
    }

    @Test
    @DisplayName("getInstance(null) should return a chronology with the default zone")
    void getInstance_withNullZone_returnsChronologyWithDefaultZone() {
        // Arrange: The default zone is set to LONDON in the setUp method.

        // Act
        CopticChronology chronology = CopticChronology.getInstance(null);

        // Assert
        assertThat(chronology.getZone()).isEqualTo(LONDON);
    }

    @Test
    @DisplayName("getInstance() should return an instance of CopticChronology")
    void getInstance_returnsInstanceOfCopticChronology() {
        // Act
        CopticChronology chronology = CopticChronology.getInstance(TOKYO);

        // Assert
        assertThat(chronology).isInstanceOf(CopticChronology.class);
    }
}