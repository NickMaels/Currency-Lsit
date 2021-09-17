package com.currency.price.controllers;


import com.currency.price.parsers.Currency;
import com.currency.price.parsers.UniversalParser;
import com.currency.price.parsers.nbm.ParserXML;
import com.currency.price.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MICBController {

    private final UniversalParser universalParser;

    private final CurrencyService currencyService;

    private final String currentDate = ParserXML.simpleDateFormat.format(new Date());

    @Value("${micb.title}")
    private String bankName;

    @RequestMapping(value = "/save/MICB")
    public String saveCurrency(@Value("${micb.link}") String link,
                               @Value("${micb.tag}") String tagName) throws IOException {


        List<Currency> micbList = universalParser.getCurrency(link, tagName).stream()
                .peek(x -> x.setBank("MICB"))
                .peek(x -> x.setDate(currentDate))
                .collect(Collectors.toList());


        currencyService.saveCurrency(micbList, bankName);

        return "redirect:/currency/MICB";
    }
}