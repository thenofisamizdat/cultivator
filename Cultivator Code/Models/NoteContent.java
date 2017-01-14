package com.example.myapplication2.app.Models;

import java.io.Serializable;

/**
 * Neil Byrne - 2015
 * NoteContent contains all data for each image content item that is created.
 */
public class NoteContent implements Serializable{

    private String note, noteID;

    public NoteContent(){}

    public NoteContent(String _note){
        note = _note;
        noteID = createUniqueContentID(_note, Storage.getInstance().getUid());
    }

    private String createUniqueContentID(String _description, String _uid){
        Long unixTime = System.currentTimeMillis() / 1000L;
        String id = unixTime.toString() + _description + _uid;
        return id;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
