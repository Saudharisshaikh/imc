package sa.med.imc.myimc.FilePicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ArcView extends View {

    private Paint paint;
    private int radius, x, y;
    private int color;

    public ArcView(Context context) {
        super(context);
        init();
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ArcView(Context context, int radius, int x, int y, int color) {
        super(context);

        this.radius = radius;
        this.x = x;
        this.y = y;
        this.color = color;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, radius, paint);

    }


}
