package takuma.idei.ideiapp;

import android.graphics.Bitmap;

public class MyLibraryListItem {
    private Bitmap mThumbnail = null;
    private String mTitle = null;

    /**
     * 空のコンストラクタ
     */
    public MyLibraryListItem() {};

    /**
     * コンストラクタ
     * @param thumbnail サムネイル画像
     * @param title タイトル
     */
    public MyLibraryListItem(Bitmap thumbnail, String title) {
        mThumbnail = thumbnail;
        mTitle = title;
    }

    /**
     * サムネイル画像を設定
     * @param thumbnail サムネイル画像
     */
    public void setThumbnail(Bitmap thumbnail) {
        mThumbnail = thumbnail;
    }

    /**
     * タイトルを設定
     * @param title タイトル
     */
    public void setmTitle(String title) {
        mTitle = title;
    }

    /**
     * サムネイル画像を取得
     * @return サムネイル画像
     */
    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    /**
     * タイトルを取得
     * @return タイトル
     */
    public String getTitle() {
        return mTitle;
    }
}