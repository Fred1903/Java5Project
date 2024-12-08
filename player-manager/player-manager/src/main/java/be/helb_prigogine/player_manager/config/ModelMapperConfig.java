package be.helb_prigogine.player_manager.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {//On doit ajt cette classe pr pouvoir utiliser ModelMapper dans PlayerService afin d'éviter trop de code manuel

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

