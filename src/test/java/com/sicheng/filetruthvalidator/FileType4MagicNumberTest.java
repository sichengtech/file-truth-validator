package com.sicheng.filetruthvalidator;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileType4MagicNumberTest {


    /**
     * 单元测试，测试检查图像文件类型
     */
    @Test
    public void test_checkFileType_image() throws Exception {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/图像/";
        // 遍历文件夹获取所有文件列表
        List<File> list = FileUtils.traverseFile(new File(basedir));
        System.out.println("共有" + list.size() + "个文件");
        // 遍历文件列表
        for (File file : list) {
            // 获取文件名
            String fileName = file.getName();
            // 获取文件扩展名
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名

            // 使用 try-with-resources 确保文件流正确关闭
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                // 调用方法检查文件类型是否与扩展名一致
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                // 断言文件类型与扩展名一致
                assertTrue(bl);
            } catch (Exception e) {
                // 如果发生异常，抛出运行时异常
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 单元测试，测试检查office文件类型
     */
    @Test
    public void test_checkFileType_document() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/office文档/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String parentDirName = file.getParentFile().getName();
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertTrue(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 单元测试，测试检查Adobe文件类型
     */
    @Test
    public void test_checkFileType_Adobe() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/Adobe文件/eps/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertTrue(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 单元测试，测试检查压缩包文件类型
     */
    @Test
    public void test_checkFileType_压缩包() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/压缩包/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertTrue(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 单元测试，测试检查视频文件类型
     */
    @Test
    public void test_checkFileType_视频() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/视频/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertTrue(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 单元测试，测试检查音频文件类型
     */
    @Test
    public void test_checkFileType_音频() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/音频/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertTrue(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 单元测试，测试检查其它办公文件类型
     */
    @Test
    public void test_checkFileType_其它办公场景常用文件类型() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/其它办公场景常用文件类型/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertTrue(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 单元测试，测试检查结构化文档文件类型
     */
    @Test
    public void test_checkFileType_结构化文档() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/结构化文档/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertTrue(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 单元测试，测试检查 不支持的文件类型
     */
    @Test
    public void test_checkFileType_不支持的文件类型() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/不支持的类型/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertFalse(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 单元测试，测试检查  假设我是伪装的病毒文件
     */
    @Test
    public void test_checkFileType_假设我是伪装的病毒文件() {
        // 获取项目根目录下的测试资源文件夹路径
        String basedir = System.getProperty("user.dir") + "/src/test/resources/testFiles2/假设我是伪装的病毒文件/";
        File rootDir = new File(basedir);
        List<File> list = FileUtils.traverseFile(rootDir);
        System.out.println("共有" + list.size() + "个文件");
        for (File file : list) {
            String fileName = file.getName();
            String ext = FileUtils.getSmartExtension(fileName);  // 获取扩展名
            try (InputStream in3 = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                boolean bl = FileType4MagicNumber.checkFileType(in3, ext);
                System.out.println(file.getName() + " 是 " + ext + " 类型吗？" + bl);
                assertFalse(bl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
