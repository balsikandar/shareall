package camera.touchtalent.com.shareutils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

/**
 * Created by bali on 13/04/17.
 */

public class MimeTypeDetector {

    /**
     * @param url
     * @return Return the MIME type for the given url.
     */
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * @param context
     * @param uri
     * @return Return the MIME type for the given uri.
     */
    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            if (fileExtension == null || fileExtension.isEmpty()){     //fallback for these URIs : content://media/external/images/media/94
                ContentResolver contentResolver = context.getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                return contentResolver.getType(uri);
            }
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

}
