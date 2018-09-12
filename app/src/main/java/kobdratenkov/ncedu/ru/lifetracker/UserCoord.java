package kobdratenkov.ncedu.ru.lifetracker;

import android.graphics.Point;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"lat", "lng", "date"})

public class UserCoord {
    private double lat;
    private double lng;
    private long date;

    public UserCoord(){
    }

    public UserCoord(double lat, double lng, long date){
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    @JsonGetter
    public double getLat(){
        return this.lat;
    }
    @JsonGetter
    public double getLng(){
        return this.lng;
    }
    @JsonGetter
    public long getDate(){
        return this.date;
    }
}
