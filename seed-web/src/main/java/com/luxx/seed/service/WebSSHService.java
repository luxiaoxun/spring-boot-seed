package com.luxx.seed.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.luxx.seed.constant.WebSshConstant;
import com.luxx.seed.model.webssh.SSHConnectInfo;
import com.luxx.seed.model.webssh.WebSSHMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class WebSSHService {
    //存放ssh连接信息的map
    private static Map<String, Object> sshMap = new ConcurrentHashMap<>();
    //线程池
    private ExecutorService executorService = Executors.newCachedThreadPool();

    //初始化连接
    public void initConnection(WebSocketSession session) {
        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setJSch(jSch);
        sshConnectInfo.setWebSocketSession(session);
        String uuid = String.valueOf(session.getAttributes().get(WebSshConstant.USER_ID_KEY));
        //将这个ssh连接信息放入map中
        sshMap.put(uuid, sshConnectInfo);
    }

    //处理客户端发送的数据
    public void handleMessage(String msgBuffer, WebSocketSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        WebSSHMsg webSSHMsg = null;
        try {
            webSSHMsg = objectMapper.readValue(msgBuffer, WebSSHMsg.class);
        } catch (IOException e) {
            log.error("Json read error:{}", e.getMessage());
            return;
        }
        String userId = String.valueOf(session.getAttributes().get(WebSshConstant.USER_ID_KEY));
        if (WebSshConstant.WEBSSH_OPERATE_CONNECT.equals(webSSHMsg.getOperate())) {
            //找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            //启动线程异步处理
            WebSSHMsg finalWebSSHMsg = webSSHMsg;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connectToSSH(sshConnectInfo, finalWebSSHMsg, session);
                    } catch (JSchException | IOException e) {
                        log.error("web ssh connect error:{}", e.getMessage());
                        close(session);
                    }
                }
            });
        } else if (WebSshConstant.WEBSSH_OPERATE_COMMAND.equals(webSSHMsg.getOperate())) {
            String command = webSSHMsg.getCommand();
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
            if (sshConnectInfo != null) {
                try {
                    transToSSH(sshConnectInfo.getChannel(), command);
                } catch (IOException e) {
                    log.error("web ssh command error:{}", e.getMessage());
                    close(session);
                }
            }
        } else {
            log.error("Unknown web ssh message type: " + webSSHMsg.toString());
            close(session);
        }
    }

    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get(WebSshConstant.USER_ID_KEY));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) {
                sshConnectInfo.getChannel().disconnect();
            }
            //map中移除
            sshMap.remove(userId);
        }
    }

    //使用jsch连接终端
    private void connectToSSH(SSHConnectInfo sshConnectInfo, WebSSHMsg webSSHMsg, WebSocketSession webSocketSession) throws JSchException, IOException {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        Session session = sshConnectInfo.getJSch().getSession(webSSHMsg.getUsername(), webSSHMsg.getHost(), webSSHMsg.getPort());
        session.setConfig(config);
        //设置密码
        session.setPassword(webSSHMsg.getPassword());
        //连接超时时间30s
        session.connect(30000);
        //开启shell通道
        Channel channel = session.openChannel("shell");
        //通道连接超时时间5s
        channel.connect(5000);
        //设置channel
        sshConnectInfo.setChannel(channel);

        //转发消息
        transToSSH(channel, "\r");

        //读取终端返回的信息流
        InputStream inputStream = channel.getInputStream();
        try {
            //循环读取
            byte[] buffer = new byte[1024];
            int i = 0;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    //将消息转发到终端
    private void transToSSH(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }

}
