package re.parkhe.parkhere;

import android.app.Application;

import re.parkhe.parkhere.model.ParkingLot;
import re.parkhe.parkhere.model.ParkingLotsList;

/**
 * Created by ryan on 10/28/17.
 */

public class App extends Application{



    @Override
    public void onCreate() {
        super.onCreate();
        ParkingLotsList parkingLotsList = ParkingLotsList.get(this);
        parkingLotsList.addParkingLot(new ParkingLot(33.7490, -84.3880));
    }


}
