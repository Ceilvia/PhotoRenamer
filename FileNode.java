package photo_renamer;
//add documentation
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import javax.activation.MimetypesFileTypeMap;
import java.util.ArrayList;

//Model1 FileNode updates ImageNode
//Model2 FileNode is used by ImageNode
public class FileNode {
	private String name;
	private FileNode parent;
	public FileType type;
	public Map<String, FileNode> children;
	private FileNode getChildrenNodes;
	
	/**
	 * Constructor for class File_Node, this 
	 * 
	 * @param name
	 *            the name of the image or directory file
	 * @param parent
	 * 			  the parent node
	 * @param type
	 * 			  the FileType of the node (image or directory)
	 */
	public FileNode(String name, FileNode parent, FileType type) {
		this.name = name;
		this.parent = parent;
		this.type = type;
		this.children = new HashMap<String, FileNode>();
	}
	
	/**
	 * returns the name of the fileNode
	 * @return String
	 */
	public String getName(){
		return name;
	}
	/**
	 * sets the name of the fileNode
	 * @return String
	 */
	public void setName(String newname){
		this.name = newname;
	}
	/**
	 * return a string representation of all children nodes names
	 * @return String
	 */
	public String getChildrenString(){
		String childrenString = "";
		for (String name : this.children.keySet()) {
		    childrenString += name + " ";
		}
		return childrenString;
	}
	
	/**
	 * Returns an arraylist of all children nodes under a FileNode
	 * @return ArrayList<FileNode>
	 */
	public ArrayList<ImageNode> getChildrenNodes(){
		ArrayList<ImageNode> nodes = new ArrayList<ImageNode>();
		for (FileNode node : this.children.values()) {
		    if(node.type == FileType.DIRECTORY){
		    	nodes.addAll(node.getChildrenNodes());
		    }
		    else {
		    	nodes.add((ImageNode) node);
		    }
		}
		return nodes;
	}
	
	/**
	 * Add childNode, representing a image or directory named name, as a child of
	 * this node.
	 * 
	 * @param name
	 *            the name of the image or directory
	 * @param childNode
	 *            the node to add as a child
	 */
	private void addChild(String name, FileNode childNode) {
		this.children.put(name, childNode);
	}
	
	/**
	 * Build the tree of nodes rooted at file in the file system; note curr is
	 * the FileNode corresponding to file, so this only adds nodes for children
	 * of file that are either directory or image to the tree. Precondition: file represents a directory.
	 * 
	 * @param file
	 *            the file or directory we are building
	 * @param curr
	 *            the node representing file
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void buildTree(File file, FileNode curr) throws ClassNotFoundException, IOException {
		File[] unfilteredfilearray = file.listFiles();
		ArrayList<File> filearray = new ArrayList<File>();
		for (int i = 0; i < unfilteredfilearray.length; i++){
			String mimetype = new MimetypesFileTypeMap().getContentType(unfilteredfilearray[i]);
			String type = mimetype.split("/")[0];
			if(type.equals("image") || unfilteredfilearray[i].isDirectory())
				filearray.add(unfilteredfilearray[i]);
		}
		for (int i=0; i<filearray.size();i++){
			if (filearray.get(i).isDirectory()){
				FileNode kids = new FileNode(filearray.get(i).getName(), curr, FileType.DIRECTORY);
				curr.addChild(filearray.get(i).getName(), kids);
				buildTree(filearray.get(i), kids);
			}
			else{
				String name = filearray.get(i).getName();
				int pos = name.lastIndexOf(".");
				if (pos > 0) {
				    name = name.substring(0, pos);
				}
				ImageNode kids = new ImageNode(name, curr, FileType.IMAGE, filearray.get(i).getPath());
				curr.addChild(filearray.get(i).getName(), kids);
			}
		}
	}
	
	//test
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		ImageNode Phil = new ImageNode("ya", null, FileType.IMAGE, "a");
		ImageNode Phi = new ImageNode("y", null, FileType.IMAGE, "a");
		Phil.addImageTag("qqq");
		Phil.addImageTag("whoop");
		Phil.addImageTag("Nice");
		Phi.addImageTag("hey");
		System.out.println(Phil.DeserializeNode());
		System.out.println(Phil.findImageHistory());
	}
}