package se2.carcassonne.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import se2.carcassonne.R;

public class GameboardAdapter extends BaseAdapter {

    private Context context;
    private int rows, cols;

    public GameboardAdapter(Context context, int rows, int cols) {
        this.context = context;
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public int getCount() {
        return rows * cols;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(80, 75)); // Anpassen Sie die Größe nach Bedarf
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        int middleRow = 5;
        int middleCol = 12;
        boolean isMiddleField = position / cols == middleRow && position % cols == middleCol;

        // Startfeld-Bedingung
        if (isMiddleField) {
            imageView.setImageResource(R.drawable.start_field);
        } else {
            // Andernfalls verwenden Sie das Standardbild
            imageView.setImageResource(R.drawable.backside);
            imageView.setAlpha(0.4f);
        }
        imageView.setOnClickListener(v -> toggleImageScale((ImageView) v));

        return imageView;
    }

    private void toggleImageScale(ImageView imageView) {
        if (imageView.getScaleX() != 1.0f) {
            imageView.setImageResource(R.drawable.backside);
            imageView.setAlpha(0.4f);
        } else {
            imageView.setImageResource(R.drawable.monastery_0);
            imageView.setAlpha(0.9f);
        }
    }
}




