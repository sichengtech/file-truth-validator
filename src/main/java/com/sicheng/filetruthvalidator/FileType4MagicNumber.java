package com.sicheng.filetruthvalidator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件真实类型检测工具
 * <p>
 * 本工具类通过读取文件的魔数（Magic Number）来检测文件的真实类型。
 * 魔数是文件头部固定字节序列，用于标识文件格式。例如，JPEG 文件的魔数为 "FFD8FF"，
 * PNG 文件的魔数为 "89504E47"。通过分析这些魔数，可以准确判断文件的实际格式，
 * 而非仅依赖文件扩展名。
 * </p>
 *
 * <h2>核心功能</h2>
 * <ul>
 *   <li>支持 48 种常见文件类型的检测。</li>
 *   <li>适用于图片、文档、视频、音频等多种文件格式。</li>
 *   <li>提供精确匹配方法，避免误判。</li>
 *   <li>支持复杂文件格式（如 MP4、WebP、SVG 等）的特殊处理。</li>
 * </ul>
 *
 * <h2>使用方法</h2>
 * <p>
 * 工具类提供了多种静态方法，供开发者根据需求选择使用：
 * </p>
 * <ol>
 *   <li>
 *     <strong>checkFileType(InputStream inputStream, String fileType)</strong><br>
 *     检查输入流是否符合指定的文件类型。<br>
 *     示例代码：
 *     <pre>
 *     try (InputStream input = new FileInputStream("example.jpg")) {
 *         boolean isJpg = FileType4MagicNumber.checkFileType(input, "jpg");
 *         System.out.println("Is JPG file: " + isJpg);
 *     }
 *     </pre>
 *   </li>
 *   <li>
 *     <strong>checkFileType(File file)</strong><br>
 *     检查文件对象是否与其扩展名声明的类型一致。<br>
 *     示例代码：
 *     <pre>
 *     File file = new File("example.docx");
 *     boolean isValidDocx = FileType4MagicNumber.checkFileType(file);
 *     System.out.println("Is valid DOCX file: " + isValidDocx);
 *     </pre>
 *   </li>
 *   <li>
 *     <strong>detectFileType(InputStream inputStream)</strong><br>
 *     分析输入流并返回可能的文件类型列表。<br>
 *     示例代码：
 *     <pre>
 *     try (InputStream input = new FileInputStream("unknown_file")) {
 *         List<String> possibleTypes = FileType4MagicNumber.detectFileType(input);
 *         System.out.println("Possible file types: " + possibleTypes);
 *     }
 *     </pre>
 *   </li>
 * </ol>
 *
 *
 * <h2>支持的文件类型</h2>
 * <p>
 * 目前支持识别的文件类型有 48 种，具体分类如下：
 * </p>
 * <ul>
 *   <li>图片类型：jpg、png、gif、tif、bmp、webp、jp2、svg</li>
 *   <li>文档类型：doc、xls、ppt、vsd、msi、docx、xlsx、pptx、rtf</li>
 *   <li>Adobe 的文件：psd、ai、eps、pdf</li>
 *   <li>压缩类型：zip、rar、tar、gz、jar、apk、7z</li>
 *   <li>视频类型：avi、mov、mpg、mp4、wmv、flv</li>
 *   <li>音频类型：wav、mp3、ram、rm、mid、amr</li>
 *   <li>其它办公场景的常用文件类型：dbx、pst、wpd、qdf、pwl</li>
 *   <li>结构化文档: xml、html、json</li>
 * </ul>
 */
public class FileType4MagicNumber {

    //存储文件类型与魔数的映射关系：key 是文件类型标识符（如 jpg、png），value 是对应的魔数十六进制字符串
    public static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();

