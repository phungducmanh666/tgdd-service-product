package services.product.helper.uploader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploaderHelper {

    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Tải lên một file ảnh, tạo tên file duy nhất và lưu vào thư mục có tiền tố.
     *
     * @param photo        MultipartFile chứa dữ liệu ảnh.
     * @param prefixFolder Thư mục con để lưu trữ ảnh (ví dụ: "categories",
     *                     "products").
     * @return Tên file mới (bao gồm tiền tố thư mục) nếu tải lên thành công, ví dụ:
     *         "categories/a1b2c3d4-e5f6-4789-1234-567890abcdef.jpg".
     * @throws IOException              nếu có lỗi trong quá trình đọc/ghi file.
     * @throws IllegalArgumentException nếu file rỗng hoặc không có tên gốc.
     */
    public String UploadPhoto(MultipartFile photo, String prefixFolder) throws IOException {
        if (photo.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file.");
        }

        String originalFileName = photo.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File original name cannot be null or empty.");
        }

        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFileName.length() - 1) {
            fileExtension = originalFileName.substring(dotIndex);
        }

        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetFolder = Paths.get(uploadDir, prefixFolder);

        if (!Files.exists(targetFolder)) {
            Files.createDirectories(targetFolder);
        }

        Path targetFilePath = targetFolder.resolve(uniqueFileName);
        Files.copy(photo.getInputStream(), targetFilePath);
        return Paths.get(prefixFolder, uniqueFileName).toString().replace("\\", "/");
    }

    /**
     * Xóa một file ảnh khỏi hệ thống lưu trữ.
     *
     * @param fileName Tên file cần xóa (bao gồm tiền tố thư mục, ví dụ:
     *                 "categories/image.jpg").
     * @throws IOException              nếu có lỗi trong quá trình xóa file.
     * @throws IllegalArgumentException nếu tên file rỗng hoặc không hợp lệ.
     */
    public void DeletePhoto(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        Path filePathToDelete = Paths.get(uploadDir, fileName);

        if (Files.exists(filePathToDelete)) {
            Files.delete(filePathToDelete);
        } else {
            System.out.println("File not found, skipping deletion: " + fileName);
        }
    }

}
