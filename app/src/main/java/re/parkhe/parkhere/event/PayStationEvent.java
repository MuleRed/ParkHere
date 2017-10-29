package re.parkhe.parkhere.event;

/**
 * Created by ryan on 10/29/17.
 */

public class PayStationEvent {
    private int id;

    public PayStationEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
