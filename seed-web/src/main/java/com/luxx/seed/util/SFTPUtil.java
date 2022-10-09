package com.luxx.seed.util;

import com.jcraft.jsch.*;

import com.luxx.seed.model.webssh.FileDTO;
import com.luxx.seed.model.webssh.FileSize;
import com.luxx.seed.model.webssh.SFTPConnectInfo;
import com.luxx.seed.model.webssh.SFTPData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * @ClassName SFTPUtil
 * @Author luo jin jiang
 * @Date 2020/3/21 17:11
 * @Version 1.0
 */
@Slf4j
public class SFTPUtil {

    /**
     * 连接ftp/sftp服务器
     *
     * @param sftpConnectInfo
     * @param sftpData
     */
    public static void getConnect(SFTPConnectInfo sftpConnectInfo, SFTPData sftpData) throws Exception {
        // 主机
        String host = sftpData.getHost();
        //端口
        int port = sftpData.getPort();
        // 用户名
        String username = sftpData.getUsername();
        // 密码
        String password = sftpData.getPassword();
        Channel channel = null;
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        // 不验证 HostKey
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        try {
            session.connect();
        } catch (Exception e) {
            if (session.isConnected()) {
                session.disconnect();
            }
            log.error("连接服务器失败,请检查主机[" + host + "],端口[" + port
                    + "],用户名[" + username + "],端口[" + port
                    + "]是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.");
        }
        channel = session.openChannel("sftp");
        try {
            channel.connect();
        } catch (Exception e) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
            log.error("连接服务器失败,请检查主机[" + host + "],端口[" + port
                    + "],用户名[" + username + "],密码是否正确,以上信息正确的情况下请检查网络连接是否正常或者请求被防火墙拒绝.");
        }
        ChannelSftp sftp = (ChannelSftp) channel;

