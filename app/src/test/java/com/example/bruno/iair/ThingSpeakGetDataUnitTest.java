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
        City city = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);
        city.updateAirQualityHistory();
        assertEquals(20, city.getOzoneO3(),0);
        assertEquals(40, city.getNitrogenDioxideNO2(),0);
        assertEquals(18, city.getCarbonMonoxideCO(),0);
    }
}