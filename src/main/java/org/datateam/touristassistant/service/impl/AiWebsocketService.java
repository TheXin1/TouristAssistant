package org.datateam.touristassistant.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphbuilder.curve.Polyline;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.datateam.touristassistant.config.WebSocketConfig;
import org.datateam.touristassistant.mapper.MessageMapper;
import org.datateam.touristassistant.pojo.Itinerary;
import org.datateam.touristassistant.pojo.Location;
import org.datateam.touristassistant.pojo.Message;
import org.datateam.touristassistant.pojo.MessageContent;
import org.datateam.touristassistant.utils.JwtUtil;
import org.datateam.touristassistant.utils.SnowFlakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;


@Component
@ServerEndpoint(value = "/ws/{token}")
public class AiWebsocketService {

    private final Logger logger = LoggerFactory.getLogger(AiWebsocketService.class);
    private Session session;

    private final ObjectMapper objectMapper = new ObjectMapper();

//     存放所有的 WebSocket 连接
//    private static Map<String, AiWebsocketService> aiWebSocketServicesMap = new ConcurrentHashMap<>();

    private static AutowireCapableBeanFactory beanFactory;
    @Autowired
    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        AiWebsocketService.beanFactory = beanFactory;
    }

    private AiServiceImpl aiService;

    private MessageMapper messageMapper;

    private TencentMapServiceImpl tencentMapService;

    // 新连接时调用
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;

       /* //验证token
        if(token==null || token.isEmpty() || JwtUtil.isTokenExpiration(token)){
            try {
                session.close();
            } catch (IOException e) {
                logger.error("连接失败", e);
            }
        }*/
        this.aiService = beanFactory.getBean(AiServiceImpl.class);
        this.messageMapper=beanFactory.getBean(MessageMapper.class);
        this.tencentMapService=beanFactory.getBean(TencentMapServiceImpl.class);
        /*session.getUserProperties().put("openid", JwtUtil.parseToken(token).get("openid"));*/
        session.getUserProperties().put("openid",token);
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
            MessageContent msgContent = objectMapper.readValue(message, MessageContent.class);
            String openid= (String) session.getUserProperties().get("openid");

            StringBuilder sb=new StringBuilder();
            //时间
            LocalDateTime nowTime = LocalDateTime.now();
            //拿到content
            String content = msgContent.getContent();

            insertMessageAsync(new Message(openid,content,msgContent.getType()
                    ,nowTime));

            Flux<String> flux;

            MessageContent responseContent = new MessageContent();
            //id
            long id=SnowFlakeUtil.getID();
            //基础字段
            responseContent.setId(id);
            responseContent.setHasSlice(true);
            responseContent.setType("assistant");
            responseContent.setTime(nowTime.toString());
            responseContent.setPolyline(new MessageContent.Polyline(false,null));

            if (isTouristPlanningRelated(content)) {
                flux = aiService.generatePlan(content);
                //具体规划逻辑
                Semaphore semaphore =new Semaphore(0);
                //id
                flux.delayElements(Duration.ofMillis(50)).subscribe(chunk->{
                    sb.append(chunk);
                    responseContent.setContent(chunk);
                    sendMessage(responseContent);
                    }, error -> {
                            semaphore.release();
                        }, // 失败释放信号量
                        semaphore::release// 成功释放信号量
                );

                semaphore.acquire();
                insertMessageAsync(new Message(openid,sb.toString(),"assistant",nowTime));

                Itinerary point=aiService.getPoint(sb.toString());

                logger.info(point.toString());

                //目前只拿第一天
                List<String> route = point.getItinerary().get(0).getRoute();



                List<List<Double>> xy =new ArrayList<>();


                for (String s:route){
                    ArrayList<Double> temp=new ArrayList<>();

                    Location locationByAddress = tencentMapService.getLocationByAddress(s);

                    temp.add(locationByAddress.getLatitude());
                    temp.add(locationByAddress.getLongitude());
                    xy.add(temp);
                }
                responseContent.setPolyline(new MessageContent.Polyline(true,xy));
                sendMessage(responseContent);



            } else {
                //对话逻辑
                flux = aiService.generateRAG(content);
                //发送得到回应
                // 设置同步信号量
                Semaphore semaphore = new Semaphore(0);
                flux.delayElements(Duration.ofMillis(50)).subscribe(chunk -> {
                    sb.append(chunk);
                    responseContent.setContent(chunk);
                    sendMessage(responseContent);
                }, error -> {
                            semaphore.release();
                        }, // 失败释放信号量
                        semaphore::release// 成功释放信号量
                       );

                semaphore.acquire();
                logger.info(sb.toString());
                //持久层代码

                insertMessageAsync(new Message(openid,sb.toString(),"assistant",nowTime));

            }


        } catch (Exception e) {
            logger.error("处理消息时发生错误", e);
        }

    }

    //异步插入
    @Async
    public CompletableFuture<Void> insertMessageAsync(Message result) {
        messageMapper.insertMessage(result);
        return CompletableFuture.completedFuture(null);
    }

    // 通过 WebSocket 发送消息
    public void sendMessage(MessageContent messageContent) {
/*        AiWebsocketService aiWebsocketService = aiWebSocketServicesMap.get(id);
        if (aiWebsocketService == null) {
            logger.warn("用户ID：" + id + " 对应的连接不存在");
            return;
        }*/

        try {
            String jsonMessage = objectMapper.writeValueAsString(messageContent);
            // 发送消息
            session.getBasicRemote().sendText(jsonMessage);
            logger.debug("消息发送成功: " + jsonMessage);
        } catch (IOException e) {
            logger.error("发送消息失败: " + e.getClass() + ": " + e.getMessage());
        }

    }

    // 判断消息是否与旅游规划相关
    //可以替换为ai识别
    private boolean isTouristPlanningRelated(String content) {
        String[] keywords = {
                "规划", "设计", "建议",
                "路线", "计划", "安排", "推荐"
        };

        for (String keyword : keywords) {
            if (content.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

}
