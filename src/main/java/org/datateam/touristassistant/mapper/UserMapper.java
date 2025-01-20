package org.datateam.touristassistant.mapper;

import org.apache.ibatis.annotations.*;
import org.datateam.touristassistant.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users(username, password_hash, email, phone) VALUES(#{username}, #{passwordHash}, #{email}, #{phone})")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User getUserById(Long userId);

    @Select("SELECT * FROM users")
    List<User> getAllUsers();

    @Update("UPDATE users SET username = #{username}, password_hash = #{passwordHash}, email = #{email}, phone = #{phone}, updated_at = CURRENT_TIMESTAMP WHERE user_id = #{userId}")
    void updateUser(User user);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    void deleteUser(Long userId);
}