    static {
        // 图片类型
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); // JPEG图像文件
        FILE_TYPE_MAP.put("jpeg", "FFD8FF"); // JPEG图像文件
        FILE_TYPE_MAP.put("png", "89504E47"); // PNG图像文件
        FILE_TYPE_MAP.put("gif", "47494638"); // GIF图像文件
        FILE_TYPE_MAP.put("tif", "49492A00"); // TIFF图像文件
        FILE_TYPE_MAP.put("tiff", "49492A00"); // TIFF图像文件
        FILE_TYPE_MAP.put("bmp", "424D"); // BMP位图文件
        FILE_TYPE_MAP.put("webp", "由isWebpFile方法特殊处理"); // webp图像文件，52 49 46 46 xx xx xx xx 57 45 42 50，对应 ASCII 字符：RIFF....WEBP
        FILE_TYPE_MAP.put("jp2", "0000000C6A5020200D0A870A"); // JPEG 2000 graphic file图像文件
        FILE_TYPE_MAP.put("svg", "由isSvgFile方法特殊处理"); // J标准SVG通常以"<?xml"或"<svg"开头

        // 文档类型
        FILE_TYPE_MAP.put("rtf", "7B5C727466"); // 富文本格式
        FILE_TYPE_MAP.put("doc", "D0CF11E0A1B11AE1"); // MS Word 97-2003文档
        FILE_TYPE_MAP.put("xls", "D0CF11E0A1B11AE1"); // MS Excel 97-2003文档
        FILE_TYPE_MAP.put("ppt", "D0CF11E0A1B11AE1"); // MS PowerPoint 97-2003文档
        FILE_TYPE_MAP.put("vsd", "D0CF11E0A1B11AE1"); // MS Visio Document
        FILE_TYPE_MAP.put("msi", "D0CF11E0A1B11AE1"); // Microsoft Installer   （这是可执行文件阿，魔数同office文件不好区分阿）
        FILE_TYPE_MAP.put("docx", "504B0304"); // MS Word 2007+ , 同zip压缩文件
        FILE_TYPE_MAP.put("xlsx", "504B0304"); // MS Excel 2007+ , 同zip压缩文件
        FILE_TYPE_MAP.put("pptx", "504B0304"); // MS PPT 2007+ , 同zip压缩文件

        //Adobe的文件
        FILE_TYPE_MAP.put("pdf", "255044462D312E"); // PDF文档
        FILE_TYPE_MAP.put("psd", "38425053"); // Adobe Photoshop文件
        FILE_TYPE_MAP.put("ai", "25504446"); // Adobe Illustrator 文件
        FILE_TYPE_MAP.put("eps", "由isEpsFile方法特殊处理"); // “eps”格式文件的全称是“Encapsulated Post Script”，是一种被封装后的文件。主要由Ai软件生成的。

        // 压缩文件
        FILE_TYPE_MAP.put("zip", "504B0304"); // ZIP压缩文件
        FILE_TYPE_MAP.put("rar", "52617221"); // RAR压缩文件
        FILE_TYPE_MAP.put("tar", "7573746172"); // tar文件
        FILE_TYPE_MAP.put("gz", "1F8B08"); // gz压缩文件
        FILE_TYPE_MAP.put("tar.gz", "1F8B08"); // gz压缩文件
        FILE_TYPE_MAP.put("jar", "504B0304"); // jar压缩文件（java）
        FILE_TYPE_MAP.put("apk", "504B0304"); // apk安卓安装包
        FILE_TYPE_MAP.put("7z", "377ABCAF271C"); // 7z压缩文件  377ABCAF271C

        // 视频文件
        FILE_TYPE_MAP.put("avi", "由isAviFile方法特殊处理"); // AVI视频文件  正确魔数：52 49 46 46 xx xx xx xx 41 56 49 20
        FILE_TYPE_MAP.put("mov", "6D6F6F76"); // QuickTime视频文件  6D 6F 6F 76
        FILE_TYPE_MAP.put("mpg", "由isMpgFile方法特殊处理"); // MPEG视频文件  000001开头
        FILE_TYPE_MAP.put("mp4", "由isMp4File方法特殊处理"); // MP4视频文件（需特殊匹配逻辑）000000开头
        FILE_TYPE_MAP.put("wmv", "3026B2758E66CF"); // Windows Video file视频文件  30 26 B2 75 8E 66 CF
        FILE_TYPE_MAP.put("flv", "464C56"); // Flash Video视频文件  46 4C 56

