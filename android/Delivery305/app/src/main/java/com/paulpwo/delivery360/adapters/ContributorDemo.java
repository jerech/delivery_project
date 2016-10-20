package com.paulpwo.delivery360.adapters;


import java.util.ArrayList;

public class ContributorDemo {
    public String _timer;
    public String _time;
    public String _address;
    public String _driver;

    public String get_timer() {
        return _timer;
    }

    public void set_timer(String _timer) {
        this._timer = _timer;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public String get_driver() {
        return _driver;
    }

    public void set_driver(String _driver) {
        this._driver = _driver;
    }

    public ContributorDemo(String _timer, String _time, String _address, String _driver) {
        this._timer = _timer;
        this._time = _time;
        this._address = _address;
        this._driver = _driver;
    }

    // ============================================================================================
    // METHOD FOR ROBOSPICE
    // ============================================================================================

    @SuppressWarnings("serial")
    public static class List extends ArrayList<ContributorDemo> {
    }
}
