package se.xjobb.scardfeud;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import se.xjobb.scardfeud.JsonGetClasses.Response;

/**
 * Created by Svempa on 2014-02-07.
 *
 * Class used Custom sorting by last event, in adapters
 */
public class CustomComparator implements Comparator<Response> {

    @Override
    public int compare(Response objOne, Response objTwo) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date dateObjOne = null;
        Date dateObjTwo = null;
        try {
            dateObjOne = format.parse(objOne.lastEvent);
            dateObjTwo = format.parse(objTwo.lastEvent);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateObjOne != null && dateObjTwo != null) {
            return dateObjTwo.compareTo(dateObjOne);
        } else {
            return 0;
        }
    }
}


//    "lastevent": "2014-01-31 13:59:34",
