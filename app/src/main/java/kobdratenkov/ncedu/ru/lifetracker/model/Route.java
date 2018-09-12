package kobdratenkov.ncedu.ru.lifetracker.model;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;



public class Route {

    private Long routeID;

    private List<Coordinate> coordinates;

    private Long userID;

    public Route() {
        coordinates = new ArrayList<Coordinate>();
    }

    public Route(Long userID) {
        this.userID = userID;
    }

    public Route(List<Coordinate> coordinates, Long userID) {
        this.coordinates = coordinates;
        this.userID = userID;
    }

    public String toString() {
        return String.format(" Route: id = %d, user = '%d', number of coords = '%d'",
                routeID, userID, coordinates.size());
    }

    /**
     * methods add new Coordinate to the list initializing if it necessary
     * @param next
     */

    public void addCoordinate(Coordinate next) {
        if (coordinates == null) {
            coordinates = new ArrayList<>();
        }
        coordinates.add(next);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addAllCoordinates(Iterable<Coordinate> nextCoordinates) {
        if (coordinates == null) {
            coordinates = new ArrayList<>();
        }
        nextCoordinates.forEach(coordinates::add);
    }



    public Long getRouteID() {
        return routeID;
    }

    public void setRouteID(Long routeID) {
        this.routeID = routeID;
    }


    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
