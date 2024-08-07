package com.example.whatsapp.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dwmvgaude",
                "api_key", "772562857818322",
                "api_secret", "uPuFqI9PhfWMg40A7ySIVSJgpH4",
                "secure",true
        ));
    }
}
