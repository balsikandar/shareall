package camera.touchtalent.com.shareutils;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by bali on 13/04/17.
 */

public class ShareUtil {

    private String mimeType;
    private String category;
    private String shareText;
    private Uri uri;
    private ComponentName componentName;
    private String packageName;
    private int addflags;
    private int setFlags;
    private String subject;

    public String getMimeType() {
        return mimeType;
    }

    public String getCategory() {
        return category;
    }

    public String getShareText() {
        return shareText;
    }

    public Uri getUri() {
        return uri;
    }

    public ComponentName getComponentName() {
        return componentName;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getAddFlags() {
        return addflags;
    }

    public int getSetFlags() {
        return setFlags;
    }

    public String getSubject() {
        return subject;
    }

    private ShareUtil(Builder builder) {
        this.mimeType = builder.mimeType;
        this.category = builder.category;
        this.shareText = builder.shareText;
        this.subject = builder.subject;
        this.uri = builder.uri;
        this.componentName = builder.componentName;
        this.packageName = builder.packageName;
        this.addflags = builder.addflag;
        this.setFlags = builder.setFlag;

    }

    public static Builder builder(Context context){
        return new Builder(context);
    }

    public static class Builder {
        private String mimeType;
        private String category;
        private String shareText;
        private String subject;
        private Uri uri;
        private ComponentName componentName;
        private String packageName;
        private int addflag;
        private int setFlag;
        public Intent intent;
        private Context context;

        public Builder from(Context context) {
            this.context = context;
            return this;
        }

        public Builder(Context context) {
            this.context = context;
            setIntent();
        }

        private Builder setIntent() {
            if (intent == null) {
                intent = new Intent(Intent.ACTION_SEND);
            }
            return this;
        }

        private Builder setIntent(Intent intent) {
            this.intent = intent;
            checkNotNull(intent);
            return this;
        }

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            checkNotNull(mimeType);
            intent.setType(mimeType);
            return this;
        }

        public Builder addCategory(String category) {
            this.category = category;
            checkNotNull(category);
            intent.addCategory(category);
            return this;
        }


        public Builder setSubject(String subject) {
            this.subject = subject;
            checkNotNull(subject);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            return this;
        }

        public Builder setShareText(String shareText) {
            this.shareText = shareText;
            checkNotNull(shareText);
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            return this;
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            getMimeType(uri);
            checkNotNull(uri);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            return this;
        }

        private void getMimeType(Uri uri) {
            String mimeType = MimeTypeDetector.getMimeType(context, uri);
            intent.setType(mimeType);
        }

        public Builder setComponentName(ComponentName componentName) {
            checkNotNull(componentName);
            this.componentName = componentName;
            intent.setComponent(componentName);
            return this;
        }

        public Builder setComponentName(String packageName) {
            this.packageName = packageName;
            checkNotNull(packageName);
            ComponentName mComponentName = Utils.getComponentName(context, intent, packageName);
            if (mComponentName != null) {
                intent.setComponent(mComponentName);
            }
            return this;
        }

        public Builder addFlags(int addflag) {
            this.addflag = addflag;
            intent.addFlags(addflag);
            return this;
        }

        public Builder setFlags(int setFlag) {
            this.setFlag = setFlag;
            intent.setFlags(setFlag);
            return this;
        }

        public ShareUtil build() {
            return new ShareUtil(this);
        }

        public void share() {
            checkNotNull(intent);
            if (intent != null) {
                context.startActivity(intent);
            }

        }

        public Intent getIntent() {
            return intent;
        }

        public void checkNotNull(Object obj) {
            if (obj == null) {
                throw new IllegalArgumentException("ShareUtil : Argument may not be null");
            }

            if (obj instanceof String && ((String) obj).isEmpty()) {
                throw new IllegalArgumentException("ShareUtil : Argument length may not be zero");
            }

            if (obj instanceof Intent){
                if (((Intent) obj).getType() == null){
                    throw new IllegalArgumentException("ShareUtil : mimetype was not set");
                }

                if (((Intent) obj).getExtras() == null){
                    throw new IllegalArgumentException("ShareUtil : extras was not set");
                }
            }
        }

    }
}
