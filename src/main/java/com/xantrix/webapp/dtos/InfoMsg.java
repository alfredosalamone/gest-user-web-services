package com.xantrix.webapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor // con questa notation verra generato automaticmaente il costruttore con le 2 proprietà
public class InfoMsg {

    public LocalDate date;
    public String    message;
}
