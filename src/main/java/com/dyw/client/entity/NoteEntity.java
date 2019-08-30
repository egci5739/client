package com.dyw.client.entity;

public class NoteEntity {
    private int noteId;
    private String noteName;
    private int noteType;
    private int noteCode;
    private int noteRank;
    private int relativeId;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
    }

    public int getNoteCode() {
        return noteCode;
    }

    public void setNoteCode(int noteCode) {
        this.noteCode = noteCode;
    }

    public int getNoteRank() {
        return noteRank;
    }

    public void setNoteRank(int noteRank) {
        this.noteRank = noteRank;
    }

    public int getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(int relativeId) {
        this.relativeId = relativeId;
    }
}
