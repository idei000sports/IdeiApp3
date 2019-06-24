package takuma.idei.ideiapp.Artist;

public class ArtistListItem {
    private static final long serialVersionUID = 1L;


    private String mSong_title = null;
    private int mRanking = 0;
    private int mPlaycount = 0;

    public ArtistListItem() {

    }

    public ArtistListItem(String song_title, int ranking, int playcount) {
        mSong_title = song_title;
        mRanking = ranking;
        mPlaycount = playcount;
    }

    public String getSong_title() {
        return mSong_title;
    }

    public void setSong_title(String song_title) {
        mSong_title = song_title;
    }

    public int getRanking() {
        return mRanking;
    }

    public void setRanking(int ranking) {
        mRanking = ranking;
    }

    public int getPlaycount() {
        return mPlaycount;
    }

    public void setPlaycount(int playcount) {
        mPlaycount = playcount;
    }
}