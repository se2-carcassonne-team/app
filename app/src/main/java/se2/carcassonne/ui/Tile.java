package se2.carcassonne.ui;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Tile {
    private int id;
    private String name;
    private int routine_id;


    public Tile(int id, String name, int routine_id) {
        this.id = id;
        this.name = name;
        this.routine_id = routine_id;
    }
    public Tile(int routine_id) {
        this.routine_id = routine_id;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getRoutineId() {
        return routine_id;
    }
    public void setRoutineId(int routine_id) {
        this.routine_id = routine_id;
    }


    public void rotate(boolean right){
        if(right){
            routine_id++;
            if (routine_id > 3) {
                routine_id=0;
            }
        }else {
            routine_id--;
            if(routine_id<0){
                routine_id =3;
            }
        }
    }


}
