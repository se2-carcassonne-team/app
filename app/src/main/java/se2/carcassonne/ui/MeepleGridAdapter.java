package se2.carcassonne.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import lombok.Getter;
import lombok.Setter;
import se2.carcassonne.R;
import se2.carcassonne.model.Coordinates;

@Getter
@Setter
public class MeepleGridAdapter extends BaseAdapter {
    private final Context context;
    private Coordinates meeplePlacementCoordinates;
    private ImageView meepleSelected = null;

    public MeepleGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 9;
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
            int dpSize = (int) (60 * context.getResources().getDisplayMetrics().density); // Convert 60 pixels to dp
            imageView.setLayoutParams(new ViewGroup.LayoutParams(dpSize, dpSize));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int padding = (int) (5 * context.getResources().getDisplayMetrics().density); // Adjust padding as needed
            imageView.setPadding(padding, padding, padding, padding);
        } else {
            imageView = (ImageView) convertView;
        }

        // TODO : Check if image is relevant for the meeple placement
        imageView.setClickable(true);
        imageView.setImageResource(R.drawable.meeple_road);

        int currentRow = position / 3;
        int currentCol = position % 3;


        boolean isMeeplePlacement = meeplePlacementCoordinates != null && meeplePlacementCoordinates.getXPosition() == currentRow &&
                meeplePlacementCoordinates.getYPosition() == currentCol;

        // TODO : Set meeple dynamically based on color and if the place is relevant
        imageView.setImageResource(isMeeplePlacement ? R.drawable.meeple_blue : R.drawable.meeple_road);

        imageView.setOnClickListener(v -> {
            meeplePlacementCoordinates = new Coordinates(currentRow, currentCol);
            notifyDataSetChanged();
        });

        return imageView;
    }
}
