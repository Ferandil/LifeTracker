package kobdratenkov.ncedu.ru.lifetracker.model;

import java.sql.Timestamp;

import kobdratenkov.ncedu.ru.lifetracker.UserCoord;

/**
 * Class represent a related node entity in the noSQL database Neo4j
 * Graph storage of routes and coordinates with Long primary key
 * Route-node is a main node. Dependent node entity is Coordinate.
 * Coordinate node doesn't have link to the route or user.
 * Coordinates are being created by cascade
 */

public class Coordinate {
    private Long coordID;
    private Double latitude;
    private Double longitude;
    private Timestamp timestamp;

    public Coordinate(Double latitude, Double longitude, Long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = new Timestamp(time);
    }

    public Coordinate() {
    }

    public Coordinate(UserCoord userCoord){
        this.timestamp = new Timestamp(userCoord.getDate());
        this.latitude = userCoord.getLat();
        this.longitude = userCoord.getLng();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


}
