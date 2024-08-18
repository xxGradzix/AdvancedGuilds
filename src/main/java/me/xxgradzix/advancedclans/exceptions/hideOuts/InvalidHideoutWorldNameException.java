package me.xxgradzix.advancedclans.exceptions.hideOuts;

public class InvalidHideoutWorldNameException extends Exception {
    public InvalidHideoutWorldNameException() {
        super("Hideout world name should start with guild_ prefix");
    }
}
