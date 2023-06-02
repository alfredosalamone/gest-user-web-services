package com.xantrix.webapp.services;

import com.xantrix.webapp.models.Utenti;
import com.xantrix.webapp.repository.UtentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UtentiServiceImpl implements UtentiService{

    @Autowired
    private UtentiRepository utentiRepository;

    @Override
    public Utenti getUtenteByUserId(String userId) {
        return utentiRepository.findByUserId(userId);
    }

    @Override
    public void createUser(Utenti utente) {
        utentiRepository.save(utente);
    }

    @Override
    public List<Utenti> getAllUsers() {
        return utentiRepository.findAll();
    }

    @Override
    public void deleteUser(String userId) {
        utentiRepository.deleteById(userId);
    }


}
