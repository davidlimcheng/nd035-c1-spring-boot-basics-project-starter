package com.udacity.jwdnd.course1.cloudstorage.data;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userId = #{userId}")
    Credential[] getCredentials(int userId);

    @Select("SELECT * FROM CREDENTIALS where userId = #{userId} AND credentialId = #{credentialId}")
    Credential getCredential(int userId, int credentialId);

    @Delete("DELETE FROM CREDENTIALS WHERE userId = #{userId} AND credentialId = #{credentialId}")
    int deleteCredential(int userId, int credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialId = #{credentialId} AND #{userId} = #{userId}")
    int updateCredential(Credential credential);
}
