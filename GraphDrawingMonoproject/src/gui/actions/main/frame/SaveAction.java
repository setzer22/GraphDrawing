package gui.actions.main.frame;

import gui.main.frame.MainFrame;
import gui.model.GraphModel;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class SaveAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private XStream xstream;

	public SaveAction(){
		putValue(NAME, "Save");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/gui/resources/save.png")));
		putValue(SHORT_DESCRIPTION, "Save graph");
		KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		putValue(ACCELERATOR_KEY,ctrlS);
		xstream = new XStream(new StaxDriver());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (MainFrame.getInstance().getCurrentView() == null)
			return;
		GraphModel currentModel = MainFrame.getInstance().getCurrentView().getModel();
		JFileChooser fileChooser = new JFileChooser();
		File f;
		if (fileChooser.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION){
			f = fileChooser.getSelectedFile();
		}
		else
			return;
		
		
		String xml;
		String fileName = "";
		try {
			fileName = f.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (!fileName.endsWith(".graph"))
			fileName += ".graph";
		
		FileOutputStream fos = null;
		    try{            
		        xml = xstream.toXML(currentModel);
		        fos = new FileOutputStream(fileName);
		        byte[] bytes = xml.getBytes("UTF-8");
		        fos.write(bytes);

		    }catch (Exception e){
		        System.err.println("Error in XML Write: " + e.getMessage());
		    }
		    finally{
		        if(fos != null){
		            try{
		                fos.close();
		            }catch (IOException e) {
		                e.printStackTrace();
		            }
		        }
		    }
			MainFrame.getInstance().renameCurrentView(f.getName());
	}

}
