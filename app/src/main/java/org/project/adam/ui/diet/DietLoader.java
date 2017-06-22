package org.project.adam.ui.diet;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.project.adam.Preferences_;
import org.project.adam.persistence.Lunch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

@EBean
public class DietLoader {

    public static final String CSV_DEFAULT_SEPARATOR  =";";

    @Pref
    protected Preferences_ preferences;


    public  List<Lunch> parseLunchesFromCsv(File csvFile) throws IOException {
        Timber.d("parseLunchesFromCsv - start - %s", csvFile.getAbsolutePath());
        List<String> lines = readFileContent(csvFile);
        String separator = preferences.fieldSeparatorsForImport().getOr(CSV_DEFAULT_SEPARATOR);
        List<Lunch> result = new ArrayList<>();
        for(String line : lines){
            int index = line.indexOf(separator);
            if(index >= 0 && index < (line.length() - separator.length())){
                result.add(Lunch.builder()
                    .timeOfDay(parseTimeOfDay(line.substring(0,index)))
                    .content(line.substring(index+separator.length(), line.length()))
                    .build());
            }
        }
        return result;
    }

    int parseTimeOfDay(String timeOfDay)throws IOException {
        Pattern pattern = Pattern.compile("^(\\d{1,2}?):(\\d{1,2}?)$");
        Matcher matcher = pattern.matcher(timeOfDay);
        if(matcher.find()){
            return Integer.parseInt(matcher.group(1)) *  60 + Integer.parseInt(matcher.group(2));
        } else {
            throw new IOException("Invalid time description: "+timeOfDay);
        }

    }

    private List<String> readFileContent(File csvFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
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
