package se2.carcassonne.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import lombok.Getter;
import lombok.Setter;
import se2.carcassonne.R;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.Tile;

@Getter
@Setter
public class MeepleAdapter extends BaseAdapter {
    private final Context context;
    private Coordinates placeMeepleCoordinates;
    private ImageView meepleSelected = null;
    private Tile tileToPlace;
    private boolean[] allowedMeeplePositions;

    public MeepleAdapter(Context context, Tile tileToPlace) {
        this.context = context;
        this.tileToPlace = tileToPlace;
        this.allowedMeeplePositions = tileToPlace.getAllowedMeeplePositions();
        rotateAllowedMeeplePositions();
    }

    private void rotateAllowedMeeplePositions() {
        if(tileToPlace.getRotation() != 0) {
            for(int i = 0; i < tileToPlace.getRotation(); i++) {
                this.allowedMeeplePositions = rotate90DegreesClockwise(allowedMeeplePositions);
            }
        }
    }

    public static boolean[] rotate90DegreesClockwise(boolean[] matrix) {
        boolean[] rotated = new boolean[9]; // Since it's a 3x3 matrix
        int N = 3; // Dimension of the matrix

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                rotated[j * N + (N - 1 - i)] = matrix[i * N + j];
            }
        }
        return rotated;
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
        if(this.allowedMeeplePositions[position]) {
            imageView.setClickable(true);
            //imageView.setImageResource(R.drawable.meeple_road);

            int currentRow = position / 3;
            int currentCol = position % 3;

            boolean isMeeplePlacement = placeMeepleCoordinates != null && placeMeepleCoordinates.getXPosition() == currentRow &&
                    placeMeepleCoordinates.getYPosition() == currentCol;

            imageView.setImageResource(isMeeplePlacement ? R.drawable.meeple_blue : R.drawable.meeple_road);

            imageView.setOnClickListener(v -> {
                placeMeepleCoordinates = new Coordinates(currentRow, currentCol);
                notifyDataSetChanged();
            });
        }

        // TODO : Set meeple dynamically based on color and if the place is relevant

        return imageView;
    }
}
