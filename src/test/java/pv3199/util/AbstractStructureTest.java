package pv3199.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class AbstractStructureTest {
    @Test
    public void test() {
        List<Integer> list = asList(6, 2, 3, 1, 8, 6, 2, 0);
        AbstractStructure<Integer> as = new AbstractStructure<>();
        AbstractStructure<Integer> as2 = new AbstractStructure<>(list);

        assertEquals(0, as.size());
        assertEquals(list.size(), as2.size());

        list.forEach(as::add);
        assertEquals(list.size(), as.size());

        assertEquals(list.get(3), as.get(3));
        assertEquals(list.get(3), as2.get(3));

        assertEquals(as, as2);

        as.set(2, -2);

        assertNotEquals(as, as2);

        as.swap(0, 2);
        assertEquals(-2, (int) as.get(0));
        assertEquals(6, (int) as.get(2));

        assertEquals(2, as.indexOf(6));
        assertEquals(7, as.indexOf(0));
        assertEquals(0, as.indexOf(-2));
        assertTrue(as2.indexOf(-2) < 0);

        as.remove(0);
        assertEquals(list.size() - 1, as.size());
        assertTrue(as.indexOf(-2) < 0);

        as.remove((Integer) 0);
        assertEquals(list.size() - 2, as.size());
        assertTrue(as.indexOf(0) < 0);

        assertEquals(2, as.split(0, 2).size());
        assertNotSame(as, as.clone());
    }
}
