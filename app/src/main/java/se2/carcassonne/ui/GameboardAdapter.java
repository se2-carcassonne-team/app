package se2.carcassonne.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import se2.carcassonne.R;
import se2.carcassonne.model.Tile;

public class GameboardAdapter extends BaseAdapter {

    private Context context;
    private int rows, cols;
    private boolean yourTurn = true;

    private Tile tile;



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
            imageView.setLayoutParams(new ViewGroup.LayoutParams(75, 75)); // Anpassen Sie die Größe nach Bedarf
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rota=GameBoardActivity.getText();
                if(!isMiddleField && yourTurn){
                    float currentRotation = imageView.getRotation();
                    v.setRotation(currentRotation + 90*rota);
                    toggleImageScale((ImageView) v);
                    yourTurn = true;
                }
            }
        });

        return imageView;
    }

    private void toggleImageScale(ImageView imageView) {
        if (imageView.getScaleX() != 1.0f) {
            imageView.setImageResource(R.drawable.backside);
            imageView.setAlpha(0.4f);
        } else {
            imageView.setImageResource(R.drawable.monastery_1);
            imageView.setAlpha(0.9f);
        }
    }


}




