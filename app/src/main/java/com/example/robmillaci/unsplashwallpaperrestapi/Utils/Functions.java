package com.example.robmillaci.unsplashwallpaperrestapi.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.example.robmillaci.unsplashwallpaperrestapi.R;

import java.io.IOException;

public class Functions {

    public static void changeMainFragment(FragmentActivity fragmentActivity, android.support.v4.app.Fragment fragment){
        fragmentActivity
                .getSupportFragmentManager()
                .beginTransaction().replace(R.id.main_container,fragment)
                .commit();
    }

    public static void changeMainFragmentWithBack(FragmentActivity fragmentActivity, android.support.v4.app.Fragment fragment){
        fragmentActivity
                .getSupportFragmentManager()
                .beginTransaction().replace(R.id.main_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    public static boolean setWallpaper(Activity activity, Bitmap bitmap){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Bitmap tempBitmap = scaleCenterCrop(bitmap,height,width);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        try{
            wallpaperManager.setBitmap(tempBitmap);
            return true;
        }catch (IOException e){
            e.getMessage();
        }
        return false;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }
}
