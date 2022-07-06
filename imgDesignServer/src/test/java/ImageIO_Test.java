import javax.imageio.ImageIO;
import java.util.Arrays;

public class ImageIO_Test {
    public static void main(String[] args) {
        String readFormats[] = ImageIO.getReaderFormatNames();
        String writeFormats[] = ImageIO.getWriterFormatNames();
        System.out.println("支持的Readers: " + Arrays.asList(readFormats));
        System.out.println("支持的Writers: " + Arrays.asList(writeFormats));
    }
}

