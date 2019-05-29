package takuma.idei.ideiapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class HomeView extends View {
    public HomeView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setTextSize(48);
        canvas.drawText("ハイ",48,4,paint);
    }
}
