package photo_renamer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

// First Design Pattern is MVC 
// PhotoRenamer is the view which represents the visualization that model contains
// Our Controller is ImageNode which uses Tags
// And ImageNode is updated by FileNode


// Second Design Pattern is Chain of Responsibility Pattern
// PhotoRenamer is the view which represents the visualization that model contains
// Idea is PhotoRenamer uses ImageNode which uses Tags and FileNode
public class PhotoRenamer {

	// Method 1: initializing the GUI : this is the constructor.
	/**
	 * Create and return the window for the Photo Renamer.
	 *
	 * @return the window for the directory explorer
	 */

	public static Tags AvailableTags = new Tags();
	public static ArrayList<ImageNode> AvailableImages = new ArrayList<ImageNode>();
	static DefaultListModel<String> model = new DefaultListModel<String>();
	static JList<String> list = new JList<String>(model);
	static DefaultListModel<String> tagsmodel = new DefaultListModel<String>();
	static JList<String> tagslist = new JList<String>(tagsmodel);
	static DefaultListModel<String> imagemodel = new DefaultListModel<String>();
	static JList<String> imageNamelist = new JList<String>(imagemodel);
	public static String filepath = "/h/u12/c5/00/rigbygra/TestA2/imagelog.txt";
	public static String taglog = "/h/u12/c5/00/rigbygra/TestA2/taglog.txt";
	

