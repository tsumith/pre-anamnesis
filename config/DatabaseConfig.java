package com.sumith.jfs.PaAnaBot.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;
@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:C:/sumith/projects/pre-anamnesis_jfs/PaAnaBot/paanabot.db");
        return dataSource;
    }
}
