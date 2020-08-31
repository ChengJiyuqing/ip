import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Event extends Task {
    String time;
    LocalDate localDate;

    public Event(String name, Status status, String time) {
        super(name, status);
        this.time = time;
        this.localDate = LocalDate.parse(time);

    }

    @Override public String toString() {
        return "[E] " + this.status.statusToSymbol() + " " + this.name + " (at:" +
                localDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))
                + ")";
    }
}
