class Song {
  String title;
  String artist;
  int year;
  double length;
  
  Song(String title, String artist, int year, double length) {
    this.title = title;
    this.artist = artist;
    this.year = year;
    this.length = length;
  }
}

class ExamplesSong {
  ExamplesSong() {}
  
  Song levitating = new Song("Levitating", "Dua Lipa", 2020, 3.5);
  Song rhiannon = new Song("Rhiannon", "Fleetwood Mac", 1975, 4.25);
  Song gold = new Song("Gold", "Kiiara", 2015, 3.47);
  
}