        // 音频文件
        FILE_TYPE_MAP.put("wav", "由isWavFile方法特殊处理"); // WAV音频文件  正确魔数：52 49 46 46 xx xx xx xx 57 41 56 45
        FILE_TYPE_MAP.put("mp3", "由isMp3File方法特殊处理"); // MP3音频文件 1. 带ID3v2标签的文件：以"49 44 33"（ID3）开头， 2. 纯MP3帧文件：以"FF FB"/"FF F3"/"FF FA"（帧同步头）开头
        FILE_TYPE_MAP.put("rm", "2E524D46"); // RealMedia视频文件
        FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); // Advanced Systems Format文件
        FILE_TYPE_MAP.put("mid", "4D546864"); // MIDI音频文件
        FILE_TYPE_MAP.put("amr", "2321414D52"); // AMR语音文件
//        FILE_TYPE_MAP.put("ram", "2E7261FD"); // RealAudio元文件   文本文件无固定魔数无法识别


        // 其它办公场景常用文件类型（国企银行）
        FILE_TYPE_MAP.put("dbx", "CFAD12FE"); // Outlook Express邮箱文件(.dbx)
        FILE_TYPE_MAP.put("pst", "2142444E"); // Outlook个人文件夹(.pst)
        FILE_TYPE_MAP.put("wpd", "FF575043"); // WordPerfect文档(.wpd)
        FILE_TYPE_MAP.put("qdf", "AC9EBD8F"); // Quicken数据文件(.qdf)
        FILE_TYPE_MAP.put("pwl", "E3828596"); // Windows密码列表文件(.pwl)  此格式已废弃（现代Windows不再使用）。
