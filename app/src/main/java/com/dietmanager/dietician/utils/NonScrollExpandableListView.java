package com.dietmanager.dietician.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public final class NonScrollExpandableListView extends ExpandableListView {
    private HashMap _$_findViewCache;

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom  = MeasureSpec.makeMeasureSpec(
                536870911,  MeasureSpec.AT_MOST
        );
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        LayoutParams params = (LayoutParams) this.getLayoutParams();
        params.height = this.getMeasuredHeight();
    }

    public NonScrollExpandableListView(@Nullable Context context) {
        super(context);
    }

    public NonScrollExpandableListView(@Nullable Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollExpandableListView(@Nullable Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }
}

