
package proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CachedSongService implements SongService {

    private final SongService realService;

    private final Map<Integer, Optional<Song>> idCache =
            new HashMap<>();

    private final Map<String, List<Song>> titleCache =
            new HashMap<>();

    private final Map<String, List<Song>> albumCache =
            new HashMap<>();

    public CachedSongService(SongService realService) {
        this.realService = Objects.requireNonNull(realService);
    }

    @Override
    public Song searchById(Integer songID) {

        Optional<Song> result = idCache.computeIfAbsent(
                songID,
                id -> Optional.ofNullable(realService.searchById(id))
        );

        return result.orElse(null);
    }

    @Override
    public List<Song> searchByTitle(String title) {

        String key = title.trim().toLowerCase(Locale.ROOT);

        return titleCache.computeIfAbsent(
                key,
                unused -> List.copyOf(realService.searchByTitle(title))
        );
    }

    @Override
    public List<Song> searchByAlbum(String album) {

        String key = album.trim().toLowerCase(Locale.ROOT);

        return albumCache.computeIfAbsent(
                key,
                unused -> List.copyOf(realService.searchByAlbum(album))
        );
    }
}
