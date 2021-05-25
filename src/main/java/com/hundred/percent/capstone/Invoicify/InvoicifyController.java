package com.hundred.percent.capstone.Invoicify;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class InvoicifyController implements ErrorController {
    @GetMapping("/")
    @ResponseStatus(HttpStatus.FOUND)
    public void redirectToDocs(HttpServletResponse httpServletResponse){
        //httpServletResponse.setHeader("Location", "/docs/index.html");
        httpServletResponse.setHeader("Location", "/home.html");
    }


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // TODO: log error details here

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // display specific error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                //return "404";

                return "<!DOCTYPE html>\n" +
                        "<html style=\"background-color:LightGray;\" lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:black\">INVPOCIFY: A CAPSTONE PROJECT</p></b></center>\n" +
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
                        "</html>";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                //return "500";
                return "<!DOCTYPE html>\n" +
                        "<html style=\"background-color:LightGray;\" lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:black\">INVPOCIFY: A CAPSTONE PROJECT</p></b></center>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:red\">500: INTERNAL SERVER ERROR</p></b></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:blue\">Uh oh, it's not you...it's us.</code></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:blue\">One of our developers must be punished for this unacceptable failure.</code></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:green\">In a forgiving mood? Let them all keep their jobs.</code></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:red\">Return to the </code><a href=\"https://invoicify100percent.herokuapp.com/\">homepage</a></center>\n" +
                        "\n" +
                        "</html>";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                //return "error";
                return "<!DOCTYPE html>\n" +
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
                        "<center><b><p style=\"color:black\">INVPOCIFY: A CAPSTONE PROJECT</p></b></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:blue\">Uh oh, something unexpected happened.</code></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:red\">Let us start over </code><a href=\"https://invoicify100percent.herokuapp.com/\">homepage</a></center>\n" +
                        "\n" +
                        "\n" +
                        "</html>";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                //return "400";
                return "<!DOCTYPE html>\n" +
                        "<html style=\"background-color:LightGray;\" lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:black\">INVPOCIFY: A CAPSTONE PROJECT</p></b></center>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:red\">400: BAD REQUEST</p></b></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:blue\">Uh oh, seems like you have sent bad request.</code></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:green\">Let's start over. But this time send me some good request, that I can understand.</code></center>\n" +
                        "</br>\n" +
                        "<center><a href=\"https://invoicify100percent.herokuapp.com/\">HOMEPAGE</a></center>\n" +
                        "</html>";

            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                //return "401";
                return "<!DOCTYPE html>\n" +
                        "<html style=\"background-color:LightGray;\" lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:black\">INVPOCIFY: A CAPSTONE PROJECT</p></b></center>\n" +
                        "</br>\n" +
                        "<center><b><p style=\"color:red\">401: NOT AUTHORIZED</p></b></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:blue\">I'll need to see your I.D. first</code></center>\n" +
                        "</br>\n" +
                        "<center><code style=\"color:green\">Return to the </code><a href=\"https://invoicify100percent.herokuapp.com/\">homepage</a></center>\n" +
                        "\n" +
                        "</html>";
            }
        }
        // display generic error
        //return "error";
        return "<!DOCTYPE html>\n" +
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
                "</html>";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
