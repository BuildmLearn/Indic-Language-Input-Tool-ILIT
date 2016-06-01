package org.buildmlearn.indickeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;


public class CustomKeyboardView extends KeyboardView {

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected OnKeyboardActionListener getOnKeyboardActionListener() {
        return super.getOnKeyboardActionListener();
    }

    @Override
    public void setPreviewEnabled(boolean previewEnabled) {
        super.setPreviewEnabled(previewEnabled);
    }

    @Override
    public void onDraw(Canvas canvas) {
              super.onDraw(canvas);


        /* For themes
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {

            if(key.codes[0]==-99)
            {
                mPaint.setTextSize(22);
                String keyLabel = key.label.toString();
                canvas.drawText(keyLabel, key.x + (key.width / 2), key.y + (key.height / 2), mPaint);
                mPaint.setTextSize(22);
            }
            if (key.label != null) {
                String keyLabel = key.label.toString();
                canvas.drawText(keyLabel, key.x + key.width, key.y + key.height, mPaint);
            } else if (key.icon != null) {
                key.icon.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                key.icon.draw(canvas);


            }


        }
*/

    }
}
