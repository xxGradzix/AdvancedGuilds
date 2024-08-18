package me.xxgradzix.advancedclans.exceptions;

public class PlayerDoesNotBelongToClanException extends Exception {
    public PlayerDoesNotBelongToClanException() {
        super("Player does not belong to the clan");
    }
}
