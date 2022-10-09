package com.luxx.seed.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.luxx.seed.model.webssh.FileDTO;
import com.luxx.seed.model.webssh.FileSize;
import com.luxx.seed.model.webssh.SFTPConnectInfo;
import com.luxx.seed.model.webssh.SFTPData;
import com.luxx.seed.util.SFTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebSftpService {
    /**
     * 存放sftp连接信息的map
     */
    private static Map<String, Object> sftpMap = new ConcurrentHashMap<>();

    public void connect(HttpSession session, SFTPData sftpData) throws Exception {
        SFTPConnectInfo sftpConnectInfo = new SFTPConnectInfo();
        SFTPUtil.getConnect(sftpConnectInfo, sftpData);
        sftpMap.put(session.getId(), sftpConnectInfo);
    }

    public void disConnect(HttpSession session) throws Exception {
        SFTPConnectInfo sftpConnectInfo = (SFTPConnectInfo) sftpMap.get(session.getId());
        SFTPUtil.disConn(sftpConnectInfo.getSession(), sftpConnectInfo.getChannel(), sftpConnectInfo.getSftp());
        sftpMap.remove(session.getId());
    }

    public List<FileDTO> listFiles(HttpSession session, String path) throws Exception {
        SFTPConnectInfo sftpConnectInfo = (SFTPConnectInfo) sftpMap.get(session.getId());
        return SFTPUtil.listFiles(path, sftpConnectInfo);
    }

    public void put(HttpSession session, MultipartFile uploadFile, String targetDir) throws Exception {
        SFTPConnectInfo sftpConnectInfo = (SFTPConnectInfo) sftpMap.get(session.getId());
        SFTPUtil.upload(targetDir, uploadFile, sftpConnectInfo);
    }

    public InputStream get(HttpSession session, String fullFileName) throws Exception {
        SFTPConnectInfo sftpConnectInfo = (SFTPConnectInfo) sftpMap.get(session.getId());
        return SFTPUtil.download(fullFileName, sftpConnectInfo);
    }

    public void delete(HttpSession session, String fullFileName) throws Exception {
        SFTPConnectInfo sftpConnectInfo = (SFTPConnectInfo) sftpMap.get(session.getId());
        SFTPUtil.delete(fullFileName, sftpConnectInfo);
    }

    public static void main(String[] args) throws Exception {
        //声明JSCH对象
        JSch jSch = new JSch();
        //获取一个Linux会话
        Session session = jSch.getSession("root", "**", 22);
        //设置登录密码
        session.setPassword("***");
        //关闭key的检验
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        //连接Linux
        session.connect();
        //通过sftp的方式连接
        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        //channel.rm("/root/logs/1234.txt");
        //channel.disconnect();
        Vector<ChannelSftp.LsEntry> list = channel.ls("/root/logs/");
        for (ChannelSftp.LsEntry entry : list) {
            System.out.println(entry.getFilename() + "==" + new FileSize(entry.getAttrs().getSize()).toString() + "==" + entry.getAttrs().isDir());
        }
//        channel.disconnect();
//        //上传文件
//        File file = new File("d:\\1234.txt");
//        InputStream inputStream = new FileInputStream(file);
//        channel.put(inputStream, "/root/logs/1234.txt");
//        //下载文件
//        OutputStream out = new FileOutputStream("d:\\log.log");
//        channel.get("/root/logs/log.log", out);
//        //关闭流
//        inputStream.close();
//        out.close();
    }


}
