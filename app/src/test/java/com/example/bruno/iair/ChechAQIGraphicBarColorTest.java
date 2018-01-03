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
public class ChechAQIGraphicBarColorTest {
    @Test
    public void gets_data() throws Exception {
        //TODO: this

        City city = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);
        City city2 = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);
        City city3 = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);
        City city4 = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);
        City city5 = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);
        City city6 = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);

        assertEquals("Green", city.getAQI());
        assertEquals("Yellow", city2.getAQI());
        assertEquals("Orange", city3.getAQI());
        assertEquals("Light red", city4.getAQI());
        assertEquals("Purple", city5.getAQI());
        assertEquals("Dark red", city6.getAQI());

    }
}