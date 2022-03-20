package junit.services;

import static junit.model.Race.HOBBIT;
import static junit.model.Race.MAIA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junit.model.Race;
import junit.model.TolkienCharacter;

class DataServiceTest {

    DataService dataService;

	@BeforeEach
	void setUp() {
		dataService = new DataService();
	}
	
    @Test
    void ensureThatInitializationOfTolkienCharactersWorks() {
        TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);

        assertEquals(33, frodo.getAge());
        assertEquals("Frodo", frodo.getName());
        assertNotEquals("Frodon", frodo.getName());
        
    }

    @Test
    void ensureThatEqualsWorksForCharacters() {
    	TolkienCharacter jake = new TolkienCharacter("Jake", 43, HOBBIT);
    	TolkienCharacter sameJake = jake;
    	TolkienCharacter jakeClone = new TolkienCharacter("Jake", 12, HOBBIT);

        assertEquals(jake, sameJake);
        assertNotEquals(jake, jakeClone);
    }

    @Test
    void ensureFellowShipCharacterAccessByNameReturnsNullForUnknownCharacter() {
    	TolkienCharacter tolkienCharacter = dataService.getFellowshipCharacter("Lars");
    	assertNull(tolkienCharacter);
    }	

    @Test
    void ensureFellowShipCharacterAccessByNameWorksGivenCorrectNameIsGiven() {
    	TolkienCharacter tolkienCharacter = dataService.getFellowshipCharacter("Frodo");    	
    	assertNotNull(tolkienCharacter);
    }

    @Test
    void ensureThatFrodoAndGandalfArePartOfTheFellowsip() {

        List<TolkienCharacter> fellowship = dataService.getFellowship();

    	TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
    	TolkienCharacter gandalf = new TolkienCharacter("Gandalf", 2020, MAIA);
    	
    	assertTrue(fellowship.contains(frodo));
    	assertTrue(fellowship.contains(gandalf));

    }

    @Test
    void ensureThatOneRingBearerIsPartOfTheFellowship() {

        List<TolkienCharacter> fellowship = dataService.getFellowship();
        Collection<TolkienCharacter> ringBearers = dataService.getRingBearers().values();
       
        assertTrue(fellowship.stream().filter(ringBearers::contains).findAny().isPresent());
        
    }

    @Test
    void ensureOrdering() {
        List<TolkienCharacter> fellowship = dataService.getFellowship();

        Iterator<TolkienCharacter> it = fellowship.iterator();
        assertTrue(it.next().getName().equals("Frodo"));
        assertTrue(it.next().getName().equals("Sam"));
        assertTrue(it.next().getName().equals("Merry"));
        assertTrue(it.next().getName().equals("Pippin"));
        assertTrue(it.next().getName().equals("Gandalf"));
        assertTrue(it.next().getName().equals("Legolas"));
        assertTrue(it.next().getName().equals("Gimli"));
        assertTrue(it.next().getName().equals("Aragorn"));
        assertTrue(it.next().getName().equals("Boromir"));
        
    }

    @Test
    void ensureAge() {
        List<TolkienCharacter> fellowship = dataService.getFellowship();

		assertTrue(fellowship.stream()
				.filter(c -> c.getRace() == Race.HOBBIT || c.getRace() == Race.MAN)
				.allMatch(c -> c.getAge() < 100));
		
		assertTrue(fellowship.stream()
				.filter(c -> c.getRace() == Race.ELF || c.getRace() == Race.DWARF || c.getRace() == Race.MAIA)
				.allMatch(c -> c.getAge() > 100));

    }

	@Test
	void ensureThatFellowshipStayASmallGroup() {

		List<TolkienCharacter> fellowship = dataService.getFellowship();

		assertThrows(IndexOutOfBoundsException.class, () -> fellowship.get(20));
	}

}