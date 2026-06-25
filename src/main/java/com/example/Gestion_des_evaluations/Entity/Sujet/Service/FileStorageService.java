package com.example.Gestion_des_evaluations.Entity.Sujet.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");

    public FileStorageService() throws IOException {
        Files.createDirectories(uploadDir);
    }

    public String store(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            String storedName = UUID.randomUUID() + extension;
            Path destination = uploadDir.resolve(storedName).normalize().toAbsolutePath();

            if (!destination.startsWith(uploadDir.toAbsolutePath())) {
                throw new RuntimeException("Chemin de fichier invalide");
            }

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return storedName;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }
}