package takuma.idei.ideiapp;



interface MusicPlayerAIDL {
    //プレーヤー関連
    void setPlayList(in List<String> playList,String albumArtPath, int position);
    //再生関連
    void playOrPauseSong();
    void setRepeat();
    void playSongFromTop(String songPath, String albumArtPath);


    //戻るor次
    void skipNext();
    void skipBack();

    //シーク関連
    void setSeek(int progress);

    //もろもろ



}
