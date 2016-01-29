package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import cs355.GUIFunctions;
import cs355.model.drawing.*;

public class Controller implements CS355Controller {

	private boolean shapeSelected = false;
	private boolean triangleActive = false;
	private ArrayList<Point2D> triangleCoordinates = new ArrayList<>();
	
	/* Mouse Events */
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		//TODO
	}
	
	private double calculateCenterTriangle(double coord1, double coord2, double coord3) {
		return ((coord1 + coord2 + coord3) / 3);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (triangleActive) {
			Point2D.Double point = new Point2D.Double(arg0.getX(), arg0.getY());
			this.triangleCoordinates.add(point);
			
			if (this.triangleCoordinates.size() == 3) 
			{
				Point2D.Double point1 = new Point2D.Double(this.triangleCoordinates.get(0).getX(), this.triangleCoordinates.get(0).getY());
				Point2D.Double point2 = new Point2D.Double(this.triangleCoordinates.get(1).getX(), this.triangleCoordinates.get(1).getY());
				Point2D.Double point3 = new Point2D.Double(this.triangleCoordinates.get(2).getX(), this.triangleCoordinates.get(2).getY());
								
				Point2D.Double center = new Point2D.Double(this.calculateCenterTriangle(point1.getX(), point2.getX(), point3.getX()),
						this.calculateCenterTriangle(point1.getY(), point2.getY(), point3.getY()));
				
				Triangle triangle = new Triangle(Model.instance().getColor(), center, point1, point2, point3);
				Model.instance().addShape(triangle);
				this.triangleCoordinates.clear();
				Model.instance().changeMade();
			}
		}
		else {
			if(!shapeSelected) {
				
				switch(Model.instance().getCurrentShape()) {
				case LINE:
					Model.instance().addShape(new Line(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()), new Point2D.Double(arg0.getX(), arg0.getY())));
					shapeSelected = true;
					break;
				case SQUARE: 
					Model.instance().addShape(new Square(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()), 0));
					shapeSelected = true;
					break;
				case RECTANGLE: 
					Model.instance().addShape(new Rectangle(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()),0, 0));
					shapeSelected = true;
					break;
				case CIRCLE: 
					Model.instance().addShape(new Circle(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()),0));
					shapeSelected = true;
					break;
				case ELLIPSE: 
					Model.instance().addShape(new Ellipse(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()),0, 0));
					shapeSelected = true;
					break;
				case TRIANGLE: 
					break;
				case NONE:
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(shapeSelected) {
			Shape currentShape = Model.instance().getLastShape();
			
			switch(currentShape.getShapeType()) {
			case LINE:
				handleActiveLine(arg0);
				break;
			case SQUARE:
				handleActiveSquare(arg0);
				break;
			case RECTANGLE:
				handleActiveRectangle(arg0);
				break;
			case CIRCLE:
				handleActiveCircle(arg0);
				break;
			case ELLIPSE:
				handleActiveEllipse(arg0);
				break;
			case TRIANGLE:
				break;
			case NONE:
				break;
			default:
				break;
			}
			GUIFunctions.refresh();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (shapeSelected){
			shapeSelected = false;
			//call set center on the shapes
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	/* Button Events */
	
	@Override
	public void colorButtonHit(Color c) {
		Model.instance().setColor(c);
		GUIFunctions.changeSelectedColor(c);
	}

	@Override
	public void lineButtonHit() {
		Model.instance().setCurrentShape(Shape.type.LINE);
		if(triangleActive) triangleActiveCleanUp();
	}

	@Override
	public void squareButtonHit() {
		Model.instance().setCurrentShape(Shape.type.SQUARE);
		if(triangleActive) triangleActiveCleanUp();
	}

	@Override
	public void rectangleButtonHit() {
		Model.instance().setCurrentShape(Shape.type.RECTANGLE);
		if(triangleActive) triangleActiveCleanUp();
	}

	@Override
	public void circleButtonHit() {
		Model.instance().setCurrentShape(Shape.type.CIRCLE);
		if(triangleActive) triangleActiveCleanUp();
	}

	@Override
	public void ellipseButtonHit() {
		Model.instance().setCurrentShape(Shape.type.ELLIPSE);
		if(triangleActive) triangleActiveCleanUp();
	}

	@Override
	public void triangleButtonHit() {
		Model.instance().setCurrentShape(Shape.type.TRIANGLE);
		triangleActive = true;
	}
	
	public void triangleActiveCleanUp() {
		this.triangleActive = false;
		this.triangleCoordinates.clear();
	}
	
	/* Shape Handlers */
		
	public void handleActiveLine(MouseEvent arg0)
	{		
		Line line = (Line) Model.instance().getLastShape();
		line.setEnd(new Point2D.Double(arg0.getX(), arg0.getY()));
		
		Model.instance().setLastShape(line);
	}
	
	public void handleActiveSquare(MouseEvent arg0)
	{
		
		Square square = (Square) Model.instance().getLastShape();
		//if the cursor is moving below the upper left corner
		if(arg0.getY() > square.getCenter().y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > square.getCenter().x)
			{
				double lengthX = arg0.getX() - square.getCenter().x;
				double lengthY = arg0.getY() - square.getCenter().y;
				double newcorner = Math.min(lengthX, lengthY);
				
				square.setUpperLeft(square.getCenter());
				square.setSize(newcorner);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < square.getCenter().x)
			{
				double lengthX = square.getCenter().x - arg0.getX();
				double lengthY = arg0.getY() - square.getCenter().y;
				double newcorner = Math.min(lengthX, lengthY);
				
				square.setUpperLeft(new Point2D.Double(square.getCenter().x - newcorner, square.getCenter().y));
				square.setSize(newcorner);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < square.getCenter().y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > square.getCenter().x)
			{
				double lengthX = arg0.getX() - square.getCenter().x;
				double lengthY = square.getCenter().y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				square.setUpperLeft(new Point2D.Double(square.getCenter().x, square.getCenter().y  - newcorner));
				square.setSize(newcorner);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < square.getCenter().x)
			{
				double lengthX = square.getCenter().x - arg0.getX();
				double lengthY = square.getCenter().y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				square.setUpperLeft(new Point2D.Double(square.getCenter().x - newcorner, square.getCenter().y - newcorner));
				square.setSize(newcorner);
			}
		}
		Model.instance().setLastShape(square);
	}
	
	public void handleActiveRectangle(MouseEvent arg0)
	{
		
		Rectangle rectangle = (Rectangle) Model.instance().getLastShape();
		//if the cursor is moving below the upper left corner
		if(arg0.getY() > rectangle.getCenter().y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > rectangle.getCenter().x)
			{
				double lengthX = arg0.getX() - rectangle.getCenter().x;
				double lengthY = arg0.getY() - rectangle.getCenter().y;
				
				rectangle.setUpperLeft(rectangle.getCenter());
				rectangle.setHeight(lengthY);
				rectangle.setWidth(lengthX);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < rectangle.getCenter().x)
			{
				double lengthX = rectangle.getCenter().x - arg0.getX();
				double lengthY = arg0.getY() - rectangle.getCenter().y;
				
				rectangle.setUpperLeft(new Point2D.Double(rectangle.getCenter().x - lengthX, rectangle.getCenter().y));
				rectangle.setHeight(lengthY);
				rectangle.setWidth(lengthX);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < rectangle.getCenter().y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > rectangle.getCenter().x)
			{
				double lengthX = arg0.getX() - rectangle.getCenter().x;
				double lengthY = rectangle.getCenter().y - arg0.getY();
				
				rectangle.setUpperLeft(new Point2D.Double(rectangle.getCenter().x, rectangle.getCenter().y  - lengthY));
				rectangle.setHeight(lengthY);
				rectangle.setWidth(lengthX);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < rectangle.getCenter().x)
			{
				double lengthX = rectangle.getCenter().x - arg0.getX();
				double lengthY = rectangle.getCenter().y - arg0.getY();
				
				rectangle.setUpperLeft(new Point2D.Double(rectangle.getCenter().x - lengthX, rectangle.getCenter().y - lengthY));
				rectangle.setHeight(lengthY);
				rectangle.setWidth(lengthX);
			}
		}
		Model.instance().setLastShape(rectangle);
	}
	
	public void handleActiveCircle(MouseEvent arg0)
	{
		
		Circle circle = (Circle) Model.instance().getLastShape();
		//if the cursor is moving below the upper left corner
		if(arg0.getY() > circle.getCenter().y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > circle.getCenter().x)
			{
				double lengthX = arg0.getX() - circle.getCenter().x;
				double lengthY = arg0.getY() - circle.getCenter().y;
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setUpperLeft(circle.getCenter());
				circle.setRadius(newcorner / 2);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < circle.getCenter().x)
			{
				double lengthX = circle.getCenter().x - arg0.getX();
				double lengthY = arg0.getY() - circle.getCenter().y;
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setUpperLeft(new Point2D.Double(circle.getCenter().x - newcorner, circle.getCenter().y));
				circle.setRadius(newcorner / 2);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < circle.getCenter().y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > circle.getCenter().x)
			{
				double lengthX = arg0.getX() - circle.getCenter().x;
				double lengthY = circle.getCenter().y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setUpperLeft(new Point2D.Double(circle.getCenter().x, circle.getCenter().y  - newcorner));
				circle.setRadius(newcorner / 2);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < circle.getCenter().x)
			{
				double lengthX = circle.getCenter().x - arg0.getX();
				double lengthY = circle.getCenter().y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setUpperLeft(new Point2D.Double(circle.getCenter().x - newcorner, circle.getCenter().y - newcorner));
				circle.setRadius(newcorner / 2);
			}
		}
		Model.instance().setLastShape(circle);
	}
	
	public void handleActiveEllipse(MouseEvent arg0)
	{
		
		Ellipse ellipse = (Ellipse) Model.instance().getLastShape();
		//if the cursor is moving below the upper left corner
		if(arg0.getY() > ellipse.getCenter().y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > ellipse.getCenter().x)
			{
				double lengthX = arg0.getX() - ellipse.getCenter().x;
				double lengthY = arg0.getY() - ellipse.getCenter().y;
				
				ellipse.setUpperLeft(ellipse.getCenter());
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < ellipse.getCenter().x)
			{
				double lengthX = ellipse.getCenter().x - arg0.getX();
				double lengthY = arg0.getY() - ellipse.getCenter().y;
				
				ellipse.setUpperLeft(new Point2D.Double(ellipse.getCenter().x - lengthX, ellipse.getCenter().y));
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < ellipse.getCenter().y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > ellipse.getCenter().x)
			{
				double lengthX = arg0.getX() - ellipse.getCenter().x;
				double lengthY = ellipse.getCenter().y - arg0.getY();
				
				ellipse.setUpperLeft(new Point2D.Double(ellipse.getCenter().x, ellipse.getCenter().y  - lengthY));
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < ellipse.getCenter().x)
			{
				double lengthX = ellipse.getCenter().x - arg0.getX();
				double lengthY = ellipse.getCenter().y - arg0.getY();
				
				ellipse.setUpperLeft(new Point2D.Double(ellipse.getCenter().x - lengthX, ellipse.getCenter().y - lengthY));
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}
		}
		Model.instance().setLastShape(ellipse);
	}
	
	/* Implement Later */
	
	@Override
	public void selectButtonHit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomInButtonHit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomOutButtonHit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hScrollbarChanged(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vScrollbarChanged(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openScene(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggle3DModelDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openImage(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveImage(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleBackgroundDisplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveDrawing(File file) {
		Model.instance().save(file);
	}

	@Override
	public void openDrawing(File file) {
		Model.instance().open(file);
		GUIFunctions.refresh();
	}

	@Override
	public void doDeleteShape() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEdgeDetection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSharpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMedianBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doUniformBlur() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doGrayscale() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMoveForward() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doMoveBackward() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSendToFront() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSendtoBack() {
		// TODO Auto-generated method stub

	}

}
