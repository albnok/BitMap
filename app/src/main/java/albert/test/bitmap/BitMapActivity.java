package albert.test.bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class BitMapActivity extends Activity {
    ImageView imageViewBitMap;
    Bitmap bitmap;
    RelativeLayout relativeLayoutBitMap;
    float imageX, imageY;
    int[] viewCoords = new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_map);
        imageViewBitMap = (ImageView) findViewById(R.id.imageViewBitMap);
        imageViewBitMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onTouched(motionEvent);
                return false;
            }
        });
        relativeLayoutBitMap = (RelativeLayout) findViewById(R.id.relativelayoutBitMap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        imageViewBitMap.getLocationOnScreen(viewCoords);
        imageViewBitMap.setImageBitmap(bitmap);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        imageY = bitmap.getHeight() / imageViewBitMap.getHeight();
        imageX = bitmap.getWidth() / imageViewBitMap.getWidth();
    }

    void onTouched(MotionEvent motionEvent) {
        float x = motionEvent.getX() - viewCoords[0];
        float y = motionEvent.getY() - viewCoords[1];
        int color = bitmap.getPixel((int) (x * imageX) , (int) (y * imageY));
        relativeLayoutBitMap.setBackgroundColor(color);
//        setTitle(x + ", " + y + " of iv: " + imageX + "x" + imageY);
//        Log.d("BitMapAct.onTouched 3", x + ", " + y + " is " + Color.red(color) + "-" + Color.green(color) + "-" + Color.blue(color));
    }

}
