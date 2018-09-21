package kobdratenkov.ncedu.ru.lifetracker.model;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;



public class Route {

    private Long routeID;

    private List<Coordinate> route;

    private Long userID;

    public Route() {
        route = new ArrayList<Coordinate>();
    }

    public Route(Long userID) {
        this.userID = userID;
    }

    public Route(List<Coordinate> coordinates, Long userID) {
        this.route = coordinates;
        this.userID = userID;
    }

    public String toString() {
        return String.format(" Route: id = %d, user = '%d', number of coords = '%d'",
                routeID, userID, route.size());
    }

    /**
     * methods add new Coordinate to the list initializing if it necessary
     * @param next
     */

    public void addCoordinate(Coordinate next) {
        if (route == null) {
            route = new ArrayList<>();
        }
        route.add(next);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addAllCoordinates(Iterable<Coordinate> nextCoordinates) {
        if (route == null) {
            route = new ArrayList<>();
        }
        nextCoordinates.forEach(route::add);
    }



    public Long getRouteID() {
        return routeID;
    }

    public void setRouteID(Long routeID) {
        this.routeID = routeID;
    }


    public List<Coordinate> getRoute() {
        return route;
    }

    public void setRoute(List<Coordinate> coordinates) {
        this.route = coordinates;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
