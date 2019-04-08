package fatec.com.br.appprofangela;


// A compatibility layer for joda-time
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.util.Log;

import com.google.ical.compat.jodatime.LocalDateIteratorFactory;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
// A Joda time class that represents a day regardless of timezone
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


public class RecurringDates {

    public void curringDate (){




    }


    /** print the first 13 Friday the 13ths in the 3rd millenium AD. */
    public static void rrule(String dt) throws ParseException {
        LocalDate start = new LocalDate();

        String dias="";

        //java.time.LocalDate start = java.time.LocalDate.now();

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        //java.time.LocalDate dateTemp = java.time.LocalDate.parse(dt, formatter);

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

        LocalDate dateTemp2 = LocalDate.parse(dt, format);

        //long daysBetween = DAYS.between(start, dateTemp2);

        int i = Days.daysBetween(start, dateTemp2).getDays();

        dias = String.valueOf(i);

        Log.i("RRULE-DIAS", dias);

        // Every friday the thirteenth.
        String ical = "RRULE:FREQ=WEEKLY"
                + ";INTERVAL=2"
                + ";BYDAY=MO,WE,TH,FR"  // every Friday
                //+ ";BYMONTHDAY=13"  // that occurs on the 13th of the month
                + ";COUNT="+dias;  // stop after dias occurences

        // Print out each date in the series.
        for (LocalDate date :
                LocalDateIteratorFactory.createLocalDateIterable(ical, start, true)) {
            Log.i("RRULE", date.toString());
            //System.out.println(date);

            if(date.toString().equals(dt)){
                Log.i("RRULE", "Pertence!");
                break;
            }
        }

        String nome = "Bernardo";
        JSONArray jsonArrayPart = new JSONArray();
        jsonArrayPart.put("Jaime");
        jsonArrayPart.put("Ana");
        jsonArrayPart.put("Junior");

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", "Trajeto A");
            jsonObject.put("motorista", nome);
            jsonObject.put("participantes", jsonArrayPart);
            jsonObject.put("km", 50);
            jsonObject.put("inicio", "Chevrolet Taubate");
            jsonObject.put("fim", "FATEC Taubate");
            jsonObject.put("freq", "RRULE:FREQ=WEEKLY"+";INTERVAL=2"+";BYDAY=MO");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("RRULE", e.getMessage());
        }

        //ParseObject obtJSON = ParseObject.createWithoutData("GrupoCarona", "AVWyMu5EH7");
        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GrupoCarona");

        query.whereEqualTo("objectId", "AVWyMu5EH7");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {

                if (e == null){

                    Log.i("RRULE_JSON", object.get("nomeGrupo").toString());
                    Log.i("RRULE_JSON", jsonObject.toString());

                    object.addUnique("segunda", jsonObject.toString());

                    object.saveInBackground();

                } else {

                    Log.e("RRULE_JSON", "Erro ao passar obejto JSON");

                }

            }
        });
        */
    }
}
