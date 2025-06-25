# File Truth Validator 文件真实类型检测工具

File Truth Validator 工具类通过读取文件的魔数（Magic Number）来检测文件的真实类型。  
魔数是文件头部固定字节序列，用于标识文件格式。例如，JPEG 文件的魔数为 "FFD8FF"，PNG 文件的魔数为 "89504E47"。通过分析这些魔数，可以准确判断文件的实际格式，而非仅依赖文件扩展名。

## 适用场景

在开发信息化系统时，通常需要处理文件上传功能（如上传图片、Office文档、PDF、视频等）。为保证系统安全性，需要验证文件的真实类型，而不仅依赖文件扩展名。

本工具是一个超级轻量化的解决方案，适合以下场景：
- 需要快速验证常见文件类型的真实性
- 对性能有较高要求
- 希望最小化依赖

## 支持的文件类型

目前支持识别的文件类型有48种，具体分类如下：

- **图片类型**：jpg、png、gif、tif、bmp、webp、jp2、svg
- **文档类型**：doc、xls、ppt、vsd、msi、docx、xlsx、pptx、rtf
- **Adobe文件**：psd、ai、eps、pdf
- **压缩类型**：zip、rar、tar、gz、jar、apk、7z
- **视频类型**：avi、mov、mpg、mp4、wmv、flv
- **音频类型**：wav、mp3、ram、rm、mid、amr
- **其它办公场景常用文件**：dbx、pst、wpd、qdf、pwl
- **结构化文档**：xml、html、json

## 与Apache Tika对比

Apache Tika是专门用于内容类型检测和内容提取的库，支持超过1400种文件格式。但其tika-parsers解析器引入了近百个三方jar包，依赖较重。

如果你的场景：
- 需要识别非常见文件格式
- 需要内容提取功能
- 可以接受较重依赖

可以考虑使用Apache Tika。如果你只需要轻量级的常见文件类型验证，File Truth Validator 工具是更好的选择。

## 核心功能

- **支持48种常见文件类型的检测**：
  - 图片、文档、视频、音频等多种文件格式
- **精确匹配方法**：
  - 避免误判，确保文件类型识别的准确性
- **特殊处理复杂文件格式**：
  - 如MP4、WebP、SVG等，提供针对性的解析逻辑
- **轻量化设计**：
  - 仅依赖Java标准库，易于集成和使用

## 使用方法

工具类提供了多种静态方法，供开发者根据需求选择使用：

1. **检查输入流是否符合指定的文件类型**
```
try (InputStream input = new FileInputStream("example.jpg")) {
    boolean isValidJpg = FileType4MagicNumber.checkFileType(input, "jpg");
    System.out.println("Is valid JPG file: " + isValidJpg);
}

```

2. 检查文件对象是否与其扩展名声明的类型一致
```
File file = new File("example.docx");
boolean isValidDocx = FileType4MagicNumber.checkFileType(file);
System.out.println("Is valid DOCX file: " + isValidDocx);


```

3. 分析输入流并返回可能的文件类型列表
```
try (InputStream input = new FileInputStream("unknown_file")) {
    List<String> possibleTypes = FileType4MagicNumber.detectFileType(input);
    System.out.println("Possible file types: " + possibleTypes);
}
