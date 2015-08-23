package ru.aim.anotheryetbashclient;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import ru.aim.anotheryetbashclient.helper.L;

public class ShareDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String TAG = "ShareDialog";

    Bitmap bitmap;
    String publicId;
    String text;

    public static ShareDialog newInstance(Bitmap bitmap, String publicId, String text) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", bitmap);
        bundle.putString("id", publicId);
        bundle.putString("text", text);
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.setArguments(bundle);
        return shareDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bitmap = getArguments().getParcelable("bitmap");
        publicId = getArguments().getString("id");
        text = getArguments().getString("text");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.share_desc);
        String[] items = TextUtils.isEmpty(publicId) ?
                getResources().getStringArray(R.array.share_types_short) : getResources().getStringArray(R.array.share_types);
        builder.setItems(items, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, publicId);
        switch (which) {
            case 0:
                if (!PermissionsUtil.isWriteExternalAllowed(getContext())) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{PermissionsUtil.EXTERNAL_STORAGE},
                            PermissionsUtil.EXTERNAL_STORAGE_REQUEST);
                    AbstractActivity.addPermAction(new Runnable() {
                        @Override
                        public void run() {
                            shareImage(sharingIntent);
                        }
                    });
                } else {
                    shareImage(sharingIntent);
                }
                break;
            case 1:
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_desc)));
                break;
            case 2:
                if (TextUtils.isEmpty(publicId)) {
                    copyToClipboard();
                } else {
                    shareLink(sharingIntent);
                }
                break;
            case 3:
                copyToClipboard();
                break;
        }
    }

    static void shareImage(Activity activity, Intent sharingIntent) {
        try {
            sharingIntent.setType("image/jpeg");
            String fileName = getCleanId(publicId) + ".jpg";
            String url = insertImage(activity.getContentResolver(), bitmap, fileName, publicId);
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                String realPath = getRealPathFromUri(getActivity(), Uri.parse(url));
                File imageFile = new File(realPath);
                intent.setData(Uri.fromFile(imageFile));
                activity.sendBroadcast(intent);
            } else {
                File file = getFile(fileName);
                if (file.exists()) {
                    url = Uri.fromFile(file).toString();
                } else {
                    Toast.makeText(activity, R.string.error_share_quote, Toast.LENGTH_SHORT).show();
                }
            }
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, publicId);
            activity.startActivity(Intent.createChooser(sharingIntent, getActivity().getString(R.string.share_desc)));
            Toast.makeText(activity, R.string.quote_will_be_in_gallery, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            L.e(TAG, "Error while trying share quote", e);
            Toast.makeText(activity, R.string.error_share_quote, Toast.LENGTH_SHORT).show();
        }
    }

    private void shareLink(Intent sharingIntent) {
        sharingIntent.setType("text/plain");
        String url = getString(R.string.bash_url) + "/" + getCleanId(publicId);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_desc)));
    }

    private void copyToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(publicId, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    static File getFile(String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + File.separator + getString(R.string.app_name));
        return new File(myDir, fileName);
    }

    static String saveBitmap(Bitmap bitmap, String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + File.separator + getString(R.string.app_name));
        myDir.mkdirs();
        File file = new File(myDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    static String insertImage(ContentResolver cr, Bitmap source,
                       String title, String description) {

        String path = saveBitmap(source, title);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_TAKEN, new Date().getTime());
        values.put(MediaStore.Images.Media.ORIENTATION, 0);
        values.put(MediaStore.Images.Media.DATA, path);
        values.put(MediaStore.Images.Media.SIZE, byteSizeOf(source));

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e) {
            Log.e(TAG, "Failed to insert image", e);
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    static String getCleanId(String id) {
        if (TextUtils.isEmpty(id)) {
            Date cur = new Date();
            return "" + cur.getTime();
        } else return id.replace("#", "");
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
