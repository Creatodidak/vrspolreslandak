package id.creatodidak.vrspolreslandak.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.core.widget.NestedScrollView;

public class CustomNestedScrollView extends NestedScrollView {
    private boolean canScroll = true;

    public CustomNestedScrollView(Context context) {
        super(context);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }
}
