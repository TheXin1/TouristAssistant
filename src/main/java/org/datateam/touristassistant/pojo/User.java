package org.datateam.touristassistant.pojo;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String openid;
    private String avatar_url;
    private String nickname;
}
