package de.saring.util.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests of class IdDateObjectList.
 *
 * @author Stefan Saring
 */
public class IdDateObjectListTest {

    /**
     * The instance to test.
     */
    private IdDateObjectList<DateNameObject> list;

    @BeforeEach
    public void setUp() {
        list = new IdDateObjectList<>();
        list.set(new DateNameObject(1, LocalDateTime.of(2009, 02, 05, 21, 30, 0), "one"));
        list.set(new DateNameObject(2, LocalDateTime.of(2008, 12, 11, 20, 30, 0), "two"));
        list.set(new DateNameObject(3, LocalDateTime.of(2009, 02, 07, 11, 40, 0), "three"));

        checkDateOrder();
    }

    /**
     * Test of set(): must add a new object to list at position 2.
     */
    @Test
    public void testSetAdd() {
        list.set(new DateNameObject(4, LocalDateTime.of(2008, 12, 25, 21, 30, 0), "four"));
        assertEquals(list.size(), 4);
        assertEquals("four", list.getAt(1).getName());
        checkDateOrder();
    }

    /**
     * Test of set(): must replace the object in list at position 3 with the new
     * one with the allready used ID.
     */
    @Test
    public void testSetReplaceNew() {
        assertEquals("three", list.getAt(2).getName());
        list.set(new DateNameObject(3, LocalDateTime.of(2009, 02, 07, 11, 40, 0), "three-new"));
        assertEquals(list.size(), 3);
        assertEquals("three-new", list.getAt(2).getName());
        checkDateOrder();
    }

    /**
     * Test of set(): must store the modified object in list from position 3 at
     * the proper list position for the modified date.
     */
    @Test
    public void testSetReplaceModified() {
        DateNameObject no3 = list.getAt(2);
        assertEquals("three", no3.getName());
        no3.setDateTime(LocalDateTime.of(2008, 07, 12, 12, 45, 0));
        no3.setName("three-new");

        list.set(no3);
        assertEquals(list.size(), 3);
        assertEquals("three-new", list.getAt(0).getName());
        checkDateOrder();
    }

    /**
     * Test of method clearAndAddAll(). The previous list content must be removed, the
     * list must contain only the new entries, sorted by date.
     */
    @Test
    public void clearAndAddAll() {

        ArrayList<DateNameObject> tempEntries = new ArrayList<>();
        tempEntries.add(new DateNameObject(5, LocalDateTime.of(2009, 02, 05, 21, 30, 0), "five"));
        tempEntries.add(new DateNameObject(6, LocalDateTime.of(2009, 02, 01, 21, 30, 0), "six"));
        list.clearAndAddAll(tempEntries);

        assertEquals(2, list.size());
        assertEquals("six", list.getAt(0).getName());
        assertEquals("five", list.getAt(1).getName());
    }

    /**
     * Test of getEntriesInDateRange(): must find all entries, the date range
     * includes the earliest and latest entry.
     */
    @Test
    public void testGetEntriesInDateRangeFindAll() {
        List<DateNameObject> lFound = list.getEntriesInDateRange(
                LocalDate.of(2008, 12, 11), LocalDate.of(2009, 02, 07));

        assertEquals(3, lFound.size());
        assertEquals("two", lFound.get(0).getName());
        assertEquals("one", lFound.get(1).getName());
        assertEquals("three", lFound.get(2).getName());
    }

    /**
     * Test of getEntriesInDateRange(): must find only one entries, the date
     * range does not include the earliest and latest entry.
     */
    @Test
    public void testGetEntriesInDateRangeFindOne() {
        List<DateNameObject> lFound = list.getEntriesInDateRange(
                LocalDate.of(2008, 12, 12), LocalDate.of(2009, 2, 6));

        assertEquals(1, lFound.size());
        assertEquals("one", lFound.get(0).getName());
    }

    /**
     * Test of getEntriesInDateRange(): must fail when the begin date ist after end date.
     */
    @Test
    public void testGetEntriesInDateRangeInvalidRange() {
        assertThrows(IllegalArgumentException.class, () ->
            list.getEntriesInDateRange(
                    LocalDate.of(2009, 12, 11),
                    LocalDate.of(2009, 11, 11)));
    }

    /**
     * This helper method makes sure that all IdDateObjects in the list are in
     * correct chronological order (the date of the previous exercise is never
     * bigger then the date of the current exercise).
     */
    private void checkDateOrder() {
        LocalDateTime previousDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

        for (IdDateObject temp : list) {
            assertTrue(previousDate.isBefore(temp.getDateTime()));
            previousDate = temp.getDateTime();
        }
    }

    /**
     * Subclass of abstract class IdDateObject for testing.
     */
    static class DateNameObject extends IdDateObject {

        private String name;

        public DateNameObject(int id, LocalDateTime dateTime, String name) {
            super(id);
            setDateTime(dateTime);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
