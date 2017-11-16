package com.example.bruno.iair;

import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.Country;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ThingSpeakGetDataUnitTest {
    @Test
    public void gets_data() throws Exception {
        String urlString = "https://api.thingspeak.com/channels/365072/feeds.json?api_key=ZJAGHCE3DO174L1Z&results=2";
        City city = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683, 30.0, 18.43, 60.55, 60.55, 60.55);
        city.updateData(urlString);
        assertEquals(100, city.getOzoneO3(),0);
        assertEquals(20, city.getNitrogenDioxideNO2(),0);
        assertEquals(40, city.getCarbonMonoxideCO(),0);
        assertEquals(18, city.getTemperature(),0);
        assertEquals(30, city.getHumidity(),0);
    }
}