//        FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A"); // 电子邮件文件(.eml)   无固定魔数无法识别

        //结构化文档
        FILE_TYPE_MAP.put("xml", "由isXmlFile方法特殊处理"); // XML文档    (检测 <?xml 等字符)
        FILE_TYPE_MAP.put("html", "由isHtmlFile方法特殊处理"); // HTML文档  (检测 <html 等字符)
        FILE_TYPE_MAP.put("json", "由isJsonFile方法特殊处理"); // HTML文档  (检测 {、[ 等字符 )
    }


    /**
     * 精确匹配MP4文件类型
     * <p>
     * MP4文件格式(ISO/IEC 14496-12)可能有以下几种情况：
     * 1. 文件以"ftyp"盒子开头，前面是4字节大小(通常为00 00 00 18/1C/14等)
     * 2. "ftyp"盒子可能出现在文件的不同位置(但通常在开头)
     * 3. 我们检查前8字节是否为"000000"且包含"66747970"(即"ftyp")
     * </p>
     *
     * @param fileHeader 文件头十六进制字符串
     * @return 是否是MP4文件
     */
    private static boolean isMp4File(String fileHeader) {
        // MP4文件应该有"ftyp"盒子(十六进制为66 74 79 70)
        final String FTYP = "66747970";

        // 检查前8字节是否为"000000"开头(4字节大小+4字节类型)
        if (fileHeader.startsWith("000000")) {
            // 检查是否包含"ftyp"盒子
            if (fileHeader.contains(FTYP)) {
                return true;
            }

            // 有些MP4文件可能有额外的元数据，检查偏移32字节处是否有"ftyp"
            if (fileHeader.length() >= 64 && fileHeader.substring(32, 40).equals(FTYP)) {
                return true;
            }
        }

        // 检查其他可能的MP4变种
        if (fileHeader.startsWith("0000001866747970") ||  // 标准MP4
                fileHeader.startsWith("0000001C66747970") ||  // 另一种常见MP4
                fileHeader.startsWith("0000001466747970")) {   // 第三种MP4变种
            return true;
        }
        return false;
    }

    /**
     * 验证WebP文件格式
     * WebP文件结构:
     * 1. 4字节 RIFF (52 49 46 46)
     * 2. 4字节 文件大小（小端序，可忽略）
     * 3. 4字节 WEBP (57 45 42 50)
     * <p>
     * 示例：52494646 20451A00 57454250 565038580A00000020000000FF0D0057
     */
    private static boolean isWebpFile(String fileHeader) {
        // 需要至少12字节(24个十六进制字符)才能验证WebP文件
        if (fileHeader == null || fileHeader.length() < 24) {
            return false;
        }

        // 检查RIFF标识和WEBP格式标识
        return fileHeader.startsWith("52494646") &&  // 检查RIFF头部
                fileHeader.regionMatches(16, "57454250", 0, 8);  // 检查WEBP标识
    }

    /**
     * 验证WAV 音频文件
     * 正确魔数：52 49 46 46 xx xx xx xx 57 41 56 45
     * 对应 ASCII：RIFF....WAVE
     * 前 4 字节：52 49 46 46（RIFF）
     * 第 9-12 字节：57 41 56 45（WAVE）
     */
    private static boolean isWavFile(String fileHeader) {
        // 需要至少12字节(24个十六进制字符)才能验证WebP文件
        if (fileHeader == null || fileHeader.length() < 24) {
            return false;
        }

        // 检查RIFF标识和WEBP格式标识
        return fileHeader.startsWith("52494646") &&  // 检查RIFF头部
                fileHeader.regionMatches(16, "57415645", 0, 8);  // 检查WEBP标识
    }

    /**
     * 精确匹配MPG(MPEG-1/2)文件类型
     * <p>
     * MPG文件格式特征：
     * 1. 文件以"00 00 01"起始码开头，后跟流ID(如BA/B3/E0/C0等)
     * 2. 常见流ID：
     * - 00 00 01 BA: Program Stream包头(常见于.mpg文件开头)
     * - 00 00 01 B3: 视频序列头(关键帧标识)
     * - 00 00 01 E0: 视频流
     * - 00 00 01 C0: 音频流
     * 3. 我们检查前4字节是否为有效起始码组合
     * </p>
     *
     * @param fileHeader 文件头十六进制字符串(不带空格/0x前缀)
     * @return 是否是MPG文件
     */
    private static boolean isMpgFile(String fileHeader) {
        // 至少需要8个字符(4字节的十六进制表示)
        if (fileHeader == null || fileHeader.length() < 8) {
            return false;
        }

        // MPG关键起始码(十六进制表示)
        final String MPEG_PS_HEADER = "000001BA"; // Program Stream包头
        final String MPEG_VIDEO_SEQ = "000001B3"; // 视频序列头
        final String MPEG_VIDEO_STREAM = "000001E0"; // 视频流
        final String MPEG_AUDIO_STREAM = "000001C0"; // 音频流

        // 检查前4字节是否为有效MPEG起始码
        String headerPrefix = fileHeader.substring(0, 8);
        return headerPrefix.equals(MPEG_PS_HEADER) ||
                headerPrefix.equals(MPEG_VIDEO_SEQ) ||
                headerPrefix.equals(MPEG_VIDEO_STREAM) ||
                headerPrefix.equals(MPEG_AUDIO_STREAM);
    }

    /**
     * 精确匹配MP3文件类型
     * <p>
     * MP3文件可能有以下两种形式：
     * 1. 带ID3v2标签的文件：以"49 44 33"（ID3）开头
     * 2. 纯MP3帧文件：以"FF FB"/"FF F3"/"FF FA"（帧同步头）开头
     * 3. 我们优先检查ID3标签，若无则检查帧同步头
     * </p>
     *
     * @param fileHeader 文件头十六进制字符串（不带空格/0x前缀）
     * @return 是否是MP3文件
     */
    private static boolean isMp3File(String fileHeader) {
        // 最小需要检测6个字符（3字节的十六进制）
        if (fileHeader == null || fileHeader.length() < 6) {
            return false;
        }

        // ID3v2标签魔数（49 44 33）
        final String ID3_TAG = "494433";
        // 有效MP3帧头（FF FB, FF F3, FF FA）
        final String MP3_FRAME_1 = "FFFB";
        final String MP3_FRAME_2 = "FFF3";
        final String MP3_FRAME_3 = "FFFA";

        // 情况1：检测ID3v2标签（优先）
        if (fileHeader.startsWith(ID3_TAG)) {
            return true;
        }

        // 情况2：检测MP3帧同步头
        String frameHeader = fileHeader.substring(0, 4);
        return frameHeader.equals(MP3_FRAME_1) ||
                frameHeader.equals(MP3_FRAME_2) ||
                frameHeader.equals(MP3_FRAME_3);
    }

    /**
     * XML检测增强
     */
    private static boolean isXmlFile(String fileHeader) {
        // 检查标准XML声明
        if (fileHeader.contains("3C3F786D6C")) {  // <?xml
            return true;
        }
        // 检查带BOM的情况
        if (fileHeader.contains("EFBBBF3C3F786D6C")) {  // UTF-8 BOM + <?xml
            return true;
        }
//        // 检查没有XML声明但以元素开头的情况
//        return fileHeader.contains("3C") &&
//                !fileHeader.startsWith("3C21"); // 以<开头但不是注释
        return false;
    }

    /**
     * HTML检测增强
     */
    private static boolean isHtmlFile(String fileHeader) {
        // 检查DOCTYPE声明
        if (fileHeader.contains("3C21444F4354595045")) {  // <!DOCTYPE
            return true;
        }
        // 检查<html>标签开头
        if (fileHeader.contains("3C68746D6C")) {  // <html
            return true;
        }
        // 检查XHTML
        if (fileHeader.contains("3C3F786D6C") &&
                fileHeader.contains("68746D6C")) {  // <?xml ... html
            return true;
        }
        return false;
    }

    /**
     * JSON文件检测增强
     * <p>
     * JSON文件可能有以下常见开头：
     * 1. 以对象开头: "{" (7B)
     * 2. 以数组开头: "[" (5B)
     * 3. 带BOM的JSON: EFBBBF + "{" 或 "["
     * 4. 带引号开头（某些API响应）: "{\"" (7B22) 或 "[\"" (5B22)
     * </p>
     * <p>
     * 改进点：
     * 1. 使用contains()方法检测关键魔数，跳过前缀空白/格式字符
     * 2. 支持更多JSON变体格式
     * 3. 明确处理BOM和空白字符场景
     * </p>
     *
     * @param fileHeader 文件头十六进制字符串（不带空格/0x前缀）
     * @return 是否是JSON文件
     */
    private static boolean isJsonFile(String fileHeader) {
        if (fileHeader == null || fileHeader.length() < 2) {
            return false;
        }

        // 处理可能的空白字符（十六进制20）
        String trimmedHeader = fileHeader.replaceAll("^20+", ""); // 移除开头的空格

        // 1. 检查标准JSON开头（{ 或 [）
        if (trimmedHeader.contains("7B") || trimmedHeader.contains("5B")) {
            // 验证关键字符出现在合理位置（前100字节内）
            int objPos = trimmedHeader.indexOf("7B");
            int arrPos = trimmedHeader.indexOf("5B");
            if ((objPos >= 0 && objPos < 200) || (arrPos >= 0 && arrPos < 200)) {
                return true;
            }
        }

        // 2. 检查带UTF-8 BOM的情况（EFBBBF）
        if (trimmedHeader.contains("EFBBBF")) {
            int bomPos = trimmedHeader.indexOf("EFBBBF");
            String afterBom = trimmedHeader.substring(bomPos + 6);
            return afterBom.contains("7B") || afterBom.contains("5B");
        }

        // 3. 检查带引号的对象/数组开头（{" 或 ["）
        if (trimmedHeader.contains("7B22") || trimmedHeader.contains("5B22")) {
            return true;
        }

        // 4. 处理非常规空白字符（如制表符09、换行符0A/0D）
        String noWhitespaceHeader = trimmedHeader.replaceAll("(09|0A|0D)+", "");
        if (noWhitespaceHeader.startsWith("7B") || noWhitespaceHeader.startsWith("5B")) {
            return true;
        }

        return false;
    }

    /**
     * 验证AVI视频文件
     * <p>
     * AVI文件格式规范：
     * 1. 文件必须以"RIFF"头部开头（52 49 46 46）
     * 2. 后跟4字节文件大小（小端序，通常不验证）
     * 3. 紧接着必须是"AVI "标识（41 56 49 20）
     * 4. 标准AVI文件还会包含"hdrl"列表（68 64 72 6C）
     * </p>
     *
     * @param fileHeader 文件头十六进制字符串（不带空格/0x前缀）
     * @return 是否是AVI文件
     */
    private static boolean isAviFile(String fileHeader) {
        // 最小需要检测12字节(24个十六进制字符)进行基础验证
        if (fileHeader == null || fileHeader.length() < 24) {
            return false;
        }

        // 1. 检查RIFF头部（前4字节）
        if (!fileHeader.startsWith("52494646")) { // "RIFF"
            return false;
        }

        // 2. 检查AVI标识（第9-12字节）
        if (!fileHeader.regionMatches(16, "41564920", 0, 8)) { // "AVI "
            return false;
        }

        // 增强验证（检查hdrl列表头）
        if (fileHeader.length() >= 48) {
            // hdrl列表通常出现在第13-16字节位置（文件头偏移量12处）
            return fileHeader.regionMatches(40, "6864726C", 0, 8); // "hdrl"
        }

        return true;
    }

    /**
     * EPS 文件有两种存储形式：
     * <p>
     * 类型	         魔数	        特征	                常见来源
     * ASCII EPS	25215053 (%!PS)	纯文本，可编辑	      手动保存的 EPS
     * 二进制 EPS	C5D0D3C6 + 其他	含二进制头，不可直接阅读	Illustrator 导出
     *
     * @param fileHeader
     * @return
     */
    private static boolean isEpsFile(String fileHeader) {
        if (fileHeader == null || fileHeader.length() < 8) {  // 至少需要4字节(8个十六进制字符)
            return false;
        }

        // 检查ASCII EPS (对应十六进制 25215053 即 "%!PS")
        if (fileHeader.startsWith("25215053")) {
            return true;
        }

        // 检查二进制EPS (魔数 C5D0D3C6)
        if (fileHeader.startsWith("C5D0D3C6")) {
            return true;
        }

        return false;
    }


    /**
     * 增强版SVG检测（支持BOM和空白前缀）
     */
    private static boolean isSvgFile(String fileHeader) {
        // 最小需要检测的字符数
        if (fileHeader == null || fileHeader.length() < 6) {
            return false;
        }

        // 处理可能的UTF-8 BOM（EFBBBF）
        String trimmedHeader = fileHeader.startsWith("EFBBBF") ?
                fileHeader.substring(6) :
                fileHeader;

        // 检查所有可能的SVG开头模式
        return trimmedHeader.startsWith("3C3F786D6C") ||  // <?xml
                trimmedHeader.startsWith("3C737667") ||    // <svg
                trimmedHeader.startsWith("3C21444F43") ||  // <!DOC
                trimmedHeader.contains("3C737667");       // 包含<svg（允许前面有注释）
    }


    /**
     * 确保读取指定长度的字节
     *
     * @param in     输入流
     * @param buffer 目标缓冲区
     * @return 实际读取的字节数
     * @throws IOException 如果读取时发生错误
     */
    private static int readFully(InputStream in, byte[] buffer) throws IOException {
        int bytesRead = 0;
        while (bytesRead < buffer.length) {
            int read = in.read(buffer, bytesRead, buffer.length - bytesRead);
            if (read == -1) break;
            bytesRead += read;
        }
        return bytesRead;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串(大写)
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexBuilder.append(String.format("%02X", b & 0xFF));
        }
        return hexBuilder.toString();
    }


    private static boolean checkFileType(String fileHeader, String type) {
        if (type != null) {
            type = type.toLowerCase().trim();
        }

        // 1. 验证MP4文件类型
        if ("mp4".equals(type)) {
            if (isMp4File(fileHeader)) {
                return true;
            }
        }

        // 2. 验证WebP文件格式
        if ("webp".equals(type)) {
            if (isWebpFile(fileHeader)) {
                return true;
            }
        }

        // 3. 验证WAV音频文件
        if ("wav".equals(type)) {
            if (isWavFile(fileHeader)) {
                return true;
            }
        }

        // 4. 验证mpg文件格式（MPG/MPEG视频文件验证方法）
        if ("mpg".equals(type)) {
            if (isMpgFile(fileHeader)) {
                return true;
            }
        }

        // 5. 验证mp3文件格式
        if ("mp3".equals(type)) {
            if (isMp3File(fileHeader)) {
                return true;
            }
        }

        // 6. 验证xml文件格式
        if ("xml".equals(type)) {
            if (isXmlFile(fileHeader)) {
                return true;
            }
        }

        // 7. 验证html文件格式
        if ("html".equals(type)) {
            if (isHtmlFile(fileHeader)) {
                return true;
            }
        }

        // 8. 验证json文件格式
        if ("json".equals(type)) {
            if (isJsonFile(fileHeader)) {
                return true;
            }
        }

        // 9. 验证AVI视频文件
        if ("avi".equals(type)) {
            if (isAviFile(fileHeader)) {
                return true;
            }
        }

        // 10. 验证eps视频文件
        if ("eps".equals(type)) {
            if (isEpsFile(fileHeader)) {
                return true;
            }
        }
        // 11. 验证svg视频文件
        if ("svg".equals(type)) {
            if (isSvgFile(fileHeader)) {
                return true;
            }
        }

        // 99. 最后检查普通文件类型
        String hexString = FILE_TYPE_MAP.get(type);
        if (hexString != null) {
            return fileHeader.startsWith(hexString);
        }

        return false;
    }


    /**
     * 检查文件二进制流 是否是某种 文件类型。 【主力方法，推荐使用】
     * 例如：abc.jpg 通过扩展名上看是jpg文件，本方法可以分析出本文件是不是真证的jpg文件。
     *
     * @param inputStream 文件的输入流
     * @param fileType    文件的扩展名，例如："jpg", "png", "gif" 等 48种文件类型
     * @return 检查结果 true/false
     */
    public static boolean checkFileType(InputStream inputStream, String fileType) {
        if (inputStream == null) {
            return false;
        }
        try {
            byte[] header = new byte[28];
            int bytesRead = readFully(inputStream, header);
            if (bytesRead < 28) {
                return false; // 文件太小，不符合任何类型的魔数长度要求
            }
            String fileHeader = bytesToHexString(header);
            return checkFileType(fileHeader, fileType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查文件是否是它声明的真实类型。  【主力方法，推荐使用】
     * 例如：abc.jpg 通过扩展名上看是jpg文件，本方法可以分析出本文件是不是真证的jpg文件。
     *
     * @param file 文件对象
     * @return 检查结果 true/false
     */
    public static boolean checkFileType(File file) {
        try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            // 标记当前位置以便后续重置
            in3.mark(32); // 比需要的28字节稍大一些
            String fileType = FileUtils.getSmartExtension(file.getName()); // 获取文件扩展名
            boolean bl = checkFileType(in3, fileType);
            if (in3.markSupported()) {
                in3.reset();
            }
            return bl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对数据流进行分析，识别出文件的真实类型，可识别48种文件类型， 返回 文件的可能性扩展名。 【不推荐使用本方法】
     * 例如：推荐算法API详解.docx  文件的检测结果是：[jar, apk, zip, pptx, xlsx, docx] ，它符合多种类型文件的特征。
     * 例如：p3.png 的检测结果是：[png]  ， 对于图像的检测还能很适合的。
     *
     * @param inputStream 文件输入流
     * @return 文件的真实类型扩展名, 返回List<String>是因为可能识别出多种类型
     */
    public static List<String> detectFileType(InputStream inputStream) {
        List<String> types = new ArrayList<>();
        if (inputStream == null) {
            return types;
        }
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream); //使用支持reset的输入流
        }
        inputStream.mark(32);

        for (String type : FILE_TYPE_MAP.keySet()) {
            if (checkFileType(inputStream, type)) {
                types.add(type);
            }
            try {
                inputStream.reset(); //重置输入流，为下一次使用做好准备
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return types;
    }

    /**
     * 采用HexString格式显示文件头28个字节 （用于调试）
     *
     * @param inputStream
     * @return
     */
    public static String showHexString(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        try {
            byte[] header = new byte[28];
            int bytesRead = readFully(inputStream, header);
            if (bytesRead < 28) {
                return ""; // 文件太小，不符合任何类型的魔数长度要求
            }
            return bytesToHexString(header);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}