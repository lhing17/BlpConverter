package cn.gsein.blpconverter.image;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author G. Seinfeld
 * @date 2019/08/26
 */
public class BlpConverter {

    /**
     * 主动技能图标位置
     */
    private static final String INPUT_ACTIVE = "active";
    /**
     * 被动技能图标位置
     */
    private static final String INPUT_PASSIVE = "passive";
    /**
     * 替换图标目录
     */
    private static final String OUTPUT_REPLACEABLE = "ReplaceableTextures";
    /**
     * 主动技能输出目录
     */
    private static final String COMMAND_BUTTONS = "CommandButtons";
    /**
     * 被动技能输出目录
     */
    private static final String COMMAND_BUTTONS_DISABLED = "CommandButtonsDisabled";

    public static void main(String[] args) {
        new BlpConverter().convert("G:\\1", "G:\\2");
    }

    public void convert(String inputDirectory, String outputDirectory) {
        if (inputDirectory == null || "".equals(inputDirectory)) {
            throw new IllegalStateException("输入文件夹为空");
        }
        if (outputDirectory == null || "".equals(outputDirectory)) {
            throw new IllegalStateException("输出文件夹为空");
        }
        File activeDir = new File(Paths.get(inputDirectory, INPUT_ACTIVE).toString());

        boolean flag = false;
        // 处理主动技能
        File[] imageFiles = activeDir.listFiles();
        if (imageFiles == null || imageFiles.length == 0) {
            flag = true;
        } else {
            for (File imageFile : imageFiles) {
                try {
                    // 读取源文件
                    BufferedImage image = ImageIO.read(imageFile);
                    // 将文件压缩为64*64
                    BufferedImage resized = ImageUtil.resizeTo64(image);

                    // 加上主动技能的边框
                    BufferedImage activeBordered = ImageUtil.addActiveBorder(resized);
                    // 加上被动技能的边框并调暗
                    BufferedImage dark = ImageUtil.addPassiveBorder(ImageUtil.adjustBrightness(resized, -64));

                    // 将中文名转换成拼音
                    String name = getEnglishName(imageFile);

                    // 将文件输出为blp
                    outputAsBlp(activeBordered,
                            Paths.get(outputDirectory, OUTPUT_REPLACEABLE, COMMAND_BUTTONS).toString(), name, "BTN");
                    outputAsBlp(dark,
                            Paths.get(outputDirectory, OUTPUT_REPLACEABLE, COMMAND_BUTTONS_DISABLED).toString(),
                            name, "DISBTN");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 处理被动技能
        File passiveDir = new File(Paths.get(inputDirectory, INPUT_PASSIVE).toString());

        imageFiles = passiveDir.listFiles();
        if (imageFiles == null || imageFiles.length == 0) {
            if (flag) {
                throw new IllegalStateException("输入目录下没有合适的图片");
            }
        } else {
            for (File imageFile : imageFiles) {
                try {
                    BufferedImage image = ImageIO.read(imageFile);
                    BufferedImage resized = ImageUtil.resizeTo64(image);

                    BufferedImage passiveBordered = ImageUtil.addPassiveBorder(resized);
                    BufferedImage dark = ImageUtil.addPassiveBorder(ImageUtil.adjustBrightness(resized, -64));

                    String name = getEnglishName(imageFile);

                    //创建文件输出流
                    outputAsBlp(passiveBordered,
                            Paths.get(outputDirectory, OUTPUT_REPLACEABLE, COMMAND_BUTTONS).toString(), name,
                            "PASBTN");
                    outputAsBlp(dark,
                            Paths.get(outputDirectory, OUTPUT_REPLACEABLE, COMMAND_BUTTONS_DISABLED).toString(),
                            name, "DISPASBTN");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getEnglishName(File imageFile) {
        String originName = imageFile.getName().substring(0, imageFile.getName().indexOf("."));
        String name;
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            name = PinyinHelper.toHanYuPinyinString(originName, format, "", true);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            name = originName;
        }
        return name;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void outputAsBlp(BufferedImage image, String dirPath, String name, String prefix) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream out =
                new FileOutputStream(Paths.get(dirPath, prefix + name + ".blp").toString());
        ImageIO.write(image, "blp", out);
        out.close();
    }
}
