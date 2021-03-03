package gui.state;

import gui.main.frame.MainFrame;
import gui.model.GraphEdge;
import gui.model.GraphVertex;
import gui.model.IGraphElement;
import gui.model.LinkNode;
import gui.properties.PropertiesFactory;
import gui.view.GraphView;

import java.awt.event.MouseEvent;

public class SelectState extends State{

	
	public SelectState(GraphView view) {
		this.view = view;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		MainFrame.getInstance().setShowPopup(true);
		IGraphElement hitElement = view.elementAtPoint(e.getPoint());
		if (hitElement != null){
			LinkNode hitNode;
			if (hitElement instanceof LinkNode){
				hitNode = (LinkNode) hitElement;
				hitElement = hitNode.getEdge();
				view.getSelectionModel().setSelectedNode(hitNode);
			}
			
			if (hitElement instanceof GraphVertex){
				GraphVertex hitVertex = (GraphVertex)hitElement;
				if (e.isControlDown()){
					if (view.getSelectionModel().isSelected(hitVertex))
						view.getSelectionModel().removeVertexFromSelection(hitVertex);
					else
						view.getSelectionModel().addVertexToSelection(hitVertex);
				}
				else{
					if (view.getSelectionModel().getSelectedVertices().size() == 0)
						view.getSelectionModel().selecteVertex(hitVertex);
				}
			}
			
			else if (hitElement instanceof GraphEdge){
				GraphEdge hitEdge = (GraphEdge)hitElement;	
				if (e.isControlDown()){
					if (view.getSelectionModel().isSelected(hitEdge))
						view.getSelectionModel().removeEdgeFromSelection(hitEdge);
					else
						view.getSelectionModel().addEdgeToSelection(hitEdge);
				}
				else
					view.getSelectionModel().selecteEdge(hitEdge);
			}
		}
		else
			view.getSelectionModel().clearSelection();
		
		boolean clearPanel = true;
		if (view.getSelectionModel().getSelectedVertices().size() == 1){
			clearPanel = false;
			MainFrame.getInstance().setPropertiesPanel(PropertiesFactory.getPropertiesPanel(view.getSelectionModel().getSelectedVertices().get(0)));
		}
		else if (view.getSelectionModel().getSelectedEdges().size() == 1){
			clearPanel = false;
			MainFrame.getInstance().setPropertiesPanel(PropertiesFactory.getPropertiesPanel(view.getSelectionModel().getSelectedEdges().get(0)));
		}
		if (clearPanel)
			MainFrame.getInstance().setPropertiesPanel(PropertiesFactory.getPropertiesPanel(view.getModel()));
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		IGraphElement hitElement = view.elementAtPoint(e.getPoint());
		if (hitElement != null){
			if (hitElement instanceof GraphVertex){
				GraphVertex hitVertex = (GraphVertex)hitElement;
				if (!e.isControlDown()){
					if (view.getSelectionModel().getSelectedVertices().size() > 0)
						view.getSelectionModel().selecteVertex(hitVertex);
				}
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		IGraphElement hitElement = view.elementAtPoint(e.getPoint());
		if (hitElement == null){
			//lasso
			MainFrame.getInstance().changeToLassoSelect();
		}
		else{
			if (hitElement instanceof LinkNode)
				MainFrame.getInstance().changeToMoveNodeState(e.getPoint());
			else if (hitElement instanceof GraphVertex)
				MainFrame.getInstance().changeToMoveState(e.getPoint());
		}
		
	}
}
