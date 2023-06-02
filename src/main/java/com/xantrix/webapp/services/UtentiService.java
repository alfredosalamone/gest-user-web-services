package com.xantrix.webapp.services;

import com.xantrix.webapp.models.Utenti;

import java.util.List;

public interface UtentiService {


    Utenti getUtenteByUserId(String userId);

    void createUser(Utenti utenti);

    List<Utenti> getAllUsers();

    void deleteUser(String userId);
}
