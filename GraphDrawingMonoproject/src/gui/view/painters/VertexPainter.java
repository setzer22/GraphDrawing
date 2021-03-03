package gui.view.painters;

import gui.model.GraphVertex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class VertexPainter implements IElementPainter {

	private GraphVertex vertex;
	private Shape shape;
	private BasicStroke stroke = new BasicStroke(3);
	
	public VertexPainter(GraphVertex vertex){
		this.vertex = vertex;
	}

	public void paint(Graphics2D g){
		
		g.setStroke(stroke);
		
		calculateVertexRadius(g.getFontMetrics());
		int x = (int) vertex.getPosition().getX();
		int y = (int) vertex.getPosition().getY();
		
		int r = vertex.getSize().width;
		x -= r/2;
		y -= r/2;
		
		shape = new Ellipse2D.Double(x,y,r,r);
		g.setColor(Color.BLACK);
		g.draw(shape);
		g.setColor(vertex.getColor());
		g.fill(shape);
		g.setColor(Color.BLACK);
		
		Dimension textDim = PaintingUtil.calculateStringDimension(g.getFontMetrics(), 
				(String)vertex.getContent());
		
		
		int startX = x + r/2  - (int)textDim.getWidth()/2;
		int startY = y + r/2 + (int)textDim.getHeight()/4; 
		
		g.drawString((String) vertex.getContent(),startX, startY);
	}
	
	private void calculateVertexRadius(FontMetrics fontMetrics){
		
		Dimension textDim = PaintingUtil.calculateStringDimension(fontMetrics, 
				(String)vertex.getContent());

		int width = (int) textDim.getWidth();
		int height = (int) textDim.getHeight();
		int radius = Math.max(width, height);
		//padding
		radius += 10;
		vertex.setSize(new Dimension(radius, radius));
		
	}
	
	public boolean containsPoint(Point2D point){
		int x = (int) point.getX();
		int y = (int) point.getY();
		
		Point2D center = vertex.getPosition();
		int a = (int) Math.pow(x - center.getX(), 2);
		int b = (int) Math.pow(y - center.getY(), 2);
		int c = (int) Math.pow(vertex.getSize().getWidth()/2, 2);
		return a + b <= c;
				
	}
	
	public Rectangle getBounds(){
		return shape.getBounds();
	}
	

	public GraphVertex getVertex() {
		return vertex;
	}

	public void setVertex(GraphVertex vertex) {
		this.vertex = vertex;
	}
}
