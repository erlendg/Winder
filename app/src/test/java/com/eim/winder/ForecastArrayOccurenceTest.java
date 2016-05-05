package com.eim.winder;

import com.eim.winder.db.Forecast;
import com.eim.winder.xml.CompareAXService;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Created by Erlend on 05.05.2016.
 */
public class ForecastArrayOccurenceTest {
    ArrayList<Forecast> oldList1;
    ArrayList<Forecast> oldList2;
    ArrayList<Forecast> oldList3;
    ArrayList<Forecast> oldList4;
    ArrayList<Forecast> oldList5;
    ArrayList<Forecast> newList;
    CompareAXService div;

    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code
        System.out.println("@BeforeClass - oneTimeSetUp");
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        System.out.println("@AfterClass - oneTimeTearDown");
    }

    @Before
    public void setUp() {
        oldList1 = new ArrayList<>();
            for (int i=1;i<10;i++){
                String date = "0" +i +"/05/16";
                oldList1.add(new Forecast(date));
            }
        oldList2= new ArrayList<>();
            for (int i=4;i<10;i++){
                String date = "1" +i +"/05/16";
                oldList2.add(new Forecast(date));
            }
        oldList3= new ArrayList<>();
        for (int i=0;i<6;i++){
            String date = "1" +i +"/05/16";
            oldList3.add(new Forecast(date));
        }
        oldList4= new ArrayList<>();
            oldList4.add(new Forecast("08/05/16"));
            oldList4.add(new Forecast("09/05/16"));
            oldList4.add(new Forecast("10/05/16"));
            oldList4.add(new Forecast("11/05/16"));
            oldList4.add(new Forecast("13/05/16"));
            oldList4.add(new Forecast("21/05/16"));

        oldList5= new ArrayList<>();
            for (int i=0;i<10;i++){
                String date = "1" +i +"/05/16";
                oldList5.add(new Forecast(date));
            }
        newList= new ArrayList<>();
            for (int i=0;i<10;i++){
                String date = "1" +i +"/05/16";
                newList.add(new Forecast(date));
            }

        div = new CompareAXService();
        System.out.println("@Before - setUp");
    }

    @After
    public void tearDown() {
        oldList1.clear();
        oldList2.clear();
        oldList3.clear();
        oldList4.clear();
        oldList5.clear();
        newList.clear();
        System.out.println("@After - tearDown");
    }

    /*@Test
    public void testEmptyCollection() {
        assertTrue(collection.isEmpty());
        System.out.println("@Test - testEmptyCollection");
    }*/

    @Test
    public void testResultLike1() {
        //this means the entire new list occurs after the old list
        assertEquals(1, div.findIfNewEvents(oldList1, newList));
        System.out.println("@Test - testResultLike1");
    }

    @Test
    public void testResultLike2() throws Exception{
        //this means earlier events have been discovered
        assertEquals(2, div.findIfNewEvents(oldList2, newList));

        System.out.println("@Test - testResultLike2");
    }

    @Test
    public void testResultLike3() throws Exception{
        //this means later events have been discovered
        assertEquals(3, div.findIfNewEvents(oldList3, newList));

        System.out.println("@Test - testResultLike3");
    }

    @Test
    public void testResultLike4() throws Exception {
        //this means new events have been found inbetween
        assertEquals(4, div.findIfNewEvents(oldList4, newList));

        System.out.println("@Test - testResultLike4");
    }

    @Test
    public void testResultLikeNegative1() throws Exception {
        //no new events discovered, no new notification:
        assertEquals(-1, div.findIfNewEvents(oldList5, newList));

        System.out.println("@Test - testResultLikeNegative1");
    }
}
