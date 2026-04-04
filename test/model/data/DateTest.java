package model.data;

import model.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTest {

    @Test
    @DisplayName("Should successfully create a valid date")
    void of_validDate_returnsDateObject() {
        assertDoesNotThrow(() -> {
            Date d = Date.of(1, 1, 2026);
            assertNotNull(d);
            assertEquals("1/1/2026", d.toString());
        });
    }

    @Test
    @DisplayName("Should throw InvalidDateException for month > 12")
    void of_invalidMonth_throwsException() {
        assertThrows(InvalidDateException.class, () -> {
            Date.of(1, 13, 2026);
        });
    }

    @Test
    @DisplayName("Should throw InvalidDateException for day 31 in a 30-day month")
    void of_invalidDayForMonth_throwsException() {
        // April has only 30 days
        assertThrows(InvalidDateException.class, () -> {
            Date.of(31, 4, 2026);
        });
    }

    @Test
    @DisplayName("Should handle leap years correctly (Feb 29 is valid in 2024)")
    void of_leapYear_isValid() {
        assertDoesNotThrow(() -> {
            Date leapDay = Date.of(29, 2, 2024);
            assertEquals("29/2/2024", leapDay.toString());
        });
    }

    @Test
    @DisplayName("Should reject Feb 29 in non-leap years")
    void of_nonLeapYear_isInvalid() {
        assertThrows(InvalidDateException.class, () -> {
            Date.of(29, 2, 2025);
        });
    }
}