package ir.afarinesh.realhope.shares.services;

import ir.afarinesh.realhope.shares.services.exceptions.CreateFileException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileManagementService {

    public String pathSeparator(){
        return "\\";
    }

    public void createFile(String path, String fileName, String content) throws CreateFileException {
        System.out.println("Path: " + path);
        boolean isDirectoryAvailable;
        File directory = new File(path);
        if (!directory.exists()) {
            System.out.println("Directory not exist ...");
            isDirectoryAvailable = directory.mkdirs();
            System.out.println("isDirectoryAvailable = " + isDirectoryAvailable);
        } else {
            isDirectoryAvailable = true;
        }

        if (isDirectoryAvailable) {
            File file = new File(directory.getAbsolutePath() + this.pathSeparator() + fileName);
            try {
                if (file.exists()) {
                    file.delete();
                }
                if (file.createNewFile()) {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(content);
                    fileWriter.close();
                } else {
                    throw new CreateFileException("The Create of the new file is failed");
                }
            } catch (IOException e) {
                throw new CreateFileException(e.getMessage());
            }
        } else {
            throw new CreateFileException("Directory is not available.");
        }
    }
}
