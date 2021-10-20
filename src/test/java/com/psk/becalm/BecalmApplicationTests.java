package com.psk.becalm;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


class BecalmApplicationTests {

    @Test
    void contextLoads() {


        LocalDateTime localDateTime = LocalDateTime.parse("1634468981377", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println("s");


    }

}
