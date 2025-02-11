package org.datateam.touristassistant.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.datateam.touristassistant.config.WebSocketConfig;
import org.datateam.touristassistant.pojo.MessageContent;
import org.datateam.touristassistant.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Component
@ServerEndpoint(value = "/ws/{token}")
public class AiWebsocketService {

    private final Logger logger = LoggerFactory.getLogger(AiWebsocketService.class);
    private Session session;

    private final JwtUtil jwtUtil=new JwtUtil();

    private final ObjectMapper objectMapper=new ObjectMapper();

//    // 存放所有的 WebSocket 连接
//    private static Map<String, AiWebsocketService> aiWebSocketServicesMap = new ConcurrentHashMap<>();

    private static AutowireCapableBeanFactory beanFactory;

    @Autowired
    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        AiWebsocketService.beanFactory = beanFactory;
    }

    private AiServiceImpl aiService;
    // 新连接时调用
    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token) {
        this.session = session;
        this.aiService = beanFactory.getBean(AiServiceImpl.class);
//验证token
/*        if (token == null || token.isEmpty()) {
            logger.error("Token 无效");
            try {
                session.close();  // 如果 token 无效，关闭连接
            } catch (IOException e) {
                logger.error("关闭 WebSocket 连接失败", e);
            }
            return;
        }

        if (jwtUtil.isTokenExpiration(token)) {
            logger.error("Token 过期");
            try {
                session.close();  // 如果 token 过期，关闭连接
            } catch (IOException e) {
                logger.error("关闭 WebSocket 连接失败", e);
            }
            return;
        }

        String id = (String) jwtUtil.parseToken(token).get("openid");
        aiWebSocketServicesMap.put(id, this);  // 将连接保存到 map 中*/
        logger.info("有新的 WebSocket 连接进入");
    }

    // 连接关闭时调用
    @OnClose
    public void onClose() {
        try {
            logger.debug("连接断开");
        } catch (Exception e) {
            logger.error("关闭 WebSocket 连接时出现错误", e);
        }
    }

    // 接收到消息时调用
    @OnMessage
    public void onMessage(String message) {
        try {
            logger.info("接收到消息: " + message);
            MessageContent msgContent= objectMapper.readValue(message,MessageContent.class);

            //持久层代码

            //拿到content
            String content = msgContent.getContent();

            //发送得到回应
            Flux<String> flux = aiService.generateRAG(content);

            flux.delayElements(Duration.ofMillis(50)).subscribe(chunk ->{
                sendMessage(chunk);
            });

        } catch (Exception e) {
            logger.error("处理消息时发生错误", e);
        }

    }

    // 通过 WebSocket 发送消息
    public void sendMessage(String message) {
/*        AiWebsocketService aiWebsocketService = aiWebSocketServicesMap.get(id);
        if (aiWebsocketService == null) {
            logger.warn("用户ID：" + id + " 对应的连接不存在");
            return;
        }*/
        try {
            session.getBasicRemote().sendText(message);  // 发送消息
            logger.debug("消息发送成功: " + message);
        } catch (IOException e) {
            logger.error("发送消息失败: " + e.getClass() + ": " + e.getMessage());
        }
    }
}
