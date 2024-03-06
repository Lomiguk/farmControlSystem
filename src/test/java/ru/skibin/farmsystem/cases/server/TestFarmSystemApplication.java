package ru.skibin.farmsystem.cases.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.skibin.farmsystem.FarmSystemApplication;

@TestConfiguration(proxyBeanMethods = false)
public class TestFarmSystemApplication {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    }

    public static void main(String[] args) {
        SpringApplication.from(FarmSystemApplication::main).with(TestFarmSystemApplication.class).run(args);
    }

}
