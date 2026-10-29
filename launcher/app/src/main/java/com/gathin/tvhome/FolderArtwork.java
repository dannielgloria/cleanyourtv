package com.gathin.tvhome;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/** Four original app images drawn directly into a folder cover, without an extra bitmap. */
final class FolderArtwork extends Drawable {
    private final List<Drawable> images = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF cell = new RectF();
    private final Rect previousBounds = new Rect();
    private final int background;

    FolderArtwork(List<Drawable> source, int background) {
        this.background = background;
        for (Drawable image : source) {
            if (image != null) images.add(image);
            if (images.size() == 4) break;
        }
    }

    @Override public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        paint.setColor(background);
        canvas.drawRect(bounds, paint);
        float gap = bounds.height() * 0.08f;
        int columns = images.size() == 1 ? 1 : 2;
        int rows = images.size() > 2 ? 2 : 1;
        float width = (bounds.width() - (columns + 1) * gap) / columns;
        float height = (bounds.height() - (rows + 1) * gap) / rows;
        for (int i = 0; i < images.size(); i++) {
            float left = bounds.left + gap + (i % columns) * (width + gap);
            float top = bounds.top + gap + (i / columns) * (height + gap);
            cell.set(left, top, left + width, top + height);
            paint.setColor(0x243D526B);
            canvas.drawRoundRect(cell, gap * 0.5f, gap * 0.5f, paint);
            Drawable image = images.get(i);
            float fit = Math.min(width / Math.max(1, image.getIntrinsicWidth()),
                    height / Math.max(1, image.getIntrinsicHeight()));
            float w = Math.max(1, image.getIntrinsicWidth()) * fit;
            float h = Math.max(1, image.getIntrinsicHeight()) * fit;
            previousBounds.set(image.getBounds());
            image.setBounds(Math.round(left + (width - w) / 2), Math.round(top + (height - h) / 2),
                    Math.round(left + (width + w) / 2), Math.round(top + (height + h) / 2));
            image.draw(canvas);
            image.setBounds(previousBounds);
        }
    }

    @Override public void setAlpha(int alpha) { }
    @Override public void setColorFilter(ColorFilter filter) { }
    @Override public int getOpacity() { return PixelFormat.OPAQUE; }
    @Override public int getIntrinsicWidth() { return 320; }
    @Override public int getIntrinsicHeight() { return 180; }
}
