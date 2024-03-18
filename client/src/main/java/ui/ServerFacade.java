package ui;

import requests.*;

public class ServerFacade {

    public static String logIn(String username, String password){

        return "Working on it";
    }

    public static String logOut(){
        return "false";
    }

    public static String clear(){
        return "false";
    }

    public static String register(String username, String password, String email){
        RegisterRequest request = new RegisterRequest(username, password, email);
        return "false";
    }

    public static String joinGame(){
        return "false";
    }

    public static void createGame(){

    }

    public static void listGames(){

    }

}
