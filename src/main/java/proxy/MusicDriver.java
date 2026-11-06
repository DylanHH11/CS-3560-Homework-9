
package proxy;

import java.util.List;

public class MusicDriver {

    public static void main(String[] args) {

        List<Song> songs = List.of(
                new Song(1, "Song1", "Artist1", "Album1", 123),
                new Song(2, "Song2", "Artist2", "Album1", 234),
                new Song(3, "Song3", "Artist1", "Album2", 321),
                new Song(4, "Song4", "Artist3", "Album2", 213),
                new Song(5, "Song5", "Artist2", "Album3", 345),
                new Song(6, "Song6", "Artist4", "Album3", 432),
                new Song(7, "Song7", "Artist3", "Album4", 231),
                new Song(8, "Song8", "Artist4", "Album4", 312)
        );

        SongService realService = new RealSongService(songs, 1000);
        SongService proxy = new CachedSongService(realService);

        System.out.println("Music Streaming Metadata Search");
        System.out.println("Total songs: " + songs.size());

        System.out.println("\nFirst search by ID:");
        runSearch(() -> System.out.println(proxy.searchById(1)));

        System.out.println("\nRepeated search by ID:");
        runSearch(() -> System.out.println(proxy.searchById(1)));

        System.out.println("\nFirst search by title:");
        runSearch(() -> System.out.println(proxy.searchByTitle("Song3")));

        System.out.println("\nRepeated search by title:");
        runSearch(() -> System.out.println(proxy.searchByTitle("Song3")));

        System.out.println("\nFirst search by album:");
        runSearch(() -> System.out.println(proxy.searchByAlbum("Album1")));

        System.out.println("\nRepeated search by album:");
        runSearch(() -> System.out.println(proxy.searchByAlbum("Album1")));

        System.out.println("\nSearching another song:");
        runSearch(() -> System.out.println(proxy.searchById(8)));
    }

    private static void runSearch(Runnable search) {
        long start = System.nanoTime();

        search.run();

        long elapsed = System.nanoTime() - start;

        System.out.printf("Elapsed time: %.3f seconds%n",
                elapsed / 1_000_000_000.0);
    }
}
