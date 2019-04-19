package fatec.com.br.appprofangela;


// A compatibility layer for joda-time
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.util.Log;

//import com.google.ical.compat.jodatime.LocalDateIteratorFactory;
import com.google.ical.compat.jodatime.LocalDateIteratorFactory;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
// A Joda time class that represents a day regardless of timezone
//import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
//import org.dmfs.rfc5545.recur.RecurrenceRule;
//import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;
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
import java.time.ZoneId;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.time.temporal.ChronoUnit.DAYS;


public class RecurringDates {

    /** print the first 13 Friday the 13ths in the 3rd millenium AD. */
    public static boolean rrule(String frequencia, String intervalo, String diasSemana, String diaSelecionado) throws ParseException {

        boolean check = false;

        LocalDate start = new LocalDate(LocalDate.now());

        DateTimeZone zone = DateTimeZone.forTimeZone(TimeZone.getDefault());

        DateTime dateTime = new DateTime ( zone );

        String dateTimeZoned = dateTime.toLocalTime ().toString ();

        Log.i("RRULE_DT_TIME", dateTimeZoned);

        String dias="";

        LocalDate localDate = new LocalDate(diaSelecionado);
        Log.i("RRULE-LOCALDATE", localDate.toString());


        Log.i("RRULE-NOW", start.toString());
        //java.time.LocalDate start = java.time.LocalDate.now();

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        //java.time.LocalDate dateTemp = java.time.LocalDate.parse(dt, formatter);

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

        LocalDate dateTemp = LocalDate.parse(diaSelecionado, format);

        int i = Days.daysBetween(start, dateTemp).getDays();

        dias = String.valueOf(i);

        Log.i("RRULE-DIAS", dias);

        // Every friday the thirteenth.
        String ical = "RRULE:FREQ="+frequencia //DAYLI Diariamente
                + ";INTERVAL="+intervalo //1"
                + ";WKST=SU"
                + ";BYDAY="+diasSemana  //MO,FR"  // every Monday and Friday
                //+ ";BYMONTHDAY=13"  // that occurs on the 13th of the month
                + ";COUNT="+dias;  // stop after dias occurences

        // Print out each date in the series.
        for (LocalDate date :
                LocalDateIteratorFactory.createLocalDateIterable(ical, start, true)) {
            //Log.i("RRULE", date.toString());
            //System.out.println(date);

            if(date.toString().equals(diaSelecionado) && !date.toString().equals(start)){

                Log.i("RRULE", "Pertence!");

                check = true;

                break;

            } else {

                check = false;

            }

        }
/*
        RecurrenceRule recurrenceRule = null;
        try {
            recurrenceRule = new RecurrenceRule(ical); //"FREQ=YEARLY;BYMONTHDAY=23;BYMONTH=5;COUNT=3"
        } catch (InvalidRecurrenceRuleException e) {
            e.printStackTrace();
        }

        RecurrenceRuleIterator it = recurrenceRule.iterator(org.dmfs.rfc5545.DateTime.nowAndHere());
        int maxInstances = 10; // limit instances for rules that recur forever
        while (it.hasNext() && (!recurrenceRule.isInfinite() || maxInstances-- > 0)) {
            org.dmfs.rfc5545.DateTime nextInstance = it.nextDateTime();
            System.out.println(nextInstance);

            if(nextInstance.toString().equals(diaSelecionado)){

                Log.i("RRULE", "Pertence!");

                check = true;

                break;

            } else {

                check = false;

            }
        }
/*
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
*/
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



        return check;
    }

}
