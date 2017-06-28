
package org.project.adam.ui.diet;

import android.content.Context;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.project.adam.Preferences_;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(ZohhakRunner.class)
public class MealLoaderTest {
    private static final String SEPARATOR = ";";

    private MealLoader mealLoader;

    @Before
    public void buildDietLoader() {
        Context context = mock(Context.class);
        Preferences_ preferences = new Preferences_(context);
        mealLoader = new MealLoader();
        mealLoader.preferences = preferences;
        when(context.getString(anyInt())).thenReturn(SEPARATOR);
    }

    @TestWith({"000:00", "00:000", ":00", "00:", "0000", ":", "0A:00", "00:A0"})
    public void time_should_not_been_read(final String badTimeFormat) {
        try {
            mealLoader.parseTimeOfDay(badTimeFormat);
            fail("Should not happen");
        } catch (IOException io) {
            assertThat(io.getMessage())
                .isEqualTo("Invalid time description: " + badTimeFormat);
        }
    }

    @TestWith(value = {"00:00;0", "01:01;61", "1:01;61", "1:1;61", "01:1;61", "11:6;666"},
        separator = ";")
    public void time_should_be_successfully_read(String timeOfDay, int expected)
        throws IOException {
        assertThat(mealLoader.parseTimeOfDay(timeOfDay)).isEqualTo(expected);
    }

    @Test
    public void testPattern() {

        Pattern pattern = Pattern.compile("^(\\d{1,2}?):(\\d{1,2}?)$");
        Matcher matcher = pattern.matcher("11:0");
        if (matcher.find()) {
            System.err.println(matcher.groupCount());
            System.err.println(matcher.group(0));
            System.err.println(matcher.group(1));
            System.err.println(matcher.group(2));
        } else {
            System.err.println("No match found");
        }

    }

}