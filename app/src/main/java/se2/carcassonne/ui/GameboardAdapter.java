package se2.carcassonne.ui;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;


import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import se2.carcassonne.R;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Tile;
import se2.carcassonne.repository.PlayerRepository;

@Getter
@Setter
public class GameboardAdapter extends BaseAdapter {
    private Context context;
    private final int rows;
    private final int cols;
    private boolean yourTurn = false;
    private boolean canPlaceTile = false;
    private boolean canPlaceMeeple = false;
    private GameBoard gameBoard;
    private Tile tileToPlace;
    private Coordinates toPlaceCoordinates;
    private int meepleCount = 7;

    public GameboardAdapter(Context context, GameBoard gameBoard, Tile tileToPlace) {
        this.context = context;
        this.gameBoard = gameBoard;
        this.rows = gameBoard.getGameBoardMatrix().length;
        this.cols = gameBoard.getGameBoardMatrix()[0].length;
        this.tileToPlace = tileToPlace;
    }

    public void setCurrentTileRotation(Tile tile) {
        this.tileToPlace = tile;
        toPlaceCoordinates = null;
        notifyDataSetChanged();
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
        FrameLayout frameLayout;
        ImageView imageView;
        ImageView overlayImageView;
        FrameLayout.LayoutParams overlayParams = new FrameLayout.LayoutParams(25, 25);

        if (convertView == null) {
            frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(75, 75));

            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(75, 75));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            frameLayout.addView(imageView);

            overlayImageView = new ImageView(context);
            overlayImageView.setLayoutParams(overlayParams);
            overlayImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            frameLayout.addView(overlayImageView);
        } else {
            frameLayout = (FrameLayout) convertView;
            imageView = (ImageView) frameLayout.getChildAt(0);
            overlayImageView = (ImageView) frameLayout.getChildAt(1);
        }


        imageView.setClickable(false);
        overlayImageView.setClickable(false);

        int currentRow = position / rows;
        int currentCol = position % cols;
        boolean isMiddleField = (currentRow == 12 && currentCol == 12);

        if (isMiddleField) {
            imageView.setImageResource(R.drawable.castle_wall_road_0);
        } else {
            if (gameBoard.getGameBoardMatrix()[currentCol][currentRow] != null) {
                // something was placed in this field
                for (Tile placedTile : gameBoard.getPlacedTiles()) {
                    if (currentCol == placedTile.getCoordinates().getXPosition() && currentRow == placedTile.getCoordinates().getYPosition()) {
                        imageView.setImageResource(
                                context.getResources().getIdentifier(placedTile.getImageName() + "_0", "drawable", context.getPackageName()));
                        //overlayImageView.setImageDrawable(getDrawable(context, R.drawable.meeple_blue));

                        imageView.setRotation(placedTile.getRotation() * 90f);
                        imageView.setAlpha(0.9f);

                        if (placedTile.getPlacedMeeple() != null) {
                            if(placedTile.getPlacedMeeple().getColor() != null) {
                                String resourceName = "meeple_" + placedTile.getPlacedMeeple().getColor().name().toLowerCase();
                                overlayImageView.setImageResource(context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName()));
                            }

                            Coordinates meepleCoordinates = placedTile.getPlacedMeeple().getCoordinates();
                            int meeplePosition = meepleCoordinates.getXPosition() * 3 + meepleCoordinates.getYPosition();

                            Log.d("meeple", "ID: " + placedTile.getId() + " Pos: " + meeplePosition);

                            //overlayImageView.setImageDrawable(getDrawable(context, R.drawable.meeple_blue));
                            overlayImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            switch (meeplePosition) {
                                case 0:
                                    overlayParams.setMargins(0, 2, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 1:
                                    overlayParams.setMargins(25, 2, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 2:
                                    overlayParams.setMargins(50, 2, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 3:
                                    overlayParams.setMargins(0, 25, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 4:
                                    overlayParams.setMargins(25, 25, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 5:
                                    overlayParams.setMargins(50, 25, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 6:
                                    overlayParams.setMargins(0, 50, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 7:
                                    overlayParams.setMargins(25, 50, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                case 8:
                                    overlayParams.setMargins(50, 50, 0, 0);
                                    overlayImageView.setLayoutParams(overlayParams);
                                    break;
                                default:
                                    break;
                            }
                        }

                    }
                }

                //get meeple position on tile and render meeple above tile


            } else {
                imageView.setImageResource(R.drawable.backside);
                imageView.setAlpha(0.4f);
            }
        }

        highlightWithCurrentRotation(tileToPlace, currentCol, currentRow, imageView);

        return frameLayout;
    }

    private void highlightWithCurrentRotation(Tile currentTileToPlace, int currentCol, int currentRow, ImageView imageView) {
        if (yourTurn && canPlaceTile) {
            ArrayList<Coordinates> highlightCoordinates = gameBoard.highlightValidPositions(currentTileToPlace);
            if (highlightCoordinates.contains(new Coordinates(currentCol, currentRow))) {
                if (toPlaceCoordinates != null && toPlaceCoordinates.getXPosition() == currentCol && toPlaceCoordinates.getYPosition() == currentRow) {
                    // set non-transparent imageView
                    imageView.setImageResource(
                            context.getResources().getIdentifier(tileToPlace.getImageName() + "_0",
                                    "drawable", context.getPackageName())
                    );
                    imageView.setAlpha(0.9f);
                } else {
                    // set transparent imageView
                    imageView.setImageResource(
                            context.getResources().getIdentifier(tileToPlace.getImageName() + "_0",
                                    "drawable", context.getPackageName())
                    );
                    imageView.setAlpha(0.4f);

                }

                imageView.setRotation(tileToPlace.getRotation() * 90f);
                imageView.setOnClickListener(v -> {
                    int currentTileRotation = currentTileToPlace.getRotation();
                    toPlaceCoordinates = new Coordinates(currentCol, currentRow);

                    float currentRotation = imageView.getRotation();
                    v.setRotation(currentRotation + 90 * currentTileRotation);
                    toggleImage((ImageView) v);
                    notifyDataSetChanged();
                });
                notifyDataSetChanged();
            }
        }

    }

    private void toggleImage(ImageView imageView) {
        if (imageView.getScaleX() != 1.0f) {
            imageView.setImageResource(R.drawable.backside);
            imageView.setAlpha(0.4f);
        } else {
            imageView.setImageResource(
                    context.getResources().getIdentifier(tileToPlace.getImageName() + "_0", "drawable", context.getPackageName()));
            imageView.setRotation(tileToPlace.getRotation() * 90f);
            imageView.setAlpha(0.9f);
        }
    }
}




