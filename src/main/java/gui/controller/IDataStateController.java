package gui.controller;

import gui.Main;
import gui.state.DataState;

public interface IDataStateController {
    void setState(Main mainApp, DataState state);
}
