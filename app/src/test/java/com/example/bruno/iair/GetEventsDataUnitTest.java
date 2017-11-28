package com.example.bruno.iair;

import com.example.bruno.iair.activities.DashBoardActivity;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.Country;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GetEventsDataUnitTest {
    @Test
    public void gets_data() throws Exception {
        City city = new City("Leiria", new Country("Portugal","PT"), 39.7495331, -8.807683);
        city.updateCityEvents();
        assertEquals("Leiria", city.getEvents().get(0).getCity());
        assertEquals("Fire", city.getEvents().get(0).getType());
    }
}