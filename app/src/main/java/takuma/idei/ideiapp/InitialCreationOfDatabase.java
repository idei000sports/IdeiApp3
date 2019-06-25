package takuma.idei.ideiapp;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;

public class InitialCreationOfDatabase {
    MediaMetadataRetriever mediaMetadataRetriever;


    public InitialCreationOfDatabase(Context context) {
        String storagePath = Environment.getExternalStorageDirectory().getPath();
        String musicFolderPath = storagePath + "/Music";
        Make(context, musicFolderPath);
    }

    public void Make (Context context,String musicFolderPath) {
        File[] fileList = new File(musicFolderPath).listFiles();
        if(fileList != null){
            for (File file : fileList) {
                if (file.isFile() && file.getName().endsWith(".mp3")) {
                    makeSongData(context, musicFolderPath, musicFolderPath + "/" + file.getName());
                }

                if (file.isDirectory()) {
                    String directory_name = file.getName();
                    itsDirectory(context, musicFolderPath + "/" + directory_name);
                }

            }
        }
    }

    public void itsDirectory(Context context, String new_directory) {
        Make(context, new_directory);

    }

    private void makeSongData(Context context, String album_path, String songPath) {
        String track_number = "";
        String song_title = "";
        String artist_name = "";
        String album_title = "";
        String album_art_path = album_path + "/folder.jpg";
        mediaMetadataRetriever = new MediaMetadataRetriever();

        try {
            mediaMetadataRetriever.setDataSource(songPath);
            song_title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            artist_name = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            album_title = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            track_number = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);


        } catch (Exception e) {
            e.printStackTrace();
        }
        AllSongsDataBase asdb = new AllSongsDataBase();
        asdb.putSong(context, artist_name,song_title,album_title,songPath,track_number);
        asdb.putAlbum(context,artist_name, album_title, album_path, album_art_path);

    }

    private static String escape(String value) {
        value = value.replaceAll("'", "\'");
        return value;
    }
}
