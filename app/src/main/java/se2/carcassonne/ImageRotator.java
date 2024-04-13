package se2.carcassonne;
import android.widget.ImageView;

public class ImageRotator {
    private ImageView imageView;

    public ImageRotator(ImageView i) {
        this.imageView = i;
    }

    public void rotateRight() {
        float currentRotation = imageView.getRotation();
        imageView.setRotation(currentRotation + 90);
    }
}
