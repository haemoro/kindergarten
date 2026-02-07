package com.sotti.kindergarten

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class KindergartenApplication

fun main(args: Array<String>) {
    System.setProperty("java.net.preferIPv4Stack", "true")
    runApplication<KindergartenApplication>(*args)
}
