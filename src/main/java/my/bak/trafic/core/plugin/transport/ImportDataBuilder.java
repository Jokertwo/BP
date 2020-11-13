package my.bak.trafic.core.plugin.transport;

import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ImportDataBuilder {

    @Override
    public String toString() {
        return "DataBuilder [beginDate=" + beginDate + ", endDate=" + endDate + ", location=" + place
                + ", direction=" + direction + ", data=" + data + "]";
    }

    public static final Date UNKNOWN_DATE = new Date(0);
    public static final String UNKNOWN_LOCATION = "unknown";

    @Getter
    private Date beginDate = UNKNOWN_DATE;

    @Getter
    private Date endDate = UNKNOWN_DATE;

    @Getter
    private String place = UNKNOWN_LOCATION;

    @Getter
    private String direction = UNKNOWN_LOCATION;

    @Getter
    private Map<String, String> data = new HashMap<>();


    public ImportDataBuilder setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
        return this;
    }


    public ImportDataBuilder setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }


    public ImportDataBuilder setPlace(String place) {
        this.place = place;
        return this;
    }


    public ImportDataBuilder setDirection(String direction) {
        this.direction = direction;
        return this;
    }


    public ImportDataBuilder setData(Map<String, String> data) {
        this.data = data;
        return this;
    }

}