        sftpConnectInfo.setChannel(channel);
        sftpConnectInfo.setSession(session);
        sftpConnectInfo.setSftp(sftp);

    }

    /**
     * 断开连接
     */
    public static void disConn(Session session, Channel channel, ChannelSftp sftp) throws Exception {
        if (null != sftp) {
            sftp.disconnect();
            sftp.exit();
            sftp = null;
        }
        if (null != channel) {
            channel.disconnect();
            channel = null;
        }
        if (null != session) {
            session.disconnect();
            session = null;
        }
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录-相对于SFPT设置的用户访问目录，
     *                   为空则在SFTP设置的根目录进行创建文件（除设置了服务器全磁盘访问）
     * @param file 要上传的文件
     */
    public static void upload(String directory, MultipartFile file, SFTPConnectInfo sftpConnectInfo) throws Exception {
        ChannelSftp sftp = sftpConnectInfo.getSftp();
        try {
            //进入目录
            sftp.cd(directory);
        } catch (SftpException sException) {
            //指定上传路径不存在
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == sException.id) {
                //创建目录
                sftp.mkdir(directory);
                //进入目录
                sftp.cd(directory);
            }
        }
//        File file = new File(uploadFile);
//        InputStream in = new FileInputStream(file);
        sftp.put(file.getInputStream(), file.getOriginalFilename());
        //in.close();
    }


    /**
     * 下载文件
     *
     * @param downloadFile 下载的文件
     * @param sftpConnectInfo
     */
    public static InputStream download(String downloadFile, SFTPConnectInfo sftpConnectInfo) throws Exception {
        // sftp操作类
        ChannelSftp sftp = sftpConnectInfo.getSftp();
        try {
            return sftp.get(downloadFile);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * 删除文件
     *
     * @param deleteFile 要删除的文件
     */
    public static void delete(String deleteFile, SFTPConnectInfo sftpConnectInfo) throws Exception {
        ChannelSftp sftp = sftpConnectInfo.getSftp();
        try {
//            sftp.cd(directory); //进入的目录应该是要删除的目录的上一级
            sftp.rm(deleteFile);
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return list 文件名列表
     * @throws Exception
     */
    public static List<FileDTO> listFiles(String directory, SFTPConnectInfo sftpConnectInfo) throws Exception {
        // sftp操作类
        ChannelSftp sftp = sftpConnectInfo.getSftp();
        List<FileDTO> fileDTOS = new ArrayList<>();
        //返回目录下所有文件名称
        @SuppressWarnings("unchecked")
        Vector<ChannelSftp.LsEntry> fileList = sftp.ls(directory);
        for (ChannelSftp.LsEntry entry : fileList) {
            if ("..".equals(entry.getFilename()) || ".".equals(entry.getFilename())) {
                continue;
            }
            FileDTO dto = new FileDTO();
            dto.setFileName(entry.getFilename());
            dto.setFileSize(new FileSize(entry.getAttrs().getSize()).toString());
            dto.setIsDir(entry.getAttrs().isDir());
            if ("/".equals(directory)) {
                dto.setFullFileName(directory.concat(entry.getFilename()));
            } else {
                dto.setFullFileName(directory.concat("/").concat(entry.getFilename()));
            }
            dto.setMtime(entry.getAttrs().getMtimeString());
            fileDTOS.add(dto);
        }
        return fileDTOS;
    }

//    /**
//     * 删除目录下所有文件
//     *
//     * @param directory 要删除文件所在目录
//     */
//    public static void deleteAllFile(String directory, SFTPData sftpData) throws Exception {
//        SFTPConnectInfo s = new SFTPConnectInfo();
//        getConnect(s, sftpData);//建立连接
//        Session session = s.getSession();
//        Channel channel = s.getChannel();
//        ChannelSftp sftp = s.getSftp();// sftp操作类
//        try {
//            List<FileDTO> files = listFiles(directory, sftpData);//返回目录下所有文件名称
//            sftp.cd(directory); //进入目录
//
//            for (FileDTO deleteFile : files) {
//                sftp.rm(deleteFile.getFileName());//循环一次删除目录下的文件
//            }
//        } catch (Exception e) {
//            throw new Exception(e.getMessage(), e);
//        } finally {
//            disConn(session, channel, sftp);
//        }
//
//    }
//
//    /**
//     * 删除目录 (删除的目录必须为空)
//     *
//     * @param deleteDir 要删除的目录
//     */
//    public static void deleteDir(String deleteDir, SFTPData sftpData) throws Exception {
//        SFTPConnectInfo s = new SFTPConnectInfo();
//        getConnect(s, sftpData);//建立连接
//        Session session = s.getSession();
//        Channel channel = s.getChannel();
//        ChannelSftp sftp = s.getSftp();// sftp操作类
//        try {
//
//            sftp.rmdir(deleteDir);
//
//        } catch (Exception e) {
//            throw new Exception(e.getMessage(), e);
//        } finally {
//            disConn(session, channel, sftp);
//        }
//    }
//
//    /**
//     * 创建目录
//     *
//     * @param directory 要创建的目录 位置
//     * @param dir       要创建的目录
//     */
//    public static void creatDir(String directory, String dir, SFTPData sftpData) throws Exception {
//        SFTPConnectInfo s = new SFTPConnectInfo();
//        getConnect(s, sftpData);//建立连接
//        Session session = s.getSession();
//        Channel channel = s.getChannel();
//        ChannelSftp sftp = s.getSftp();// sftp操作类
//        try {
//            sftp.cd(directory);
//            sftp.mkdir(dir);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage(), e);
//        } finally {
//            disConn(session, channel, sftp);
//        }
//    }
//
//    /**
//     * 更改文件名
//     *
//     * @param directory 文件所在目录
//     * @param oldFileNm 原文件名
//     * @param newFileNm 新文件名
//     * @throws Exception
//     */
//    public static void rename(String directory, String oldFileNm, String newFileNm, SFTPData sftpData) throws Exception {
//        SFTPConnectInfo s = new SFTPConnectInfo();
//        getConnect(s, sftpData);//建立连接
//        Session session = s.getSession();
//        Channel channel = s.getChannel();
//        ChannelSftp sftp = s.getSftp();// sftp操作类
//        try {
//            sftp.cd(directory);
//            sftp.rename(oldFileNm, newFileNm);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage(), e);
//        } finally {
//            disConn(session, channel, sftp);
//        }
//    }
//
//    /**
//     * 进入目录
//     *
//     * @param directory
//     * @throws Exception
//     */
//    public static void cd(String directory, SFTPData sftpData) throws Exception {
//
//        SFTPConnectInfo s = new SFTPConnectInfo();
//        getConnect(s, sftpData);//建立连接
//        Session session = s.getSession();
//        Channel channel = s.getChannel();
//        ChannelSftp sftp = s.getSftp();// sftp操作类
//        try {
//            sftp.cd(directory); //目录要一级一级进
//        } catch (Exception e) {
//            throw new Exception(e.getMessage(), e);
//        } finally {
//            disConn(session, channel, sftp);
//        }
//    }
}