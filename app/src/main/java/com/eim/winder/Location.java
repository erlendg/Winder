package com.eim.winder;

/**
 * Created by Erlend on 01.02.2016.
 */

public interface Location {
    public String getName();
    public String getType();
    public String getCountry();
    public String getTimezoneID();
    public int utcOffsetMinutes();


}
