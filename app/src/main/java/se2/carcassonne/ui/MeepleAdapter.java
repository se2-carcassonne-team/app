package se2.carcassonne.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import se2.carcassonne.R;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.Meeple;
import se2.carcassonne.model.Tile;
import se2.carcassonne.repository.PlayerRepository;

@Getter
@Setter
public class MeepleAdapter extends BaseAdapter {
    private final Context context;
    private Coordinates placeMeepleCoordinates;
    private ImageView meepleSelected = null;
    private Tile tileToPlace;
    private boolean[] allowedMeeplePositions;
    private boolean isRoadFreeForMeeple;

    public MeepleAdapter(Context context, Tile tileToPlace, boolean isRoadFreeForMeeple) {
        this.context = context;
        this.tileToPlace = tileToPlace;
        this.allowedMeeplePositions = tileToPlace.getAllowedMeeplePositions();
        this.isRoadFreeForMeeple = isRoadFreeForMeeple;
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
        ImageView imageView = setupImageView(convertView);

        // Determine the feature at the current subgrid position after considering tile rotation
        int featureType = tileToPlace.rotatedFeatures(tileToPlace.getRotation())[position];

        // Check if the position is allowed for placing a meeple
        if (allowedMeeplePositions[position]) {
            if (featureType == 2) {  // Feature is a road
                setupRoadFeature(imageView, position);
            } else {  // Feature is not a road
                setupOtherFeature(imageView, position);
            }
        } else {
            imageView.setImageResource(android.R.color.transparent); // Or any placeholder
            imageView.setClickable(false);
        }

        return imageView;
    }

    private ImageView setupImageView(View convertView) {
        ImageView imageView = (convertView instanceof ImageView) ? (ImageView) convertView : new ImageView(context);
        int dpSize = (int) (60 * context.getResources().getDisplayMetrics().density);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(dpSize, dpSize));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        int padding = (int) (5 * context.getResources().getDisplayMetrics().density);
        imageView.setPadding(padding, padding, padding, padding);
        return imageView;
    }

    private void setupRoadFeature(ImageView imageView, int position) {
        if (isRoadFreeForMeeple) { // Road is free of other meeples
            makeImageViewClickable(imageView, position);
        } else {
            imageView.setImageResource(R.drawable.road_occupied); // Display an indication that the road is blocked
            imageView.setClickable(false);
        }
    }

    private void setupOtherFeature(ImageView imageView, int position) {
        makeImageViewClickable(imageView, position);
    }

    private void makeImageViewClickable(ImageView imageView, int position) {
        imageView.setClickable(true);

        int currentRow = position / 3;
        int currentCol = position % 3;
        boolean isMeeplePlacement = placeMeepleCoordinates != null &&
                placeMeepleCoordinates.getXPosition() == currentRow &&
                placeMeepleCoordinates.getYPosition() == currentCol;

        String resourceName = "meeple_" + PlayerRepository.getInstance().getCurrentPlayer().getPlayerColour().name().toLowerCase();
        imageView.setImageResource(isMeeplePlacement ? context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName()) : R.drawable.meeple_road);

        imageView.setOnClickListener(v -> {
            placeMeepleCoordinates = isMeeplePlacement ? null : new Coordinates(currentRow, currentCol);
            notifyDataSetChanged();
        });
    }

    public void removeMeeples(List<Meeple> meeplesToRemove) {
        for (Meeple meeple : meeplesToRemove) {
            if (meeple != null && meeple.getCoordinates() != null) {
                int positionIndex = meeple.getCoordinates().getXPosition() * 3 + meeple.getCoordinates().getYPosition();
                if (positionIndex >= 0 && positionIndex < allowedMeeplePositions.length) {
                    allowedMeeplePositions[positionIndex] = false;
                }
            }
        }
        notifyDataSetChanged(); // Refresh the GridView
    }


}
