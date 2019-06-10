package takuma.idei.ideiapp;



interface MusicPlayerAIDL {
    //プレーヤー関連
    void setPlayList(in List<String> playList,String albumArtPath);
    //再生関連
    void playOrPauseSong();
    void setRepeat();


    //戻るor次
    void skipNext();
    void skipBack();

    //シーク関連
    void setSeek(int progress);

    //もろもろ



}
