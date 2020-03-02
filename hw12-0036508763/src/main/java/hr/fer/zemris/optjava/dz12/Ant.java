package hr.fer.zemris.optjava.dz12;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.optjava.dz12.Orientation.*;

/**
 * @author Marko Ivić
 * @version 1.0.0
 */
public class Ant {
    private Orientation orientation;
    private int width;
    private int height;
    private int xPosition;
    private int yPosition;
    private int foodEaten;
    private int actionsLeft;
    private List<Listener> listeners;

    public Ant(Orientation orientation, int width, int height, int actionsLeft) {
        this.orientation = orientation;
        this.width = width;
        this.height = height;
        this.xPosition = 0;
        this.yPosition = 0;
        this.foodEaten = 0;
        this.actionsLeft = 600;
        listeners = new ArrayList<>();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    private void notifyListeners() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).notifyListener();
        }
    }

    public void reset() {
        this.xPosition = 0;
        this.yPosition = 0;
        this.foodEaten = 0;
        this.actionsLeft = 600;
        this.orientation = EAST;
    }

    public boolean foodInFront(int[][] map) {
        boolean foodInFront;
        if (orientation == Orientation.EAST) {
            foodInFront = map[yPosition][(xPosition + 1) % width] == 1;
        } else if (orientation == WEST) {
            foodInFront = map[yPosition][(xPosition - 1 + width) % width] == 1;
        } else if (orientation == NORTH) {
            foodInFront = map[(yPosition - 1 + height) % height][xPosition] == 1;
        } else {
            foodInFront = map[(yPosition + 1) % height][xPosition] == 1;
        }
        return foodInFront;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean move(int[][] map) {
        notifyListeners();
        if (map[yPosition][xPosition] == 1) {
            foodEaten++;
        }
        map[yPosition][xPosition] = 0;
        if (noActionsLeft()) return false;
        if (orientation == Orientation.EAST) {
            xPosition = (xPosition + 1) % width;
        } else if (orientation == WEST) {
            xPosition = (xPosition - 1 + width) % width;
        } else if (orientation == NORTH) {
            yPosition = (yPosition - 1 + height) % height;
        } else {
            yPosition = (yPosition + 1) % height;
        }
        if (map[yPosition][xPosition] == 1) {
            foodEaten++;
        }
        map[yPosition][xPosition] = 0;
        actionsLeft--;
        return true;
    }

    public boolean rotateLeft() {
        notifyListeners();
        if (noActionsLeft()) return false;
        orientation = switch (orientation) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
        actionsLeft--;
        return true;
    }

    public boolean rotateRight() {
        notifyListeners();
        if (noActionsLeft()) return false;
        orientation = switch (orientation){
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
        actionsLeft--;
        return true;
    }

    public boolean noActionsLeft() {
        if (actionsLeft <= 0) {
            return true;
        }
        return false;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public int getActionsLeft() {
        return actionsLeft;
    }

    public void setActionsLeft(int actionsLeft) {
        this.actionsLeft = actionsLeft;
    }
}
