package com.hundred.percent.capstone.Invoicify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InvoicifyApplicationIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void redirectRootToHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/home.html"));
    }

    @Test
    void redirectToErrorPage() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(jsonPath("$").value("<!DOCTYPE html>\n" +
                        "<html style=\"background-color:LightGray;\" lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:black\">INVOICIFY: A CAPSTONE PROJECT</p></b></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:blue\">Uh oh, something unexpected happened.</code></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:red\">Let us start over </code><a href=\"https://invoicify100percent.herokuapp.com/\">homepage</a></center>\n" +
                        "\n" +
                        "\n" +
                        "</html>"))
                .andExpect(status().isOk());
    }
}
