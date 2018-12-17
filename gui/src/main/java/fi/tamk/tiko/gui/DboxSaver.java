package fi.tamk.tiko.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

/**
 * Class for accessing the Dropbox and saving the file in there.
 */
public class DboxSaver {
    private static final String ACCESS_TOKEN = "<--ACCESS TOKEN GOES HERE-->";

    /**
     * Saves the shopping list file to Dropbox.
     * @param filename Name of the .json file to save to Dropbox.
     * @return True if saving was successful, false if it failed.
     * @throws DbxException Exception for incorrect access token.
     */
    public static boolean saveToDbox(String filename) throws DbxException {
        boolean uploadSuccessful = false;
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        File f = new File(filename);
        
        try (InputStream in = new FileInputStream(filename)) {
            if (f.exists()) {
                FileMetadata metadata = client.files().uploadBuilder("/" + filename)
            .uploadAndFinish(in);
            uploadSuccessful = true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return uploadSuccessful;
    }
}