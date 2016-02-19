package com.eim.winder;


/**
 * Created by Erlend on 01.02.2016.
 */
public class TabularInfo {
    private String from = "from";
    private String to = "to";
    private int period = 0;

    private int symbolNumber = 0;
    private int symbolNumberEx = 0;
    private String symbolName = "symbolName";
    private String symbolVar = "symbolVar";

    private double precipitationValue = 0.0;
    private double precipitationMin = 0.0;
    private double precipitationMax = 0.0;

    private double windDirectionDeg = 0.0;
    private String windDirectionCode = "windDirectionCode";
    private String windDirectionName = "windDirectionName";
    private double windSpeed = 0.0;
    private String windSpeedName = "windSpeedName";

    private String temperatureUnit = "celsius";
    private double temperatureValue = 0.0;

    private String pressureUnit = "pressureUnit";
    private double pressureValue = 0.0;

    private int counter = 0;

    public TabularInfo() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setTo(String to) {
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

    public double getPrecipitationValue() {
        return precipitationValue;
    }

    public void setPrecipitationValue(double precipitationValue) {
        this.precipitationValue = precipitationValue;
    }

    public double getPrecipitationMin() {
        return precipitationMin;
    }

    public void setPrecipitationMin(double precipitationMin) {
        this.precipitationMin = precipitationMin;
    }

    public double getPrecipitationMax() {
        return precipitationMax;
    }

    public void setPrecipitationMax(double precipitationMax) {
        this.precipitationMax = precipitationMax;
    }

    public double getWindDirectionDeg() {
        return windDirectionDeg;
    }

    public void setWindDirectionDeg(double windDirectionDeg) {
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

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
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

    public double getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(double temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(String pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public double getPressureValue() {
        return pressureValue;
    }

    public void setPressureValue(double pressureValue) {
        this.pressureValue = pressureValue;
    }

    @Override
    public String toString() {
        return "TabularInfo{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", period=" + period +
                ", counter =" + counter +
                ", symbolNumber=" + symbolNumber +
                ", symbolNumberEx=" + symbolNumberEx +
                ", symbolName='" + symbolName + '\'' +
                ", symbolVar='" + symbolVar + '\'' +
                ", precipitationValue=" + precipitationValue +
                ", precipitationMin=" + precipitationMin +
                ", precipitationMax=" + precipitationMax +
                ", windDirectionDeg=" + windDirectionDeg +
                ", windDirectionCode='" + windDirectionCode + '\'' +
                ", windDirectionName='" + windDirectionName + '\'' +
                ", windSpeed=" + windSpeed +
                ", windSpeedName='" + windSpeedName + '\'' +
                ", temperatureUnit='" + temperatureUnit + '\'' +
                ", temperatureValue=" + temperatureValue +
                ", pressureUnit='" + pressureUnit + '\'' +
                ", pressureValue=" + pressureValue +
                '}';
    }
}
