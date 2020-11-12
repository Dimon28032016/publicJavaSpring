package org.example.web.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class UploadFileForm {

    @NotNull
    MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        if(file.getSize() != 0)
            this.file = file;
        else
            this.file = null;
    }
}
