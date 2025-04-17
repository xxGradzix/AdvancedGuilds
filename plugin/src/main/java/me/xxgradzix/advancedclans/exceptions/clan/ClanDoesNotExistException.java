package me.xxgradzix.advancedclans.exceptions.clan;

public class ClanDoesNotExistException extends Exception {
    public ClanDoesNotExistException() {
        super("Clan does not exist");
    }
}
