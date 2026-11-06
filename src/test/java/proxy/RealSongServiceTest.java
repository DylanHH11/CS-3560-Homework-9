
package proxy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RealSongServiceTest {

    private final Song song1 =
            new Song(1, "Blue Horizon", "Artist A", "Open Skies", 180);

    private final Song song2 =
            new Song(2, "Blue Nights", "Artist B", "Night Drive", 210);

    private final Song song3 =
            new Song(3, "Sunrise", "Artist C", "Open Skies", 200);

    private RealSongService createService() {
        return new RealSongService(List.of(song1, song2, song3), 0);
    }

    @Test
    public void testSongProperties() {
        assertAll(
                () -> assertEquals(1, song1.getId()),
                () -> assertEquals("Blue Horizon", song1.getTitle()),
                () -> assertEquals("Artist A", song1.getArtist()),
                () -> assertEquals("Open Skies", song1.getAlbum()),
                () -> assertEquals(180, song1.getDuration())
        );
    }

    @Test
    public void testSearchById() {
        assertSame(song2, createService().searchById(2));
    }

    @Test
    public void testMissingId() {
        assertNull(createService().searchById(999));
    }

    @Test
    public void testSearchByTitle() {
        List<Song> results = createService().searchByTitle("BLUE");

        assertEquals(2, results.size());
        assertTrue(results.contains(song1));
        assertTrue(results.contains(song2));
    }

    @Test
    public void testSearchByAlbum() {
        List<Song> results = createService().searchByAlbum("Open Skies");

        assertEquals(2, results.size());
        assertTrue(results.contains(song1));
        assertTrue(results.contains(song3));
    }

    @Test
    public void testServiceCopiesSongList() {
        List<Song> original = new ArrayList<>();
        original.add(song1);

        RealSongService service = new RealSongService(original, 0);
        original.clear();

        assertSame(song1, service.searchById(1));
    }
}
