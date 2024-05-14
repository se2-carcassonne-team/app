package se2.carcassonne.helper.utils;
import android.widget.ImageView;


public class ImageRotator {
    private final ImageView imageView;


    public ImageRotator(ImageView i) {
        this.imageView = i;
    }

    public void rotateRight() {
        float currentRotation = imageView.getRotation();
        imageView.setRotation(currentRotation + 90);
    }

    public void rotateLeft() {
        float currentRotation = imageView.getRotation();
        imageView.setRotation(currentRotation - 90);
    }
}
