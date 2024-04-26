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
            if (e1 != null && e2 != null) {
                // Horizontales Scrollen
                int scrollX = view.getScrollX() + (int) (e1.getX() - e2.getX());
                int maxScrollX = view.getWidth() - view.getWidth(); // Anpassen, wenn das GridView nicht die volle Breite hat
                view.scrollTo(Math.max(0, Math.min(maxScrollX, scrollX)), view.getScrollY());

                // Vertikales Scrollen
                int scrollY = view.getScrollY() + (int) (e1.getY() - e2.getY());
                int maxScrollY = view.getHeight() - view.getHeight(); // Anpassen, wenn das GridView nicht die volle Höhe hat
                view.scrollTo(view.getScrollX(), Math.max(0, Math.min(maxScrollY, scrollY)));
            }
            return true;
        }

    }
}

