package vn.ptit.moviebooking.ticket.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MapperConfiguration.class);

    @Bean
    public ObjectMapper objectMapper() {
        log.debug("Configure default ObjectMapper");
        ObjectMapper objectMapper = new ObjectMapper();

        // Avoid errors when encountering undefined properties in JSON that are not present in the Java class being converted
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Do not serialize (convert to JSON) fields with empty or null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        objectMapper.registerModule(new JavaTimeModule()); // To support Instant datatype of Java 8
        objectMapper.registerModule(new Jdk8Module());

        return objectMapper;
    }
}
