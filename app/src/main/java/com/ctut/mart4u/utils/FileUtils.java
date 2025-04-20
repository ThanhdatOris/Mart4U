package com.ctut.mart4u.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {
    public static String saveImageToStorage(Context context, Uri imageUri) {
        try {
            // Tạo tên file duy nhất dựa trên thời gian
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "product_" + timeStamp + ".jpg";

            // Thư mục lưu ảnh
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            // Tạo file mới
            File imageFile = new File(storageDir, imageFileName);

            // Copy dữ liệu từ Uri vào file
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            OutputStream outputStream = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            // Trả về đường dẫn tuyệt đối của file
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}