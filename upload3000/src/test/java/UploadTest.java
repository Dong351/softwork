import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import softwork.UploadMain3000;
import softwork.utils.HttpUtils;
import softwork.utils.UploadFileStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = UploadMain3000.class)
public class UploadTest {

    @Test
    public void uploadFileTest() throws FileNotFoundException {

        // 要上传的文件
        File file = new File("X:\\uploads\\4.jpg");
        UploadFileStatus fileStatus = new UploadFileStatus();
        // 上传到服务器后的文件名
        fileStatus.setFileName("3");
        // 上传到服务器的哪个位置
        fileStatus.setFilePath("/root/usr/local/webfile/softwork/");
        // 文件的大小
        fileStatus.setFileSize(file.length());
        // 文件的类型
        fileStatus.setFileType("jpg");
        fileStatus.setInputStream(new FileInputStream(file));

//        String result = HttpUtils.postFile("http://47.106.83.201/upload", fileStatus);
        String result = HttpUtils.postFile("http://49.234.239.138:3000/upload/", fileStatus);
//        String result = HttpUtils.postFile("https://clubajax.autohome.com.cn/Upload/UpImageOfBase64New?dir=image&cros=autohome.com.cn", fileStatus);
        System.out.println(result);
    }
}
