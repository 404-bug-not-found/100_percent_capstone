package com.hundred.percent.capstone.Invoicify;

import com.hundred.percent.capstone.Invoicify.utilities.ExcludeGeneratedFromJaCoCo;
import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoicifyApplication {

    @ExcludeGeneratedFromJaCoCo
    public static void main(String[] args) {
        Sentry.init(options -> {
            options.setDsn("https://e454b885c6f4445ab1b8b636b659b5ac@o710865.ingest.sentry.io/5778899");
        });
        SpringApplication.run(InvoicifyApplication.class, args);
    }

}
