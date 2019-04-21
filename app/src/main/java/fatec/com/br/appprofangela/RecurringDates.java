package fatec.com.br.appprofangela;


// A compatibility layer for joda-time

import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.util.Log;
import android.widget.Switch;

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
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.time.temporal.ChronoUnit.DAYS;


public class RecurringDates {

    public static String currentDay() {

        LocalDate start = new LocalDate(LocalDate.now());

        String dataAtual = start.toString();

        return dataAtual;
    }


    public static boolean rrule(String frequencia, String intervalo, List<String> listDiasSemana, String dataInicial, String diaSelecionado) throws ParseException {

        int plusDay = 0;

        boolean check = false;

        int checkDiaDaSemana = 0;

        //String dias;

        //String exclusao="\nEXDATE:"+"2019-04-19";//dateTime2.toDateTime().toString();

        LocalDate start = new LocalDate(dataInicial); //new LocalDate(LocalDate.now()); para a data atual do usuário.

        String diaDaSemanaDiaSelecionado;

        //TESTE PARA SABER A ZONE ID DO USUÁRIO
        //DateTimeZone zone = DateTimeZone.forTimeZone(TimeZone.getDefault()); //Mesmo resultado de DateTimeZone dateTimeZone = DateTimeZone.getDefault(); //Output: America/Sao_Paulo
        //DateTime dateTime2 = new DateTime ( zone ); //Output: 2019-04-20T00:21:39.974-03:00
        //String dateTimeZoned = dateTime2.toLocalTime ().toString ();  // Mesmo resultado de LocalTime localTime = new LocalTime(); //Output: 00:21:39.974.
        //TESTE PARA SABER A ZONE ID DO USUÁRIO

        DateTime data2 = new DateTime(diaSelecionado);

        DateTimeFormatter fmtDate = DateTimeFormat.forPattern("yyyyMMdd'T'HHMMSS'Z'"); //MM/dd/yyyy HH:mm:ss 20190530T083000Z

        //DateTimeFormatter fmt2 = ISODateTimeFormat.basicDateTimeNoMillis(); //Formatar a data no padrão ISO. (não funcionou para o padrão que precisamos para o parâmetro UNTIL da RRULE).

        String data = fmtDate.print(data2);

        Log.i("RRULE_DT_data2: ", data2.toString());

        switch (BaseActivity.diaDaSemanaSelecionado) {

            case 1:
                diaDaSemanaDiaSelecionado = "SU";
                break;
            case 2:
                diaDaSemanaDiaSelecionado = "MO";
                break;
            case 3:
                diaDaSemanaDiaSelecionado = "TU";
                break;
            case 4:
                diaDaSemanaDiaSelecionado = "WE";
                break;
            case 5:
                diaDaSemanaDiaSelecionado = "TH";
                break;
            case 6:
                diaDaSemanaDiaSelecionado = "FR";
                break;
            case 7:
                diaDaSemanaDiaSelecionado = "SA";
                break;
            default:
                diaDaSemanaDiaSelecionado = "";
        }

        for (int i = 0; i < listDiasSemana.size(); i++) {

            Log.i("RRULEDIA.LIST.: ", listDiasSemana.get(i));
            Log.i("RRULEDIA.STRING.: ", diaDaSemanaDiaSelecionado);

            if (listDiasSemana.get(i).contains(diaDaSemanaDiaSelecionado)) {
                Log.i("RRULEDIA.STRING.: ", "CHECK!");
                checkDiaDaSemana = 1;
            } else {
                checkDiaDaSemana = 0;
            }
        }

        if (intervalo.equals("1") && checkDiaDaSemana == 1) {

            Log.i("RRULEDiasCOMAtual: ", "COM DIA ATUAL");

        } else {
            plusDay = 1;

            Log.i("RRULEDiasSEMAtual: ", "SEM DIA ATUAL");
        }

        String diasDaSemanaSemEspacos = listDiasSemana.toString().substring(1, listDiasSemana.toString().length() - 1);

        diasDaSemanaSemEspacos = diasDaSemanaSemEspacos.replaceAll("\\s", "");

        Log.i("RRULE-NOW", start.toString());

        // PARA CALCULAR A QUANTIDADE DE DIAS ENTRE DUAS DATAS
        /*DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate dateTemp = LocalDate.parse(diaSelecionado, format);
        int i = Days.daysBetween(start, dateTemp).getDays();
        dias = String.valueOf(i); */

        String ical =
                //"DTSTART;TZID=US-Eastern:20190414T090000" + //ERROR
                "RRULE:FREQ=" + frequencia //DAYLI
                        + ";INTERVAL=" + intervalo //1
                        + ";UNTIL=" + data //20190530T083000Z
                        + ";WKST=SU"
                        + ";BYDAY=" + diasDaSemanaSemEspacos;  //"MO,FR"  // every Monday and Friday
        //+ ";BYMONTHDAY=13"  // that occurs on the 13th of the month
        //+ ";COUNT="+dias;  // stop after dias occurences

        // Print out each date in the series.
        for (LocalDate date :
                LocalDateIteratorFactory.createLocalDateIterable(ical, start.plusDays(plusDay), true)) {

            Log.i("RRULE.DIAS.RECORRE: ", date.toString());

            if (date.toString().equals(diaSelecionado)) {

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
/*  // UTILIZANDO OBJETOS JSON
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
