package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试minio的sdk
 * @date 2023/2/17 11:55
 */
public class MinioTest {

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://127.0.0.1:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    /**
     * 上传文件
     * @throws Exception
     */
    @Test
    public void test_upload() throws Exception {

        //通过扩展名得到媒体资源类型 mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();
        }

        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")//桶
                .filename("D:\\BaiduNetdiskDownload\\1、黑马程序员Java项目《学成在线》企业级开发实战\\学成在线项目—视频\\day01 项目介绍&环境搭建\\Day1-01.项目介绍.mp4")
//                .filename("D:\\develop\\upload\\1.mp4") //指定本地文件路径
//                .object("1.mp4")//对象名 在桶下存储该文件
                .object("test/01/1.mp4")//对象名 放在子目录下
                .contentType(mimeType)//设置媒体文件类型
                .build();

        //上传文件
        minioClient.uploadObject(uploadObjectArgs);



    }

    //删除文件
    @Test
    public void test_delete() throws Exception {

        //RemoveObjectArgs
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                .object("1.mp4")
                .build();

        //删除文件
        minioClient.removeObject(removeObjectArgs);

    }

    //查询文件 从minio中下载
    @Test
    public void test_getFile() throws Exception {

        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket("testbucket").object("test/01/1.mp4").build();
        //查询远程服务获取到一个流对象
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        //指定输出流
//        FileOutputStream outputStream = new FileOutputStream(new File("D:\\Java\\Project\\ilearning\\file\\download\\1a.mp4"));
        FileOutputStream outputStream = new FileOutputStream(new File("D:\\Java\\MiddleWare\\minIO_download\\1a.mp4"));
        IOUtils.copy(inputStream,outputStream);

        //校验文件的完整性  对文件的内容进行md5
//        FileInputStream fileInputStream1 = new FileInputStream(new File("D:\\develop\\upload\\1.mp4"));
//        String source_md5 = DigestUtils.md5Hex(fileInputStream1);
        String source_md5 = DigestUtils.md5Hex(inputStream);
        FileInputStream fileInputStream = new FileInputStream(new File("D:\\Java\\MiddleWare\\minIO_download\\1a.mp4"));
        String local_md5 = DigestUtils.md5Hex(fileInputStream);
        //本地流和远程读取的流会有差异，所以比对结果为非；正确的做法是，将文件服务器的文件下载到本地，读取成本地流再进行对比
        if(source_md5.equals(local_md5)){
            System.out.println("下载成功");
        }

    }

    //合并分块
    @Test
    public void testMerge() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<ComposeSource> sources = Stream.iterate(0, i -> i++).limit(6)
                .map(i -> ComposeSource.builder().bucket("testbucket").object("chunk/".concat(Integer.toString(i))).build())
                .collect(Collectors.toList());
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().bucket("testbucket").object("merge/merge01.mp4").sources(sources).build();
        minioClient.composeObject(composeObjectArgs);
    }

}
