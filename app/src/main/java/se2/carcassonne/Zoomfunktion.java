package se2.carcassonne;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class Zoomfunktion implements View.OnTouchListener {

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f; // Initialer Skalierungsfaktor
    private View view;

    public Zoomfunktion(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        view = v;
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private static final float SCALE_FACTOR = 0.1f;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= (1 + SCALE_FACTOR * detector.getScaleFactor());
            scaleFactor = Math.max(0.1f, scaleFactor);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            return true;
        }
    }
}

