package photo_renamer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

//model1 tags is used by imageNode
//model2 tags is used by imageNode
public class Tags {
	public static final String PREFIX = new String("@");
	public ArrayList<String> tagList = new ArrayList<String>();
	
	/**
	 * Add a new Tag to this ArrayList of tags if the tag don't already exist in the ArrayList.
	 * @param tagName
	 */
	public void addTag(String tagName) {
		if (tagList.contains(PREFIX + tagName) != true) {
			tagList.add(PREFIX + tagName);
		}
	}
	
	/**
	 * returns a list of the available tags stored in our taglog.txt file
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void getAvailableTag() throws ClassNotFoundException, IOException{
		String tagString = "";
		try{
			tagString = this.DeserializeTag();
		}catch (EOFException e){
			System.out.println("creating tags.txt...");
		}
		String[] listTag = tagString.split("\n");
		for (String tag : listTag){
			this.tagList.add(tag);
		}
	}
	
	/**
	 * stores the current name of tags in to a
	 * file that keeps track of all the available tags.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void serializeTag(String tagName) throws IOException, ClassNotFoundException {
		String history = "";
		try{
			history = this.DeserializeTag();
		}catch (EOFException e){
		}
		
		OutputStream file = new FileOutputStream(PhotoRenamer.taglog);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutputStream output = new ObjectOutputStream(buffer);
		output.reset();
		String out = history + tagName + "\n";
		output.writeObject(out);
		output.close();
	}

	/**
	 * Returns a string representation of the content of the tag log file
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String DeserializeTag() throws ClassNotFoundException, IOException {
		FileInputStream file = new FileInputStream(PhotoRenamer.taglog);
		InputStream buffer = new BufferedInputStream(file);
		ObjectInputStream input = new ObjectInputStream(buffer);
		String history = (String)input.readObject();
		buffer.close();
		return history;
	}
	
	/**
	 * remove a tag from the ArrayList of tags
	 * @param tagName
	 */
	public void removeTag(String tagName) {
		tagList.remove(PREFIX + tagName);
	}
	
	/**
	 * return the name of the tag at index i
	 * @param i
	 * @return name of the tag at index i
	 */
	public String getTagAtIndex(int i) {
		return tagList.get(i);
	}
	
	/**
	 * get the number of tags in the ArrayList of tags
	 * @return the length of taglist
	 */
	public int getTagsLength() {
		return tagList.size();
	}
	
	/**
	 * Return a list containing all tags
	 * @return a 
	 */
	public String[] getTagsArray() {
		String tagsNames = new String();
		for (int i = 0; i < tagList.size(); i++) {
			tagsNames += tagList.get(i) + " ";
		}
		String[] tags = tagsNames.split(" ");
		return tags;
	}
	
	/**
	 * return a string representation of the ArrayList of tags
	 * @return string representation of tags 
	 */
	public String getTagsString() {
		String tagsNames = new String();
		for (int i = 0; i < tagList.size(); i++) {
			tagsNames += tagList.get(i);
		}
		return tagsNames;
	}

	public static void main(String[] args) {
	}
}
