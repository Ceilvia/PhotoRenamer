package photo_renamer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import photo_renamer.FileType;
import photo_renamer.FileNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Model1 ImageNode is used by PhotoRenamer  
// Model2 ImageNode is used by PhotoRenamer
public class ImageNode extends FileNode {
	private Tags imageTags = new Tags();
	private String name;
	private FileNode parent;
	private FileType type;
	public String imagepath;

	/**
	 * Constructor for class File_Node, this
	 * 
	 * @param name
	 *            the name of the image or directory file
	 * @param parent
	 *            the parent node
	 * @param type
	 *            the FileType of the node (image or directory)
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ImageNode(String name, FileNode parent, FileType type, String imagepath)
			throws ClassNotFoundException, IOException {
		super(name, parent, type);
		this.name = super.getName();
		if (type == FileType.IMAGE) {
			updateImageTags();
		} else {
			throw new IllegalArgumentException("FileType should be IMAGE");
		}
		this.imagepath = imagepath;
	}

	/**
	 * rename the file represented by the ImageNode to the name that the
	 * ImageNode currently have.
	 * 
	 * @param image
	 * @throws IOException
	 */
	public void fileRenaming() throws IOException {
		File file = new File(this.imagepath);
		String[] temp = this.imagepath.split("\\.");
		String fileExtension = temp[temp.length - 1];
		
		String filelocation = this.imagepath.split("\\.")[0];
		filelocation = filelocation.split("@")[0];

		File file2 = new File(filelocation + this.imageTags.getTagsString() + "." + fileExtension);
		file.renameTo(file2);
	}

	/**
	 * Updates the ImageNode's imageTags by checking the name for it's current
	 * tags.
	 */
	public void updateImageTags() {
		String[] parts = this.name.split("@");
		for (int i = 1; i < parts.length; i++) {
			imageTags.addTag(parts[i]);
		}
	}

	/**
	 * Adds a specified tag to the ImageNode's tags and updates the ImageNode's
	 * file name accordingly
	 * 
	 * @param imageTag
	 *            the name of the tag being added to the image
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void addImageTag(String imageTag) throws IOException, ClassNotFoundException {
		if(imageTag.length() >= 1){
			this.serializeNode();
			this.setName(this.getName() + Tags.PREFIX + imageTag);
			this.imageTags.addTag(imageTag);
			this.fileRenaming();
			String[] temp = this.imagepath.split("\\.");
			String fileExtension = temp[temp.length - 1];
			this.imagepath = temp[0] + Tags.PREFIX + imageTag + "." + fileExtension;
		}
	}

	/**
	 * Removes a specified tag from the ImageNode's tags and updates the
	 * ImgaeNode's file name accordingly
	 * 
	 * @param imageTag
	 *            the name of tag being removed from the image
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void removeImageTag(String imageName) throws IOException, ClassNotFoundException {
		this.setName(this.getName().replace(Tags.PREFIX + imageName, ""));
		this.imageTags.removeTag(imageName);
		this.fileRenaming();
		this.serializeNode();
		this.imagepath = (this.imagepath.replace(Tags.PREFIX + imageName, ""));
	}

	/**
	 * Return all the name that an image file represented by an ImageNode used
	 * to have.
	 * 
	 * @param image
	 * @return String
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public List<String> findImageHistory() throws ClassNotFoundException, IOException {
		// deserializes the serialized log file
		String logline = this.DeserializeNode();
		// splits logline by ImageNode log entries
		String[] logdata = logline.split("-");
		ArrayList<String> returnStr = new ArrayList<String>();
		// iterates through each ImageNode log entry in logdata
		for (String item : logdata) {
			// splits timestamp and name of the node including the tags
			String[] split1 = item.split("_");
			if (split1.length > 1) {
				// splits names and tags
				String[] split2 = split1[1].split("@");
				// compares ImageNode log entry name with name of ImageNode
				// you're finding history for
				String[] temp = this.name.split("@");
				if (split2[0].equals(temp[0])) {
					// adds name to the returnStr
					if (split1.length > 1) {
						returnStr.add(split1[split1.length - 1] + "\n");
					}
				}
			}
		}
		return returnStr;
	}

	/**
	 * stores the current name of images with it's tags and timestamp in to a
	 * file that keeps track of all the changes in tags.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void serializeNode() throws IOException, ClassNotFoundException {
		String history = "";
		try{
			history = this.DeserializeNode();
		}catch (EOFException e){
			System.out.println("creating log.txt...");
		}
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		OutputStream file = new FileOutputStream(PhotoRenamer.filepath);
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutputStream output = new ObjectOutputStream(buffer);
		output.reset();
		String out = history + "-" + timeStamp + "_" + this.name + this.imageTags.getTagsString() + "\n";
		output.writeObject(out);
		output.close();
	}

	/**
	 * Returns a string representation of the content of the file represented by
	 * filepath
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String DeserializeNode() throws ClassNotFoundException, IOException {
		FileInputStream file = new FileInputStream(PhotoRenamer.filepath);
		File file2 = new File(PhotoRenamer.filepath);
		boolean empty = file2.length() == 0;
		if(empty == false){
			InputStream buffer = new BufferedInputStream(file);
			ObjectInputStream input = new ObjectInputStream(buffer);
			String history = (String)input.readObject();
			buffer.close();
			return history;
		}
		file.close();
		return "";
	}
}