	public static JFrame buildWindow() throws IOException, ClassNotFoundException {
		File logFile = new File("log.txt");
		if(!logFile.exists()){
			logFile.createNewFile();
		}
		filepath = (String)logFile.getAbsolutePath();
		File tagFile = new File("tags.txt");
		if(!tagFile.exists()){
			tagFile.createNewFile();
		}
		taglog = (String)tagFile.getAbsolutePath();
		JFrame directoryFrame = new JFrame("Photo Renamer");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JLabel directoryLabel = new JLabel("Select a directory");
		JLabel imageLabel = new JLabel("Select an image");
		JLabel tagLabel = new JLabel("Available Tags:");
		JLabel imagehistoryLabel = new JLabel("Image History");

		JScrollPane imageScroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		imageScroller.setPreferredSize(new Dimension(300, 600));

		JScrollPane tagsScroller = new JScrollPane(tagslist, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tagsScroller.setPreferredSize(new Dimension(300, 600));
		
		JScrollPane imageHistoryScroller = new JScrollPane(imageNamelist, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		imageHistoryScroller.setPreferredSize(new Dimension(300, 600));

		JTextField ADTags = new JTextField("Enter Tag Name");

		// create the list of available tags from log file
		AvailableTags.getAvailableTag();
		for (String tags : AvailableTags.tagList) {
			tagsmodel.addElement(tags);
		}

		JButton addTagButton = new JButton("Add a Tag");
		JButton removeTagButton = new JButton("Remove a Tag");
		addTagButton.setVerticalTextPosition(AbstractButton.CENTER);
		removeTagButton.setVerticalTextPosition(AbstractButton.CENTER);
		addTagButton.setHorizontalTextPosition(AbstractButton.LEADING);
		removeTagButton.setHorizontalTextPosition(AbstractButton.LEADING);
		JButton AddImageTag = new JButton("Add Selected Tag");
		JButton RemoveImageTag = new JButton("Remove Selected Tag");
		AddImageTag.setVerticalTextPosition(AbstractButton.CENTER);
		RemoveImageTag.setVerticalTextPosition(AbstractButton.CENTER);
		AddImageTag.setHorizontalTextPosition(AbstractButton.LEADING);
		RemoveImageTag.setHorizontalTextPosition(AbstractButton.LEADING);
		
		JButton RevertOldName = new JButton("Update Image History");
		RevertOldName.setHorizontalTextPosition(AbstractButton.LEADING);
		RevertOldName.setHorizontalTextPosition(AbstractButton.LEADING);
		
		JButton RenameImage = new JButton("Rename");
		RevertOldName.setHorizontalTextPosition(AbstractButton.LEADING);
		RevertOldName.setHorizontalTextPosition(AbstractButton.LEADING);

		//the listener for getting past names of an image
		RevertOldName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imagemodel.clear();
				for (ImageNode i : AvailableImages) {
					if (i.getName().equals(list.getSelectedValue())) {
						try {
							for(String name: i.findImageHistory()){
								imagemodel.addElement(name);
							}
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
					}
				}	
			}
		});
		
		//the listener for reverting to an older name
		RenameImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imageNamelist.getSelectedValue();
				for (ImageNode i : AvailableImages) {
					if (i.getName().equals(list.getSelectedValue())) {
						File file = new File(i.imagepath);
						i.setName(imageNamelist.getSelectedValue());
						String filelocation = i.imagepath.split("\\.")[0];
						String[] temp2 = i.getName().split("@");
						String tagsString = new String();
						for(int p = 1; p<temp2.length; p++){
							i.imagepath += "@"+temp2[p];
							tagsString += "@"+temp2[p];
						}
						tagsString = tagsString.split("\\n")[0];
						String[] extendy = i.imagepath.split("\\.");
						String fileExtension = "." + extendy[extendy.length-1];
						fileExtension = fileExtension.split("@")[0];
						filelocation = filelocation.split("@")[0];
						File file2 = new File(filelocation + tagsString + fileExtension);
						file.renameTo(file2);
						model.removeElement(list.getSelectedValue());
						model.addElement(imageNamelist.getSelectedValue());
					}
				}
			}
		});
	
		//the listener for adding a tag to available tags
		addTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (AvailableTags.tagList.contains(Tags.PREFIX + ADTags.getText())) {
					// Do Nothing
				} else {
					AvailableTags.addTag(ADTags.getText());
					tagsmodel.addElement(ADTags.getText());
					try {
						AvailableTags.serializeTag(ADTags.getText());
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		
		// The listener for removing a tag from available tags
		removeTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AvailableTags.removeTag(ADTags.getText());
				tagsmodel.removeElement(ADTags.getText());
				try {
					PrintWriter pw = new PrintWriter(taglog);
					pw.close();
					for(int i = 0; i<tagsmodel.size(); i++){
						AvailableTags.tagList.clear();
						AvailableTags.addTag(tagsmodel.getElementAt(i));
						AvailableTags.serializeTag(tagsmodel.getElementAt(i));
					}
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// The listener for adding a tag to an image
		AddImageTag.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (tagslist.getSelectedValue() != null) {
						for (ImageNode i : AvailableImages) {
							if (i.getName().equals(list.getSelectedValue())) {
								if ((i.imagepath.contains("@"+tagslist.getSelectedValue()+"@") == false) || (i.imagepath.contains("@"+tagslist.getSelectedValue()+"\\.") == false)) {
									i.addImageTag(tagslist.getSelectedValue());
									String temp = list.getSelectedValue();
									model.removeElement(list.getSelectedValue());
									model.addElement(temp + "@" + tagslist.getSelectedValue());
								}
							}
						}
					}
				} catch (ClassNotFoundException | IOException e1) {
				}
			}
		});
		
		// The listener for removing a tag to an image
		RemoveImageTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					for (ImageNode i : AvailableImages) {
						if (i.getName().equals(list.getSelectedValue())) {
							i.removeImageTag(tagslist.getSelectedValue());
							String temp = list.getSelectedValue();
							temp = temp.replace(Tags.PREFIX + tagslist.getSelectedValue(), "");
							model.removeElement(list.getSelectedValue());
							model.addElement(temp);
						}
					}
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// The directory choosing button.
		JButton openButton = new JButton("Choose Directory");
		openButton.setVerticalTextPosition(AbstractButton.CENTER);
		openButton.setHorizontalTextPosition(AbstractButton.LEADING); // aka
																		// LEFT,
																		// for
																		// left-to-right
																		// locales
		// The listener for Selecting a directory
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFileChooser();
			}

			private void showFileChooser() {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(directoryFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					AvailableImages.removeAll(AvailableImages);
					model.removeAllElements();
					File file = chooser.getSelectedFile();
					FileNode Root = new FileNode(file.getName(), null, FileType.DIRECTORY);
					try {
						FileNode.buildTree(file, Root);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					AvailableImages.addAll(Root.getChildrenNodes());
					for (ImageNode i : AvailableImages) {
						model.addElement(i.getName());
					}
				}
			}
		});
		// Put the GUI all together!
		Container c = directoryFrame.getContentPane();
		JPanel directoryChooser = new JPanel(new BorderLayout());
		JPanel imageChooser = new JPanel(new BorderLayout());
		JPanel tagsChooser = new JPanel(new BorderLayout());
		JPanel tagsbuttons = new JPanel(new BorderLayout());
		JPanel imageTagsButtons = new JPanel(new BorderLayout());
		JPanel HistoryChooser = new JPanel(new BorderLayout());
		JPanel subPanel = new JPanel(new BorderLayout());
		JPanel imageSubPanel = new JPanel(new BorderLayout());

		directoryChooser.add(directoryLabel, BorderLayout.CENTER);
		directoryChooser.add(openButton, BorderLayout.PAGE_END);

		imageChooser.add(imageLabel, BorderLayout.PAGE_START);
		imageChooser.add(imageScroller, BorderLayout.CENTER);
		imageChooser.add(imageTagsButtons, BorderLayout.PAGE_END);
		imageTagsButtons.add(AddImageTag, BorderLayout.LINE_START);
		imageTagsButtons.add(RemoveImageTag, BorderLayout.LINE_END);

		tagsChooser.add(tagsScroller, BorderLayout.CENTER);
		tagsChooser.add(tagLabel, BorderLayout.PAGE_START);
		tagsChooser.add(tagsbuttons, BorderLayout.PAGE_END);
		tagsbuttons.add(addTagButton, BorderLayout.LINE_START);
		tagsbuttons.add(ADTags, BorderLayout.PAGE_START);
		tagsbuttons.add(removeTagButton, BorderLayout.LINE_END);
		
		imageSubPanel.add(RevertOldName, BorderLayout.WEST);
		imageSubPanel.add(RenameImage, BorderLayout.EAST);
		
		HistoryChooser.add(imageHistoryScroller, BorderLayout.CENTER);
		HistoryChooser.add(imagehistoryLabel, BorderLayout.PAGE_START);
		HistoryChooser.add(imageSubPanel, BorderLayout.PAGE_END);
		
		subPanel.add(tagsChooser, BorderLayout.WEST);
		subPanel.add(HistoryChooser, BorderLayout.EAST);

		c.add(directoryChooser, BorderLayout.WEST);
		c.add(imageChooser, BorderLayout.CENTER);
		c.add(subPanel, BorderLayout.EAST);
		//c.add(imageChooser, BorderLayout.EAST);

		directoryFrame.pack();
		return directoryFrame;
	}

	/**
	 * Create and show a PhotoRenamer, which displays the contents of a
	 * directory.
	 *
	 * @param argsv
	 *            the command-line arguments.
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		PhotoRenamer.buildWindow().setVisible(true);
	}
}


