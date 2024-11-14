package com.project.backend.services.file;

import com.project.backend.exceptions.DataNotFoundException;
import com.project.backend.exceptions.InvalidParamException;
import com.project.backend.models.File;
import com.project.backend.models.FileFormat;
import com.project.backend.models.Student;
import com.project.backend.repositories.FileFormatRepository;
import com.project.backend.repositories.FileRepository;
import com.project.backend.repositories.StudentRepository;
import com.project.backend.responses.ResponseObject;
import com.project.backend.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Service
public class FileService implements IFileService {
    @Autowired
    private FileFormatRepository fileFormatRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private StudentRepository studentRepository;
    @Override
    public ResponseObject uploadFile(MultipartFile file) {
        ResponseObject response = new ResponseObject();
        try{
            // Get student who upload file
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userName = ((UserDetails) principal).getUsername();
            Student student = studentRepository.findByEmail(userName).orElseThrow(() -> new DataNotFoundException("User not found"));

            String fileName = file.getOriginalFilename();
            String fileSize = Long.valueOf(file.getSize()).toString();
            String fileFormatName;

            // Get format name from original file name
            if(fileName != null && fileName.contains(".")){
                fileFormatName = fileName.substring(fileName.lastIndexOf('.') + 1);
            } else throw new InvalidParamException("Invalid file name");

            String fileUrl = cloudinaryService.uploadFile(file);

            FileFormat fileFormat = fileFormatRepository.findByName(fileFormatName).orElseThrow(() -> new DataNotFoundException("File format not found"));
            LocalDateTime now = LocalDateTime.now();

            File uploadFile = new File();
            uploadFile.setStudent(student);
            uploadFile.setName(fileName);
            uploadFile.setSize(fileSize);
            uploadFile.setFileFormat(fileFormat);
            uploadFile.setUrl(fileUrl);
            uploadFile.setUploadDate(now);

            fileRepository.save(uploadFile);
            response.setData(uploadFile);
            response.setStatus(HttpStatus.OK);
            response.setMessage("Upload file successfully");
        } catch(Exception e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
