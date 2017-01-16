package com.android.finki.mpip.footballdreamteam.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.Button;

import com.android.finki.mpip.footballdreamteam.R;

/**
 * Created by Borce on 28.08.2016.
 */
public class ButtonAwesome extends Button {

    private static final String KEY = "font_awesome";
    private static final LruCache<String, Typeface> typefaceCache = new LruCache<>(12);

    public ButtonAwesome(Context context) {
        super(context);
        init();
    }

    public ButtonAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonAwesome(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Set the typeface to the component to be FontAwesome.
     */
    private void init() {
        Typeface typeface = typefaceCache.get(KEY);
        if (typeface == null) {
            String path = getContext().getString(R.string.font_awesome_path);
            typeface = Typeface.createFromAsset(getContext().getAssets(), path);
            typefaceCache.put(KEY, typeface);
        }
        setTypeface(typeface);
    }
}
