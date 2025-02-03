package me.xxgradzix.advancedclans.exceptions.clan;

public class PlayerDoesNotBelongToClanException extends Exception {
    public PlayerDoesNotBelongToClanException() {
        super("Player does not belong to the clan");
    }
}
