package com.sicheng.filetruthvalidator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilsTest {

    /**
     * 测试复合扩展名识别
     */
    @Test
    void testGetSmartExtensionWithCompoundExtensions() {
        assertEquals("tar.gz", FileUtils.getSmartExtension("example.tar.gz"));
        assertEquals("tar.bz2", FileUtils.getSmartExtension("example.tar.bz2"));
        assertEquals("tar.xz", FileUtils.getSmartExtension("example.tar.xz"));
        assertEquals("tar.zst", FileUtils.getSmartExtension("example.tar.zst"));
        assertEquals("pkg.tar.zst", FileUtils.getSmartExtension("example.pkg.tar.zst"));
        assertEquals("svg.gz", FileUtils.getSmartExtension("example.svg.gz"));
        assertEquals("jpg.lr", FileUtils.getSmartExtension("example.jpg.lr"));

        // 测试大小写不敏感
        assertEquals("tar.gz", FileUtils.getSmartExtension("example.TAR.GZ"));
    }

    /**
     * 测试隐藏文件（无扩展名）
     */
    @Test
    void testGetSmartExtensionWithHiddenFiles() {
        assertEquals("", FileUtils.getSmartExtension(".profile"));
        assertEquals("", FileUtils.getSmartExtension(".htaccess"));
        assertEquals("", FileUtils.getSmartExtension(".gitignore"));
    }

    /**
     * 测试普通单扩展名
     */
    @Test
    void testGetSmartExtensionWithSimpleExtensions() {
        assertEquals("txt", FileUtils.getSmartExtension("document.txt"));
        assertEquals("jpg", FileUtils.getSmartExtension("photo.jpg"));
        assertEquals("mp3", FileUtils.getSmartExtension("song.MP3")); // 测试大小写转换
        assertEquals("pdf", FileUtils.getSmartExtension("report..pdf")); // 测试多个点
    }

    /**
     * 测试无扩展名情况
     */
    @Test
    void testGetSmartExtensionWithNoExtension() {
        assertEquals("", FileUtils.getSmartExtension("filename"));
        assertEquals("", FileUtils.getSmartExtension(""));
        assertEquals("", FileUtils.getSmartExtension(" "));
    }

    /**
     * 测试边界情况
     */
    @Test
    void testGetSmartExtensionBoundaryCases() {
        assertEquals("", FileUtils.getSmartExtension(".")); // 仅有点
        assertEquals("", FileUtils.getSmartExtension("file.")); // 以点结尾
        assertEquals("tar.gz", FileUtils.getSmartExtension(".tar.gz")); // 以点开头但有复合扩展名
    }
}
