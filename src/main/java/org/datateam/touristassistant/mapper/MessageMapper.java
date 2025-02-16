package org.datateam.touristassistant.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.datateam.touristassistant.pojo.Message;
import org.datateam.touristassistant.pojo.MessageContent;

@Mapper
public interface MessageMapper {
    void insertMessage(Message message);

    void selectMessageByOpenid(String openid);
}
