package com.eim.winder;

import java.util.Date;

/**
 * Created by Erlend on 01.02.2016.
 */
public class TabularInfo {
    private Date from;
    private Date to;
    private int period;
    private int symbolNumber;
    private int symbolNumberEx;
    private String symbolName;
    private String symbolVar;
    private float precipitationValue;
    private float precipitationMin;
    private float precipitationMax;
    private float windDirectionDeg;
    private String windDirectionCode;
    private String windDirectionName;
    private float windSpeed;
    private String windSpeedName;
    private String temperatureUnit;
    private float temberatureValue;
    private String pressureUnit;
    private float pressureValue;

    public TabularInfo(Date from, Date to, int period, int symbolNumber, int symbolNumberEx, String symbolName, String symbolVar, float precipitationValue, float precipitationMin, float precipitationMax, float windDirectionDeg, String windDirectionCode, String windDirectionName, float windSpeed, String windSpeedName, String temperatureUnit, float temberatureValue, String pressureUnit, float pressureValue) {
        this.from = from;
        this.to = to;
        this.period = period;
        this.symbolNumber = symbolNumber;
        this.symbolNumberEx = symbolNumberEx;
        this.symbolName = symbolName;
        this.symbolVar = symbolVar;
        this.precipitationValue = precipitationValue;
        this.precipitationMin = precipitationMin;
        this.precipitationMax = precipitationMax;
        this.windDirectionDeg = windDirectionDeg;
        this.windDirectionCode = windDirectionCode;
        this.windDirectionName = windDirectionName;
        this.windSpeed = windSpeed;
        this.windSpeedName = windSpeedName;
        this.temperatureUnit = temperatureUnit;
        this.temberatureValue = temberatureValue;
        this.pressureUnit = pressureUnit;
        this.pressureValue = pressureValue;
    }

    public TabularInfo() {
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getSymbolNumber() {
        return symbolNumber;
    }

    public void setSymbolNumber(int symbolNumber) {
        this.symbolNumber = symbolNumber;
    }

    public int getSymbolNumberEx() {
        return symbolNumberEx;
    }

    public void setSymbolNumberEx(int symbolNumberEx) {
        this.symbolNumberEx = symbolNumberEx;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getSymbolVar() {
        return symbolVar;
    }

    public void setSymbolVar(String symbolVar) {
        this.symbolVar = symbolVar;
    }

    public float getPrecipitationValue() {
        return precipitationValue;
    }

    public void setPrecipitationValue(float precipitationValue) {
        this.precipitationValue = precipitationValue;
    }

    public float getPrecipitationMin() {
        return precipitationMin;
    }

    public void setPrecipitationMin(float precipitationMin) {
        this.precipitationMin = precipitationMin;
    }

    public float getPrecipitationMax() {
        return precipitationMax;
    }

    public void setPrecipitationMax(float precipitationMax) {
        this.precipitationMax = precipitationMax;
    }

    public float getWindDirectionDeg() {
        return windDirectionDeg;
    }

    public void setWindDirectionDeg(float windDirectionDeg) {
        this.windDirectionDeg = windDirectionDeg;
    }

    public String getWindDirectionCode() {
        return windDirectionCode;
    }

    public void setWindDirectionCode(String windDirectionCode) {
        this.windDirectionCode = windDirectionCode;
    }

    public String getWindDirectionName() {
        return windDirectionName;
    }

    public void setWindDirectionName(String windDirectionName) {
        this.windDirectionName = windDirectionName;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindSpeedName() {
        return windSpeedName;
    }

    public void setWindSpeedName(String windSpeedName) {
        this.windSpeedName = windSpeedName;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public float getTemberatureValue() {
        return temberatureValue;
    }

    public void setTemberatureValue(float temberatureValue) {
        this.temberatureValue = temberatureValue;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(String pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public float getPressureValue() {
        return pressureValue;
    }

    public void setPressureValue(float pressureValue) {
        this.pressureValue = pressureValue;
    }
}
