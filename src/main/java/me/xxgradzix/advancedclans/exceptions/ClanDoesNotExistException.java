package me.xxgradzix.advancedclans.exceptions;

public class ClanDoesNotExistException extends Exception {
    public ClanDoesNotExistException() {
        super("Clan does not exist");
    }
}
