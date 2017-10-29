package re.parkhe.parkhere.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by ryan on 10/28/17.
 */

public class ParkingLotsList {
    private static ParkingLotsList sParkingLotsList;

    private Context mContext;
    private ArrayList<ParkingLot> mParkingLots;

    private ParkingLotsList(Context context) {
        mContext = context.getApplicationContext();
        mParkingLots = new ArrayList<>();
    }

    public static ParkingLotsList get(Context context) {
        if (sParkingLotsList == null) {
            sParkingLotsList = new ParkingLotsList(context);
        }
        return sParkingLotsList;
    }

    public void addParkingLot(ParkingLot parkingLot) {
        mParkingLots.add(parkingLot);
    }

    public void setParkingLotsList(ArrayList<ParkingLot> parkingLots) {
        mParkingLots = parkingLots;
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return mParkingLots;

    }

    public ParkingLot getParkingLotsFromPostion(int p) {
        return mParkingLots.get(p);
    }
}
