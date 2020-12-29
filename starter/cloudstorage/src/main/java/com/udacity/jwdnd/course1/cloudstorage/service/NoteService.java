package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.data.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.data.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper, UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public Note[] getAllUserNotes(String username) {
        User user = userMapper.getUser(username);
        return noteMapper.getNotes(user.getUserId());
    }

    public int addNote(NoteForm noteData, String username) {
        User user = userMapper.getUser(username);

        Note newNote = new Note();
        newNote.setUserId(user.getUserId());
        newNote.setNoteTitle(noteData.getTitle());
        newNote.setNoteDescription(noteData.getDescription());

        return noteMapper.insert(newNote);
    }

    public int deleteNote(int noteId, String username) {
        User user = userMapper.getUser(username);

        return noteMapper.deleteNote(user.getUserId(), noteId);
    }

    public int editNote(NoteForm noteData, String username) {
        User user = userMapper.getUser(username);
        Note note = noteMapper.getNote(user.getUserId(), Integer.parseInt(noteData.getStringId()));

        note.setNoteTitle(noteData.getTitle());
        note.setNoteDescription(noteData.getDescription());

        return noteMapper.updateNote(note);
    }
}
