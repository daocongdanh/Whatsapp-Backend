package com.example.whatsapp.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.whatsapp.dtos.TestDTO;
import com.example.whatsapp.responses.ResponseSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final Cloudinary cloudinary;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@ModelAttribute TestDTO testDTO) throws IOException {
        Map r = cloudinary.uploader().upload(testDTO.getFile().getBytes(),
                ObjectUtils.asMap("resource_type","auto"));
        String image = (String)r.get("secure_url");
        return ResponseEntity.ok().body(ResponseSuccess.builder()
                .message("Success")
                .status(HttpStatus.OK.value())
                .data(image)
                .build());
    }
}
