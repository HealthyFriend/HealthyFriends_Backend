package server.healthyFriends.S3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import server.healthyFriends.apiPayload.ResponseDTO;
import server.healthyFriends.apiPayload.ResponseUtil;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    @PostMapping(path="/S3-Test",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseDTO<String> uploadExample(
            @RequestPart(value = "file", required = false) MultipartFile file) {

        String url = s3Service.uploadExample(file);
        return ResponseUtil.success("업로드 성공",url);
    }



}
