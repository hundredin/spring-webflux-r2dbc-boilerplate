package com.underbout.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UnderboutApiApplication

fun main(args: Array<String>) {
    runApplication<UnderboutApiApplication>(*args)
}
