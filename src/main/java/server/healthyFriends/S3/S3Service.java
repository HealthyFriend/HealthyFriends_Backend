package server.healthyFriends.S3;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;
    public String uploadExample(String name, MultipartFile file) {
        String url = "";
        if(file != null)
        {
            url = s3Uploader.S3upload(file, "static/hf-images");
        }
        return url;
    }

}
