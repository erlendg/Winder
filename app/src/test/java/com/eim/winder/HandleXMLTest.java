package com.eim.winder;

/**
 * Created by Erlend on 15.02.2016.
 */
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class HandleXMLTest {
    HandleXML testObj;
    String url;
    @Before
    public void setUp() {
        System.out.println("@Before - setUp()");
        url = "http://www.yr.no/sted/Norge/Østfold/Moss/Kambo/varsel.xml";
        testObj = new HandleXML(url);
    }

    @Test
    public void checkIfHandleXMLGetsCreated() {
        System.out.println("@Test - checkIfHandleXMLGetsCreated()");
        assertNotNull(testObj);
    }

    @Test
    public void checkIfHandleXMLMethodFetchXMLDoesWhatWasIntended() {
        System.out.println("@Test - checkIfHandleXMLMethodFetchXMLDoesWhatWasIntended()");
        testObj.fetchXML();
        System.out.println(testObj.getName());
        assertEquals("Kambo",testObj.getName());
    }


}
