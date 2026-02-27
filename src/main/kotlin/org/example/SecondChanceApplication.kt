package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecondChanceApplication

fun main(args: Array<String>) {
    runApplication<SecondChanceApplication>(*args)
}
