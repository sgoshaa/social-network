package ru.skillbox.diplom.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.enums.Errors;
import ru.skillbox.diplom.model.api.enums.FileType;
import ru.skillbox.diplom.model.api.response.StorageResponse;
import ru.skillbox.diplom.repository.PersonRepository;
import ru.skillbox.diplom.util.UserUtility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class StorageService {

    private final PersonRepository personRepository;

    @Value("${CLOUD_NAME}")
    private String CLOUD_NAME;
    @Value("${API_KEY}")
    private String API_KEY;
    @Value("${API_SECRET}")
    private String API_SECRET;


    @Autowired
    public StorageService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public StorageResponse upload(String type, MultipartFile file) {

        User user = UserUtility.getUser();
        if (user != null) {
            Person person = personRepository.getOne(user.getId());

            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            byte[] res = getBytes(file, ext);
            String url = cloudinary(res);

            Map map = new HashMap<>();
            map.put("id", url);
            map.put("ownerId", person.getId());
            map.put("fileName", file.getOriginalFilename());
            map.put("relativeFilePath", type);
            map.put("rawFileURL", new File(file.getOriginalFilename()).getAbsolutePath());
            map.put("fileFormat", FilenameUtils.getExtension(file.getOriginalFilename()));
            map.put("bytes", (int) file.getSize());
            map.put("fileType", FileType.IMAGE);
            map.put("createdAt", System.currentTimeMillis());

            StorageResponse response = new StorageResponse();
            response.setData(map);
            response.setError(Errors.string);
            response.setTimestamp(System.currentTimeMillis());
            return response;
        }
        return null;
    }

    private static byte[] getBytes(MultipartFile image, String ext) {
        if (ext == null) {
            return null;
        }
        if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png")) {
            try {
                byte[] bytes = image.getBytes();
                if (bytes.length > 1048576) {
                    return null;
                }
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String cloudinary(byte[] bytes) {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET,
                "secure", true));
        String url = "";
        try {
            url = cloudinary.uploader().upload(bytes, ObjectUtils.asMap(
                    "transformation", new Transformation()
                            .gravity("face").height(1000).width(1000).crop("fill").chain()
                            .radius("max").chain().width(150).crop("scale")
            )).get("url").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

}
