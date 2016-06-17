package org.buildmlearn.indickeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodSubtype;


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
    public boolean onLongPress(Keyboard.Key key) {
        if (key.codes[0] == Constants.SPACE_KEY) {
            getOnKeyboardActionListener().onKey(Constants.LongPressSPACEKEY, null);
            return true;
        }
        return super.onLongPress(key);

    }

    void setSubtypeOnSpaceKey(final InputMethodSubtype subtype) {
        final LatinKeyboard keyboard = (LatinKeyboard)getKeyboard();
        keyboard.setSpaceIcon(ContextCompat.getDrawable(getContext(),subtype.getIconResId()));
        invalidateAllKeys();
    }
/*
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       Drawable dr = new ColorDrawable(Color.BLUE);
        dr.setAlpha(30);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
            dr.draw(canvas);

            }


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


