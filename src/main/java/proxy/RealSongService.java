
package proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RealSongService implements SongService {

    private final List<Song> songs;
    private final long delayMillis;

    public RealSongService(List<Song> songs) {
        this(songs, 1000);
    }

    public RealSongService(List<Song> songs, long delayMillis) {
        if (delayMillis < 0) {
            throw new IllegalArgumentException("Delay cannot be negative");
        }

        this.songs = List.copyOf(songs);
        this.delayMillis = delayMillis;
    }

    @Override
    public Song searchById(Integer songID) {
        simulateDelay();

        for (Song song : songs) {
            if (Objects.equals(song.getId(), songID)) {
                return song;
            }
        }

        return null;
    }

    @Override
    public List<Song> searchByTitle(String title) {
        simulateDelay();

        List<Song> results = new ArrayList<>();
        String query = title.trim().toLowerCase(Locale.ROOT);

        for (Song song : songs) {
            if (song.getTitle().toLowerCase(Locale.ROOT).contains(query)) {
                results.add(song);
            }
        }

        return results;
    }

    @Override
    public List<Song> searchByAlbum(String album) {
        simulateDelay();

        List<Song> results = new ArrayList<>();
        String query = album.trim().toLowerCase(Locale.ROOT);

        for (Song song : songs) {
            if (song.getAlbum().toLowerCase(Locale.ROOT).contains(query)) {
                results.add(song);
            }
        }

        return results;
    }

    private void simulateDelay() {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Song search interrupted", e);
        }
    }
}
