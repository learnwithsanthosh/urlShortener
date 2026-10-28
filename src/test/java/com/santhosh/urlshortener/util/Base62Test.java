package com.santhosh.urlshortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Base62Test {

    @Test
    void shouldEncodeExpectedValues() {
    assertEquals("0", Base62.encode(0));
    assertEquals("z", Base62.encode(61));
    assertEquals("10", Base62.encode(62));
    assertEquals("100", Base62.encode(3844));
}

    @Test
    void shouldDecodeExpectedValues() {
        assertEquals(0L, Base62.decode("0"));
        assertEquals(61L, Base62.decode("z"));
        assertEquals(62L, Base62.decode("10"));
        assertEquals(3844L, Base62.decode("100"));
    }
    @Test
    void shouldRoundTripMultipleValues() {
        long[] values = {
                0L,
                1L,
                61L,
                62L,
                3844L,
                1_000_000L,
                Long.MAX_VALUE
        };

        for (long value : values) {
            assertEquals(
                    value,
                    Base62.decode(Base62.encode(value))
            );
        }
    }

    @Test
    void shouldRejectNegativeIds() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Base62.encode(-1)
        );
    }

    @Test
    void shouldRejectInvalidCharacters() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Base62.decode("abc@")
        );
    }
}
