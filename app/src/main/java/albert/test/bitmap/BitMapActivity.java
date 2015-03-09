package albert.test.bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;


public class BitMapActivity extends Activity {
    ImageView imageViewBitMap;
    Bitmap dataBitMap, dirtyBitMap;
    RelativeLayout relativeLayoutBitMap;
    float imageX, imageY;
    int[] viewCoordinates = new int[2];
    HashMap<Integer, DUN> hashMap;
    /** arbitrary number based on the biggest area's pixel count to optimize fill rates */
    static final int MAX_FILL = 131072;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_map);
        hashMap = new HashMap<>();
        hashMap.put(0, new DUN("ZONE A Yellow", 334, Color.YELLOW));
        hashMap.put(1, new DUN("ZONE B Green", 544, Color.GREEN));
        hashMap.put(2, new DUN("ZONE C Blue", 671, Color.BLUE));
        hashMap.put(3, new DUN("ZONE D Red", 121, Color.RED));

        imageViewBitMap = (ImageView) findViewById(R.id.imageViewBitMap);
        imageViewBitMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onTouched(motionEvent);
                return false;
            }
        });
        relativeLayoutBitMap = (RelativeLayout) findViewById(R.id.relativelayoutBitMap);
        dataBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.testmap);
        dirtyBitMap = dataBitMap.copy(dataBitMap.getConfig(), true); // copies a fake map without the true data
        dataBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.datamap);
        imageViewBitMap.getLocationOnScreen(viewCoordinates);
        imageViewBitMap.setImageBitmap(dirtyBitMap);
    }

    class DUN {
        String name;
        int voters, color;

        DUN(String name, int voters, int color) {
            this.name = name;
            this.voters = voters;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public int getVoters() {
            return voters;
        }

        public int getColor() {
            return color;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        imageY = dataBitMap.getHeight() / imageViewBitMap.getHeight();
        imageX = dataBitMap.getWidth() / imageViewBitMap.getWidth();
    }

    void onTouched(MotionEvent motionEvent) {
        float x = motionEvent.getX() - viewCoordinates[0];
        float y = motionEvent.getY() - viewCoordinates[1];
        resolveMap((int) (x * imageX), (int) (y * imageY));
    }


    void resolveMap(int x, int y) {
        int colorFound = dataBitMap.getPixel(x, y);
        if (Color.green(colorFound)>0) {
            return;
        }
        int blue = Color.blue(colorFound);
        DUN dun = hashMap.get(blue);
        if (dun==null) {
            return;
        }
        int i = 0;
        Log.d("BMA.resolveMap 2", "got something to look for... " + dun.getName());
        for (int lx = 0; lx < dataBitMap.getWidth(); lx++) {
            for (int ly = 0; ly < dataBitMap.getHeight(); ly++) {
                if (dataBitMap.getPixel(lx, ly) == colorFound) {
                    i++;
                    if (i>MAX_FILL) {
                        break;
                    }
                    dirtyBitMap.setPixel(lx, ly, dun.getColor());
                }
            }
            if (i>MAX_FILL) {
                break;
            }
        }
        Log.d("BMA.resolveMap 3", "done looking for... " + dun.getName());
        setTitle("Clicked " + dun.getName() + " pixels: " + i);

        imageViewBitMap.setImageBitmap(dirtyBitMap);
    }

}
