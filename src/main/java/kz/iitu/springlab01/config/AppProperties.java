package kz.iitu.springlab01.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(

        @NotBlank String owner,
        @NotBlank String group,
        @Valid Mail mail,
        @Valid LocaleSettings locale

) {

    public record Mail(

            @NotBlank @Email String from,

            @Min(1)
            @Max(10)
            @DefaultValue("3")
            int retryCount,

            @DefaultValue("5s")
            Duration timeout,

            @DefaultValue("true")
            boolean enabled

    ) {
    }

    public record LocaleSettings(

            @NotBlank
            @Pattern(regexp = "ru|kk|en")
            @DefaultValue("en")
            String defaultLocale,

            @NotEmpty
            @Size(min = 1)
            List<String> supported

    ) {
    }
}

