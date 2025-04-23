package com.backend.system.dto.firebase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notice implements Serializable {
    private String subject;
    private String content;
    private String imagePath;
    private Map<String, String> data;
    private List<String> registrationTokens;
}
