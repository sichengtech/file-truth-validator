package com.sicheng.filetruthvalidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtils {

    /**
     * 通过递归遍历parentDir文件夹下的所有文件 (不包含目录)
     * 仅适合文件数量不多的情况
     * 仅用于测试环境
     *
     * @param parentDir
     * @return 文件列表
     */
    public static List<File> traverseFile(File parentDir) {
        List<File> list = new ArrayList<>();
        if (parentDir.isDirectory()) {
            File[] files = parentDir.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    List<File> listtmp = traverseFile(subFile); // 递归调用
                    for (File ff : listtmp) {
                        if (!".DS_Store".equals(ff.getName())) {
                            list.add(ff);
                        }
                    }
                }
            }
        } else {
            if (!".DS_Store".equals(parentDir.getName())) {
                list.add(parentDir);
            }
        }
        return list;
    }

    /**     * 获取文件后缀名 （可识别复合扩展名）
     * <p>
     * 在文件系统中，存在多种复合扩展名（多个点号分隔的后缀），常见的可分为以下几类：
     *
     * 一、压缩归档类（最常见）
     *   - 双重压缩格式
     *     .tar.gz (gzip压缩的tar包)
     *     .tar.bz2 (bzip2压缩的tar包)
     *     .tar.xz (xz压缩的tar包)
     *     .tar.zst (zstd压缩的tar包)
     *   - 安装包相关
     *     .rpm (Red Hat包)
     *     .deb (Debian包)
     *     .pkg.tar.zst (Arch Linux包)
     *
     * 二、编程开发类
     *   - 构建工具
     *     .min.js (压缩后的JavaScript)
     *     .d.ts (TypeScript类型定义)
     *     .esm.js (ES Module格式JS)
     *   - 配置文件
     *     .conf.d (配置目录)
     *     .service (systemd服务文件)
     *
     * 三、文档与媒体类
     *   - 电子书格式
     *     .epub (实际是zip包)
     *     .fb2.zip (压缩的FictionBook2文档)
     *   - 图像相关
     *     .svg.gz (压缩的SVG图像)
     *     .jpg.lr (Lightroom预览文件)
     *
     * 四、系统与日志文件
     *   - 日志轮转
     *     .log.1 .log.2.gz (轮转日志)
     *     .journal (systemd日志)
     *   - 备份文件
     *     .bak~ (临时备份)
     *     .swp (vim临时文件)
     *
     * 五、特殊案例
     *   - 版本控制
     *     .gitignore
     *     .gitmodules
     *   - 隐藏文件
     *     .profile (Unix配置文件)
     *     .htaccess (Apache配置)
     *
     * @param filename
     * @return

     */
    public static String getSmartExtension(String filename) {
        //先处理 复合扩展名
        String[] knownMultiExtensions = {"tar.gz", "tar.bz2", "tar.xz", "pkg.tar.zst", "tar.zst", "svg.gz", "jpg.lr"};
        String lowerName = filename.toLowerCase();
        for (String ext : knownMultiExtensions) {
            if (lowerName.endsWith("." + ext)) {
                return ext;
            }
        }
        // 处理隐藏文件（如.profile） 它是没有扩展名的
        if (filename.startsWith(".") && filename.indexOf('.', 1) < 0) {
            return "";
        }

        int lastDot = filename.lastIndexOf('.');
        if (lastDot < 0) return "";

        // 最后处理普通的 单扩展名
        return filename.substring(lastDot + 1).toLowerCase();
    }
}
