package com.ww.administrator.demotest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.util.ViewUtil;

/**
 * Created by Administrator on 2016/9/20.
 */
public class CutoutTextView extends View {

    public static final float PHI = 1.6182f;
    private final TextPaint textPaint;
    private Bitmap cutout;
    private int foregroundColor = Color.parseColor("#F44336");
    private String text;
    private float textSize;
    private float textY;
    private float textX;
    private float maxTextSize;



    public CutoutTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        text = "家瓦商城";
        maxTextSize = context.getResources().getDimensionPixelSize(R.dimen.display_4_text_size);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateTextPosition();
        createBitmap();
    }

    private void calculateTextPosition() {
        float targetWidth = getWidth() / PHI;
        textSize = ViewUtil.getSingleLineTextSize(text, textPaint, targetWidth, 0f, maxTextSize,
                0.5f, getResources().getDisplayMetrics());
        textPaint.setTextSize(textSize);

        // measuring text is fun :] see: https://chris.banes.me/2014/03/27/measuring-text/
        textX = (getWidth() - textPaint.measureText(text)) / 2;
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        float textHeight = textBounds.height();
        textY = (getHeight() + textHeight) / 2;
    }

    private void createBitmap() {
        if (cutout != null && !cutout.isRecycled()) {
            cutout.recycle();
        }
        cutout = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        cutout.setHasAlpha(true);
        Canvas cutoutCanvas = new Canvas(cutout);
        cutoutCanvas.drawColor(foregroundColor);

        // this is the magic – Clear mode punches out the bitmap
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        cutoutCanvas.drawText(text, textX, textY, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(cutout, 0, 0, null);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return true;
    }
}
