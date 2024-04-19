package se2.carcassonne;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ZoomFunction implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private int lastScrollX;
    private int lastScrollY;
    private View view;

    public ZoomFunction(Context context, View view) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.view = view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            lastScrollX = (int) e.getX();
            lastScrollY = (int) e.getY();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Horizontales Scrollen
            int scrollX = view.getScrollX() + lastScrollX - (int) e2.getX();
            view.scrollTo(scrollX, view.getScrollY());
            lastScrollX = (int) e2.getX();

            // Vertikales Scrollen
            int scrollY = view.getScrollY() + lastScrollY - (int) e2.getY();
            view.scrollTo(view.getScrollX(), scrollY);
            lastScrollY = (int) e2.getY();
            return true;
        }
    }
}

