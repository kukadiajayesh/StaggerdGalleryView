package com.jadygallery.staggered.component;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PhoneMediaControl {

    Handler handler = new Handler();
    public loadAlbumPhoto loadalbumphoto;

    public void loadGalleryPhotosAlbums(final Context mContext, final int guid) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<AlbumEntry> albumsSorted = new ArrayList<AlbumEntry>();
                HashMap<Integer, AlbumEntry> albums = new HashMap<Integer, AlbumEntry>();
                AlbumEntry allPhotosAlbum = null;
                String cameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + "Camera/";
                Integer cameraAlbumId = null;

                Cursor cursor = null;
                try {
                    cursor = MediaStore.Images.Media.query(
                            mContext.getContentResolver(),
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "", null,
                            MediaStore.Images.Media.DATE_TAKEN + " DESC");
                    if (cursor != null) {

                        /*cursor.moveToFirst();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            Log.e("", "column: " + cursor.getColumnName(i) + ": value:" + cursor.getString(i));
                        }

                        if (cursor.getCount() > 0) {
                            return;
                        }*/

                        int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                        int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                        int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
                        int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);

                        while (cursor.moveToNext()) {
                            int imageId = cursor.getInt(imageIdColumn);
                            int bucketId = cursor.getInt(bucketIdColumn);
                            String bucketName = cursor.getString(bucketNameColumn);
                            String path = cursor.getString(dataColumn);
                            long dateTaken = cursor.getLong(dateColumn);
                            int orientation = cursor.getInt(orientationColumn);

                            if (path == null || path.length() == 0) {
                                continue;
                            }

                            PhotoEntry photoEntry = new PhotoEntry(bucketId, imageId, dateTaken, path, orientation);
                            File file = new File(path);

                            if (allPhotosAlbum == null) {
                                allPhotosAlbum = new AlbumEntry(0, "All Photos", file.getParent(), photoEntry);
                                albumsSorted.add(0, allPhotosAlbum);
                            }
                            if (allPhotosAlbum != null) {
                                allPhotosAlbum.addPhoto(photoEntry);
                            }

                            AlbumEntry albumEntry = albums.get(bucketId);
                            if (albumEntry == null) {
                                albumEntry = new AlbumEntry(bucketId, bucketName, file.getParent(), photoEntry);
                                albums.put(bucketId, albumEntry);
                                if (cameraAlbumId == null && cameraFolder != null && path != null && path.startsWith(cameraFolder)) {
                                    albumsSorted.add(0, albumEntry);
                                    cameraAlbumId = bucketId;
                                } else {
                                    albumsSorted.add(albumEntry);
                                }
                            }

                            albumEntry.addPhoto(photoEntry);
                        }
                    }
                } catch (Exception e) {
                    Log.e("tmessages", e.toString());
                } finally {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e) {
                            Log.e("tmessages", e.toString());
                        }
                    }
                }
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadalbumphoto != null) {
                            loadalbumphoto.loadPhoto(albumsSorted);
                        }
                    }
                });
            }
        }).start();
    }

    public void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            handler.post(runnable);
        } else {
            handler.postDelayed(runnable, delay);
        }
    }


    private final String[] projectionPhotos = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.ORIENTATION
    };

    public static class AlbumEntry {
        public int bucketId;
        public String bucketName, bucketPath;
        public PhotoEntry coverPhoto;
        public ArrayList<PhotoEntry> photos = new ArrayList<PhotoEntry>();

        public AlbumEntry(int bucketId, String bucketName, String bucketPath, PhotoEntry coverPhoto) {
            this.bucketId = bucketId;
            this.bucketName = bucketName;
            this.bucketPath = bucketPath;
            this.coverPhoto = coverPhoto;
        }

        public void addPhoto(PhotoEntry photoEntry) {
            photos.add(photoEntry);
        }
    }

    public static class PhotoEntry implements Parcelable {

        public int bucketId;
        public int imageId;
        public long dateTaken;
        public String path;
        public int orientation, height = 0;

        public PhotoEntry(int bucketId, int imageId, long dateTaken, String path, int orientation) {
            this.bucketId = bucketId;
            this.imageId = imageId;
            this.dateTaken = dateTaken;
            this.path = path;
            this.orientation = orientation;
        }

        protected PhotoEntry(Parcel in) {
            bucketId = in.readInt();
            imageId = in.readInt();
            dateTaken = in.readLong();
            path = in.readString();
            orientation = in.readInt();
            height = in.readInt();
        }

        public static final Creator<PhotoEntry> CREATOR = new Creator<PhotoEntry>() {
            @Override
            public PhotoEntry createFromParcel(Parcel in) {
                return new PhotoEntry(in);
            }

            @Override
            public PhotoEntry[] newArray(int size) {
                return new PhotoEntry[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(bucketId);
            parcel.writeInt(imageId);
            parcel.writeLong(dateTaken);
            parcel.writeString(path);
            parcel.writeInt(orientation);
            parcel.writeInt(height);
        }
    }


    public loadAlbumPhoto getLoadalbumphoto() {
        return loadalbumphoto;
    }

    public void setLoadalbumphoto(loadAlbumPhoto loadalbumphoto) {
        this.loadalbumphoto = loadalbumphoto;
    }

    public interface loadAlbumPhoto {
        void loadPhoto(ArrayList<AlbumEntry> albumsSorted);
    }

}
