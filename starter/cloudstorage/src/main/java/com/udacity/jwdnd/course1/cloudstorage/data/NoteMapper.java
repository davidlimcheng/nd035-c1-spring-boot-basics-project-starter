package com.udacity.jwdnd.course1.cloudstorage.data;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotes(int userId);

    @Select("SELECT * FROM NOTES where userId=#{userId} AND noteId = #{noteId}")
    Note getNote(int userId, int noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE NOTES SET notedescription = #{noteDescription}, notetitle = #{noteTitle} WHERE noteid = #{noteId} AND userid = #{userId}")
    int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    int deleteNote(int userId, int noteId);
}
