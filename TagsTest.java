package photo_renamer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

//these are our Unit tests for Tags class
public class TagsTest {

	@Test
	public void testaddTag() {
		Tags apple = new Tags();
		String newstr = new String("red");
		apple.addTag(newstr);
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("@red");
		assertTrue(apple.tagList.equals(answer));
	}

	@Test
	public void testaddTag2() {
		Tags apple = new Tags();
		String newstr = new String("red");
		String newstr2 = new String("red");
		apple.addTag(newstr);
		apple.addTag(newstr2);
		ArrayList<String> answer2 = new ArrayList<String>();
		answer2.add("@red");
		assertTrue(apple.tagList.equals(answer2));
	}

	@Test
	public void testremoveTag() {
		Tags apple = new Tags();
		String newstr = new String("red");
		apple.addTag(newstr);
		String newstr2 = new String("red");
		apple.removeTag(newstr2);
		ArrayList<String> answer = new ArrayList<String>();
		assertTrue(apple.tagList.equals(answer));
	}

	@Test
	public void testremoveTag2() {
		Tags apple = new Tags();
		String newstr = new String("red");
		String newstr3 = new String("yellow");
		apple.addTag(newstr);
		apple.addTag(newstr3);
		String newstr2 = new String("red");
		apple.removeTag(newstr2);
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("@yellow");
		assertTrue(apple.tagList.equals(answer));
	}

	@Test
	public void testgetTagAtIndex() {
		Tags apple = new Tags();
		String newstr = new String("red");
		String newstr3 = new String("yellow");
		String newstr4 = new String("green");
		apple.addTag(newstr);
		apple.addTag(newstr3);
		apple.addTag(newstr4);
		System.out.println(apple.getTagAtIndex(2));
		assertTrue(apple.getTagAtIndex(2).equals("@green"));
	}

	@Test
	public void testgetTagsLength() {
		Tags apple = new Tags();
		String newstr = new String("red");
		String newstr3 = new String("yellow");
		String newstr4 = new String("green");
		apple.addTag(newstr);
		apple.addTag(newstr3);
		apple.addTag(newstr4);
		assertEquals(apple.getTagsLength(), 3);

	}

	@Test
	public void testgetTagsArray() {
		Tags apple = new Tags();
		String newstr = new String("red");
		String newstr3 = new String("yellow");
		String newstr4 = new String("green");
		apple.addTag(newstr);
		apple.addTag(newstr3);
		apple.addTag(newstr4);
		assertTrue(apple.getTagsArray()[0].equals("@red"));
		assertTrue(apple.getTagsArray()[1].equals("@yellow"));
		assertTrue(apple.getTagsArray()[2].equals("@green"));
	}

	@Test
	public void testgettagsString() {
		Tags apple = new Tags();
		String newstr = new String("red");
		String newstr3 = new String("yellow");
		String newstr4 = new String("green");
		apple.addTag(newstr);
		apple.addTag(newstr3);
		apple.addTag(newstr4);
		String newstr6 = "@red@yellow@green";
		assertEquals(apple.getTagsString(), newstr6);
	}
}