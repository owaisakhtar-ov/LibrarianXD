package Genre;

public class GenreModel {
    private String genre_UniqueID;
    private String genreName;
    private int genre_ID;

    public GenreModel(String genre_UniqueID, String genreName, int genre_ID) {
        this.genre_UniqueID = genre_UniqueID;
        this.genreName = genreName;
        this.genre_ID = genre_ID;
    }

    public int getGenre_ID() {
        return genre_ID;
    }

    public void setGenre_ID(int genre_ID) {
        this.genre_ID = genre_ID;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getGenre_UniqueID() {
        return genre_UniqueID;
    }

    public void setGenre_UniqueID(String genre_UniqueID) {
        this.genre_UniqueID = genre_UniqueID;
    }
}

