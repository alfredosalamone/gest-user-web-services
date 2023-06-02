package com.xantrix.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dtos.InfoMsg;
import com.xantrix.webapp.exceptions.BindingException;
import com.xantrix.webapp.exceptions.DuplicateException;
import com.xantrix.webapp.exceptions.NotFoundException;
import com.xantrix.webapp.models.Utenti;
import com.xantrix.webapp.services.UtentiService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

;
;

@RestController
@RequestMapping(value = "/api/utenti")
@Log
public class UtentiController {

    @Autowired
    private UtentiService utentiService;

    @Autowired
    private ResourceBundleMessageSource errMessage;

    @GetMapping(value = "/cerca/user/{userId}", produces = "application/json")
    public ResponseEntity<Utenti> getUserByUserId(@PathVariable("userId") String userId) throws NotFoundException {
        log.info("Otteniamo l'utente con userId " + userId);

        Utenti ute = utentiService.getUtenteByUserId(userId);

        if (ute == null) {
            String errMsg = String.format("L'utente cod userId %s non è stato trovato", userId);
            log.warning(errMsg);
            throw new NotFoundException(errMsg);
        } else
            return new ResponseEntity<Utenti>(ute, HttpStatus.OK);
    }

    @PostMapping(value = "/inserisci", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<InfoMsg> createUser(@Valid @RequestBody Utenti ute, BindingResult bindingResult) {
        log.info("Inseriamo l'utente avente userId " + ute.getUserId());

        //1. controllo che la validazione dei dati sia andata a buon fine
        if (bindingResult.hasErrors()) {
            String errMsg = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
            log.warning(errMsg);
            throw new BindingException(errMsg);
        }

        //2. devo controllare che l'utente che si sta tendando d'inserire non esista già in anagrafica

        Utenti checkUte = utentiService.getUtenteByUserId(ute.getUserId());
        if (checkUte != null) {
            String errMsg = String.format("L'utente con userId %s è già presente in anagrafica", ute.getUserId());
            log.warning(errMsg);
            throw new DuplicateException();
        }

        //3. Se arrivo qui posso inserire il nuovo utente

        utentiService.createUser(ute);

        //4: creiamo il valore di ritorno del metodo
        return new ResponseEntity<InfoMsg>(new InfoMsg(LocalDate.now(), "Inserimento utente eseguita con successo"), HttpStatus.CREATED);
    }

    @GetMapping(value = "/cerca/{filter}", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<List<Utenti>> getAllUsers(@PathVariable("filter") String filter) {
        log.info("Otteniamo la lista di tutti gli utenti");

        if (!"tutti".equals(filter)) {
            String errMsg = "Impossibile recuperare la lista di tutti gli utenti perchè il filter in input è diverso da tutti";
            log.warning(errMsg);
            throw new NotFoundException(errMsg);
        }

        List<Utenti> utenti = utentiService.getAllUsers();
        return new ResponseEntity<>(utenti, HttpStatus.OK);
    }

    @RequestMapping(value = "/elimina/{userId}", method = RequestMethod.DELETE, produces = "application/json")
    @SneakyThrows
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId) {
        log.info("Eliminiamo l'utente con userId " + userId);

        //1. verificare la presenza in anagrafica dell'utente da eliminare
        Utenti ute = utentiService.getUtenteByUserId(userId);
        if (ute == null) {
            String errMsg = String.format("L'utente di userId %s non è presente in anagrafica", userId);
            log.warning(errMsg);
            throw new NotFoundException(errMsg);
        }

        utentiService.deleteUser(userId);

        //creazione sistema per generazione messaggio di ritorno
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        responseNode.put("code", HttpStatus.OK.toString());
        responseNode.put("message", "Eliminazione utente " + userId + " eseguita con successo");

        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
    }

}
