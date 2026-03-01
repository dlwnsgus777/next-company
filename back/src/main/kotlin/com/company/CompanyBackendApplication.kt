package com.company

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CompanyBackendApplication

fun main(args: Array<String>) {
	runApplication<CompanyBackendApplication>(*args)
}
