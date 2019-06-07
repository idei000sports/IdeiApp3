package takuma.idei.ideiapp;



interface MusicPlayerAIDL {
    //プレーヤー関連
    void setSelectSong(String path);
    void setAlbum(in List<String> makedAlbum,String albumArtPath);
    String getAlbumArt();
    //再生関連
    void playOrPauseSong();
    void stopSong();
    void setRepert();
    String playNow();


    //戻るor次
    void skipNext();
    void skipBack();
    List<String> getNowSongData();

    //シーク関連
    void setSeek(int progress);
    int getTotalTime();
    int getCurrentPosition();

    //もろもろ



}
