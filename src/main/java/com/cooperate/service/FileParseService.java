package com.cooperate.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileParseService {

    void parsePerson(MultipartFile file);

    void parseGarag(MultipartFile file);

}
