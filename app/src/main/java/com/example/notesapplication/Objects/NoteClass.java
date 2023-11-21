package com.example.notesapplication.Objects;

import com.google.firebase.Timestamp;

public class NoteClass {

    String noteTitle;
    String noteContent;
    Timestamp timestamp;

    public NoteClass(String noteTitle, String noteContent, Timestamp timestamp) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.timestamp = timestamp;
    }
    public NoteClass(){

    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
