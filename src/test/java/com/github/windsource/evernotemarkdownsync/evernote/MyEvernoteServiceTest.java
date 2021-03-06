package com.github.windsource.evernotemarkdownsync.evernote;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.github.windsource.evernotemarkdownsync.evernote.MyEvernoteService;

public class MyEvernoteServiceTest {
	// A sandbox token just for test
	static final String token = "S=s1:U=958f1:E=1757e169412:C=16e266566d0:P=1cd:A=en-devtoken:V=2:H=396128d9e470bd02311b195c8b494e81";

	final static String noteContentPath = "/noteContent.enml";
	static String noteContent = "";
			
	@BeforeClass
	public static void oneTimeSetUp() throws IOException, URISyntaxException {
		URL url = MyEvernoteServiceTest.class.getResource(noteContentPath);
		byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
		noteContent = new String(encoded, "UTF-8");
	}
	
	@Test
	public void testMyEvernoteService() throws Exception {
		new MyEvernoteService(token, true);
	}

	@Test
	public void testCreateNote() throws Exception {
		MyEvernoteService service = new MyEvernoteService(token, true);
		service.createNote("My new note", noteContent, null, "");
	}
	
	@Test
	public void testCreateNoteWithCreationDate() throws Exception {
		MyEvernoteService service = new MyEvernoteService(token, true);
		DateTime date = new DateTime("2015-10-25T17:11:25+01:00");
		Note note = service.createNote("My new note", noteContent, date.getMillis(), "");
		assertEquals(note.getCreated(), date.getMillis());
		assertEquals(note.getUpdated(), date.getMillis());
	}
	
	@Test
	public void testCreateNoteWithSource() throws Exception {
		MyEvernoteService service = new MyEvernoteService(token, true);
		String source = "My great app.abc";
		Note note = service.createNote("My new note", noteContent, null, source);
		assertEquals(note.getAttributes().getSource(), source);
	}
	
	// The following test fails for unknown reason. See also
	// * https://github.com/evernote/evernote-sdk-java/issues/28
	// * https://stackoverflow.com/questions/58658429/evernote-java-sdk-notestoreclient-findnotes-does-not-find-notes
	// 
	// @Test
	// public void testFindNoteBySource() throws Exception {
	// 	String source = UUID.randomUUID().toString();
		
	// 	// create a note
	// 	MyEvernoteService service = new MyEvernoteService(token, true);
	// 	Note note = service.createNote("My new note", noteContent, null, source);
		
	// 	// now search for it
	// 	NoteList noteList = service.findNoteBySource(source);
	// 	assertEquals(noteList.getNotesSize(), 1);
	// 	assertEquals(noteList.getNotes().get(0).getGuid(), note.getGuid());
	// }
}
