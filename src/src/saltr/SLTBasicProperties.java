/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

public class SLTBasicProperties {
    private int age;
    private String gender;         //Gender "F", "M", "female", "male"

    private String appVersion;     // Version of the client app, e.g. 4.1.1

    private String systemName;     //The name of the OS the current device is running. E.g. iPhone OS.
    private String systemVersion;  //The version number of the OS the current device is running. E.g. 6.0.

    private String browserName;    //The name of the browser the current device is running. E.g. Chrome.
    private String browserVersion; //The version number of the browser the current device is running. E.g. 17.0.

    private String deviceName;     //A human-readable name representing the device.
    private String deviceType;     //The Type name of the device. E.g. iPad.

    private String locale;         //The current locale the user is in. E.g. enUS.

    private String country;        //The country the user is in, specified by ISO 2-letter code. E.g. US for United States.
    //Set to (locate) to detect the country based on the IP address of the caller.

    private String region;         //The region (state) the user is in. E.g. ca for California.
    //Set to (locate) to detect the region based on the IP address of the caller.

    private String city;           //The city the user is in. E.g. San Francisco.
    //Set to (locate) to detect the city based on the IP address of the caller.

    private String location;       //The location (latitude/longitude) of the user. E.g. 37.775,-122.4183.
    //Set to (locate) to detect the location based on the IP address of the caller.


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
