package org.project.adam.ui.diet;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.joda.time.LocalTime;
import org.project.adam.Preferences_;
import org.project.adam.persistence.Meal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

@EBean
public class MealLoader {

    public static final String CSV_DEFAULT_SEPARATOR  =";";

    @Pref
    protected Preferences_ preferences;


    public  List<Meal> parseMealsFromCsv(InputStream csvFile) throws IOException {
        Timber.d("parseMealsFromCsv - start");
        List<String> lines = readFileContent(csvFile);
        String separator = preferences.fieldSeparatorsForImport().getOr(CSV_DEFAULT_SEPARATOR);
        List<Meal> result = new ArrayList<>();
        Map<String,String> mealMap = new HashMap<>();

        for(String line : lines){
            int index = line.indexOf(separator);
            if(index >= 0 && index < (line.length() - separator.length())){
                String time = line.substring(0,index);
                String mealContent;
                if(mealMap.containsKey(time)){
                    //concat content
                    mealContent = mealMap.get(time)+ "\n"+line.substring(index+separator.length(), line.length()).trim();
                }else{
                    mealContent = line.substring(index+separator.length(), line.length()).trim();
                }
                mealMap.put(time,mealContent);
            }
        }

        for (Map.Entry<String, String> entry : mealMap.entrySet()) {
            result.add(Meal.builder()
                .timeOfDay(parseTimeOfDay(entry.getKey()))
                .content(entry.getValue())
                .build());
        }

        return result;
    }

    LocalTime parseTimeOfDay(String timeOfDay)throws IOException {
        Pattern pattern = Pattern.compile("^(\\d{1,2}?):(\\d{1,2}?)$");
        Matcher matcher = pattern.matcher(timeOfDay);
        if(matcher.find()){
            return new LocalTime(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))) ;
        } else {
            throw new IOException("Invalid time description: "+timeOfDay);
        }

    }

    private List<String> readFileContent(InputStream csvFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String lineTrimmed = line.trim();
                if(lineTrimmed.length() > 0){
                    lines.add(line);
                }
            }
            return lines;
        }
    }

}
