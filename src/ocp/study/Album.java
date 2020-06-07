package ocp.study;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Album {

  public String name;
  public List<Track> tracks;

  public Album(String name) {
    this.name = name;
    tracks = new ArrayList<>();
  }

  public Album(String name, String trackName, int rating) {
    this(name);
    tracks.add(new Track(trackName, rating));
  }

  public void addTrack(Track track) {
    tracks.add(track);
  }

  public Stream<Track> getTracks() {
    return tracks.stream();
  }

  public String toString() {
    return name + "->{" + tracks + "}";
  }

  public static class Track {

    public String name;
    public int rating;

    public Track(String name, int rating) {
      this.name = name;
      this.rating = rating;
    }

    public String toString() {
      return name + ":" + rating;
    }

    public int getRating() {
      return rating;
    }
  }
}
