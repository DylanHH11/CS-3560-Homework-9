
package proxy;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CachedSongServiceTest {

    private static class CountingSongService implements SongService {

        int idCalls = 0;
        int titleCalls = 0;
        int albumCalls = 0;

        final Song song = new Song(
                1, "Midnight Echo", "Artist A", "City Lights", 200
        );

        @Override
        public Song searchById(Integer songID) {
            idCalls++;

            return Objects.equals(songID, 1) ? song : null;
        }

        @Override
        public List<Song> searchByTitle(String title) {
            titleCalls++;

            String query = title.trim().toLowerCase(Locale.ROOT);

            if (song.getTitle().toLowerCase(Locale.ROOT).contains(query)) {
                return List.of(song);
            }

            return List.of();
        }

        @Override
        public List<Song> searchByAlbum(String album) {
            albumCalls++;

            String query = album.trim().toLowerCase(Locale.ROOT);

            if (song.getAlbum().toLowerCase(Locale.ROOT).contains(query)) {
                return List.of(song);
            }

            return List.of();
        }
    }

    @Test
    public void testFirstIdSearchCallsServer() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        assertNotNull(proxy.searchById(1));
        assertEquals(1, real.idCalls);
    }

    @Test
    public void testRepeatedIdSearchUsesCache() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        Song first = proxy.searchById(1);
        Song second = proxy.searchById(1);

        assertSame(first, second);
        assertEquals(1, real.idCalls);
    }

    @Test
    public void testRepeatedTitleSearchUsesCache() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        proxy.searchByTitle("Midnight");
        proxy.searchByTitle("Midnight");

        assertEquals(1, real.titleCalls);
    }

    @Test
    public void testRepeatedAlbumSearchUsesCache() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        proxy.searchByAlbum("City Lights");
        proxy.searchByAlbum("City Lights");

        assertEquals(1, real.albumCalls);
    }

    @Test
    public void testNormalizedTitleCache() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        proxy.searchByTitle("Midnight");
        proxy.searchByTitle(" midnight ");

        assertEquals(1, real.titleCalls);
    }

    @Test
    public void testDifferentIdsCallServer() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        proxy.searchById(1);
        proxy.searchById(2);

        assertEquals(2, real.idCalls);
    }

    @Test
    public void testMissingIdIsCached() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        assertNull(proxy.searchById(999));
        assertNull(proxy.searchById(999));

        assertEquals(1, real.idCalls);
    }

    @Test
    public void testEmptyResultIsCached() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        assertTrue(proxy.searchByAlbum("Unknown").isEmpty());
        assertTrue(proxy.searchByAlbum("Unknown").isEmpty());

        assertEquals(1, real.albumCalls);
    }

    @Test
    public void testCachedListsCannotBeModified() {
        CountingSongService real = new CountingSongService();
        SongService proxy = new CachedSongService(real);

        List<Song> results = proxy.searchByTitle("Midnight");

        assertThrows(
                UnsupportedOperationException.class,
                () -> results.clear()
        );

        assertEquals(1, real.titleCalls);
    }
}
