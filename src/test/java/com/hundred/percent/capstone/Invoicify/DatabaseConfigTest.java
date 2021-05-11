package com.hundred.percent.capstone.Invoicify;

import com.hundred.percent.capstone.Invoicify.config.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseConfigTest {


    @Test
    void dummyDatabaseConfigTest(){
        DatabaseConfig databaseConfig = new DatabaseConfig();
    }
}
