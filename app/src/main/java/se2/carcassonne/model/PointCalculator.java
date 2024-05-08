package se2.carcassonne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Data
@Getter
@Setter
public class PointCalculator {
        // Points per tile type
        private static final int POINTS_CITY = 2;
        private static final int POINTS_ROAD = 1;
        private static final int POINTS_MONASTERY = 1;
        private static final int POINTS_FIELD = 3;

        // TODO : Get current game board

        /**
         * Gets feature of a given tile based on the meeple position
         * @param tile
         * @return feature type
         */
        private int tileFeatureFromMeeplePosition(Tile tile){
                // TODO : Just get feature of meeple on the tile
                return 0;
        }

        //private checkCompatibility(Tile tile){
                /*
                if (on the edge) -> get a new tile and continue @1
                 */

                /*

                 */

        //}

}
