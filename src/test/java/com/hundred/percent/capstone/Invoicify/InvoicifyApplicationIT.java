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
               // .andExpect(header().stringValues("Location", "/docs/index.html"));
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

    /*@Test
    void redirectTo404Page() throws Exception {
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
                        "<center><b><p style=\"color:black\">INVOICIFY: A CAPSTONE PROJECT</p></b></center>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:red\">HTTP: <span>404</span></p></b></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:blue\"><span>this_page</span>.<em>not_found</em> = true;</code></center>\n" +
                        "</br>\n" +
                        "<center><code><span style=\"color:orange\">if (<b style=\"color:violet\">you_spelt_it_wrong</b>)</span> {<span style=\"color:green\">try_again()</span>;}</code></center>\n" +
                        "</br>\n" +
                        "<center><code><span style=\"color:orange\">else if (<b style=\"color:violet\">we_screwed_up</b>)</span> {<em>alert</em>(<i style=\"color:green\"><b>\"We're really sorry about that.\"</b></i>);\n" +
                        "    <span>window</span>.<em>location</em> = home;}</code></center>\n" +
                        "</br>\n" +
                        "<center><a href=\"https://invoicify100percent.herokuapp.com/\">HOME</a></center>\n" +
                        "\n" +
                        "</html>"))
                .andExpect(status().isOk());
    }*/

}
