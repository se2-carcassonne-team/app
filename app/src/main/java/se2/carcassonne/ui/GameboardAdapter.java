package se2.carcassonne.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import se2.carcassonne.R;
import se2.carcassonne.model.Coordinates;
import se2.carcassonne.model.GameBoard;
import se2.carcassonne.model.Tile;

@Getter
@Setter
public class GameboardAdapter extends BaseAdapter {
    private Context context;
    private final int rows;
    private final int cols;
    private boolean yourTurn = true;
    private GameBoard gameBoard;
    private Tile tileToPlace;
    private Coordinates toPlaceCoordinates;


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
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(75, 75));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setClickable(false);

        int currentRow = position / rows;
        int currentCol = position % cols;
        boolean isMiddleField = (currentRow == 12 && currentCol == 12);
        boolean isNextToMiddleField = (currentCol == 13 && currentRow == 12);

        if (isMiddleField) {
            imageView.setImageResource(R.drawable.castle_wall_road_0);
        } else if (isNextToMiddleField && gameBoard.getGameBoardMatrix()[12][13] == null) {
            //gameBoard.placeTile(gameBoard.getAllTiles().get(1), new Coordinates(currentCol, currentRow));
            imageView.setImageResource(R.drawable.road_junction_large_0);
        } else {
            if (gameBoard.getGameBoardMatrix()[currentCol][currentRow] != null && tileToPlace != null) {
                // something was placed in this field
                imageView.setImageResource(
                        context.getResources().getIdentifier(tileToPlace.getImageName() + "_" + tileToPlace.getRotation(), "drawable", context.getPackageName()));
                imageView.setAlpha(0.9f);
            } else {
                imageView.setImageResource(R.drawable.backside);
                imageView.setAlpha(0.4f);
            }

        }
        if (tileToPlace != null) {
            highlightWithCurrentRotation(tileToPlace, currentCol, currentRow, imageView, isMiddleField);
        }

        return imageView;
    }

    private void highlightWithCurrentRotation(Tile currentTileToPlace, int currentCol, int currentRow, ImageView imageView, boolean isMiddleField) {
        ArrayList<Coordinates> highlightCoordinates = gameBoard.highlightValidPositions(currentTileToPlace);
        if (highlightCoordinates.contains(new Coordinates(currentCol, currentRow))) {
            imageView.setImageResource(
                    context.getResources().getIdentifier(tileToPlace.getImageName() + "_" + tileToPlace.getRotation(), "drawable", context.getPackageName()));
            imageView.setOnClickListener(v -> {
                int rota = currentTileToPlace.getRotation();
                toPlaceCoordinates = new Coordinates(currentCol, currentRow);
                if (yourTurn) {
                    float currentRotation = imageView.getRotation();
                    v.setRotation(currentRotation + 90 * rota);
                    toggleImage((ImageView) v);
                    yourTurn = true;
                }
            });
            notifyDataSetChanged();
        }
    }

    private void toggleImage(ImageView imageView) {
        if (imageView.getScaleX() != 1.0f) {
            imageView.setImageResource(R.drawable.backside);
            imageView.setAlpha(0.4f);
        } else {
            imageView.setImageResource(
                    context.getResources().getIdentifier(
                            tileToPlace.getImageName() + "_" + 0, "drawable", context.getPackageName())
            );
            //imageView.setImageResource(R.drawable.castle_center_entry_0);
            imageView.setAlpha(0.9f);
        }
    }
}




