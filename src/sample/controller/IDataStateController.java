package sample.controller;

import sample.Main;
import sample.state.DataState;

public interface IDataStateController {
    void setState(Main mainApp, DataState state);
}
