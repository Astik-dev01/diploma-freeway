package com.example.freeway.util;

import com.example.freeway.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class FileUtils {
    public final String basePath = System.getProperty("user.dir") + File.separator + "files";
//    @Value("${server.servlet.context-path}")
//    private String contextPath;
//
//    @Value("${server.port}")
//    private String serverPort;
//
//    @Value("${server.address}")
//    private String serverAddress;

    @Value("${test.mode}")
    private boolean testMode;

    @Value("${file.base.url.prod}")
    private String prodBaseUrl;

    @Value("${file.base.url.test}")
    private String testBaseUrl;

    public String buildUrl(String path) {
        String base = testMode ? testBaseUrl : prodBaseUrl;
        return base + path.replace("\\", "/");
    }

    public String saveMultipartFileWithResize(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileExt = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                fileExt = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            }

            checkFileExtOrException(fileExt);

            String baseFileName = DateUtils.formatToString(new Date(), DateUtils.FORMAT_DD_MM_YYYY_HH_MM_SS_SSS_FOR_PATH);
            String fullFileName = baseFileName + "." + fileExt;

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String formattedDate = LocalDate.now().format(formatter);
            String[] dateParts = formattedDate.split("/");

            String packagePath = String.format("%s/%s/%s", dateParts[0], dateParts[1], dateParts[2]);
            String fullPath = basePath + File.separator + packagePath;

            File directory = new File(fullPath);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new BadRequestException("error.create_directory");
            }

            File targetFile = new File(directory, fullFileName);
            file.transferTo(targetFile);

            return String.format("%s/%s", packagePath, fullFileName);

        } catch (IOException e) {
            throw new BadRequestException("error.file_save_failed: " + e.getMessage());
        }
    }

    public void deleteFileIfExists(String relativePath) {
        try {
            String fullPath = basePath + File.separator + relativePath;
            File file = new File(fullPath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            throw new BadRequestException("error.file_deleting_failed: " + e.getMessage());
        }
    }

    /**
     * Метод для сохранения файла на диск в папке по дате (год/месяц/день)
     *
     * @param fileBody Тело файла в формате Base64
     * @param fileExt  Расширение файла
     */
    public String saveBase64File(String fileBody, String fileExt) {
        try {
            checkFileExtOrException(fileExt);

            // Декодируем тело файла из Base64
            byte[] fileContent = Base64.getDecoder().decode(fileBody);

            // Формируем имя файла с расширением
            String fullFileName = DateUtils.formatToString(new Date(), DateUtils.FORMAT_DD_MM_YYYY_HH_MM_SS_SSS_FOR_PATH) + "." + fileExt;

            // Получаем текущую дату и форматируем её в "год/месяц/день"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String formattedDate = LocalDate.now().format(formatter);

            // Формируем путь для создания директорий (год/месяц/день)
            String[] dateParts = formattedDate.split("/");

            String yearFolder = dateParts[0];   // Год
            String monthFolder = dateParts[1];  // Месяц
            String dayFolder = dateParts[2];    // День

            // Формируем полный путь для файла
            String packagePath = String.format("%s/%s/%s", yearFolder, monthFolder, dayFolder);
            String fullPath = basePath + File.separator + packagePath;

            // Проверяем и создаём директорию, если её не существует
            File directory = new File(fullPath);
            if (!directory.exists()) {
                boolean dirsCreated = directory.mkdirs();
                if (!dirsCreated) {
                    throw new BadRequestException("error.create_directory"); //TODO Добавить локализацию "Ошибка создания папки"
                }
            }

            File file = new File(directory, fullFileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileContent);
            }

            return String.format("%s/%s", packagePath, fullFileName);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public String saveMultipartFile(MultipartFile file) {
        try {
            // Получаем расширение файла
            String originalFileName = file.getOriginalFilename();
            String fileExt = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                fileExt = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            }

            // Проверка допустимых расширений
            checkFileExtOrException(fileExt);

            // Генерация имени файла
            String fullFileName = DateUtils.formatToString(new Date(), DateUtils.FORMAT_DD_MM_YYYY_HH_MM_SS_SSS_FOR_PATH) + "." + fileExt;

            // Формируем путь по дате
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String formattedDate = LocalDate.now().format(formatter);

            String[] dateParts = formattedDate.split("/");

            String yearFolder = dateParts[0];
            String monthFolder = dateParts[1];
            String dayFolder = dateParts[2];

            String packagePath = String.format("%s/%s/%s", yearFolder, monthFolder, dayFolder);
            String fullPath = basePath + File.separator + packagePath;

            File directory = new File(fullPath);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new BadRequestException("error.create_directory");
            }

            // Сохраняем файл
            File savedFile = new File(directory, fullFileName);
            file.transferTo(savedFile);

            // Возвращаем относительный путь к файлу
            return String.format("%s/%s", packagePath, fullFileName);
        } catch (IOException e) {
            throw new BadRequestException("error.file_save_failed: " + e.getMessage());
        }
    }

    /**
     * Метод для получения файла по его пути
     *
     * @param filePathLocal Путь файла, включая расширение
     * @return Файл как ресурс
     */
    public String generateFileDownloadLink(String filePathLocal, HttpServletRequest httpServletRequest) {
        try {
            String packagePath = File.separator + filePathLocal;
            String filePath = basePath + packagePath;
            File file = new File(filePath);

            if (!file.exists()) {
                throw new BadRequestException("error.file_not_found"); //TODO Добавить локализацию "Файл не найден"
            }

            return String.format("files/%s", filePathLocal.replace(File.separator, "/"));
//            return String.format("http://%s:%s%s/files/%s",
//                    serverAddress,
//                    serverPort,
//                    contextPath,
//                    filePathLocal.replace(File.separator, "/"));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    /**
     * Метод для проверки расширение файла
     *
     * @param fileExt Расширение
     */
//    private static void checkFileExtOrException(String fileExt) {
//        if (!fileExt.equalsIgnoreCase("png") &&
//                !fileExt.equalsIgnoreCase("jpg") &&
//                !fileExt.equalsIgnoreCase("jpeg") &&
//                !fileExt.equalsIgnoreCase("pdf") &&) {
//            !fileExt.equalsIgnoreCase("")
//            throw new BadRequestException("error.file_body.unacceptable.format"); //TODO Добавить локализацию "Не поддерживаемый формат файла"
//        }
//    }
    private static void checkFileExtOrException(String fileExt) {
        if (fileExt == null || fileExt.isBlank()) {
            log.warn("Файл без расширения загружается");
        }
        // Разрешаем любые расширения
    }
}
