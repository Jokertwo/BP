package my.bak.trafic.core.plugin.transport;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.Optional;


@Getter
@ToString
public class ExportDataBuilder {

    private Optional<Date> begin = Optional.empty();
    private Optional<Date> end = Optional.empty();
    private Optional<String> direction = Optional.empty();
    private Optional<String> place = Optional.empty();
    private Optional<String> value = Optional.empty();
    private Optional<String> type = Optional.empty();


    public ExportDataBuilder setBegin(Date begin) {
        this.begin = Optional.ofNullable(begin);
        return this;
    }


    public ExportDataBuilder setEnd(Date end) {
        this.end = Optional.ofNullable(end);
        return this;
    }


    public ExportDataBuilder setDirection(String direction) {
        this.direction = Optional.ofNullable(direction);
        return this;
    }


    public ExportDataBuilder setPlace(String place) {
        this.place = Optional.ofNullable(place);
        return this;
    }


    public ExportDataBuilder setValue(String value) {
        this.value = Optional.ofNullable(value);
        return this;
    }


    public ExportDataBuilder setType(String type) {
        this.type = Optional.ofNullable(type);
        return this;
    }

}
