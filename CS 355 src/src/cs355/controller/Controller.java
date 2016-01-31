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

	private int currentShapeIndex = -1;
	private Point2D.Double mouseDragStart = null;
	private ArrayList<Point2D> triangleCoordinates = new ArrayList<>();
	private Mode controllerMode = Mode.NONE;
	
	public enum Mode {
		SHAPE, SELECT, ZOOM_IN, ZOOM_OUT, NONE
	}
	
	/* Mouse Events */
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(controllerMode == Mode.SELECT) {
			this.currentShapeIndex = Model.instance().selectShape(new Point2D.Double(arg0.getX(), arg0.getY()), 5);
		}
	}
	
	private double calculateCenterTriangle(double coord1, double coord2, double coord3) {
		return ((coord1 + coord2 + coord3) / 3);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
		if(controllerMode == Mode.SHAPE) {
				
			if(Model.instance().getCurrentShape() == Shape.type.TRIANGLE) {
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
			
				switch(Model.instance().getCurrentShape()) {
				case LINE:
					Model.instance().addShape(new Line(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()), new Point2D.Double(arg0.getX(), arg0.getY())));
					break;
				case SQUARE: 
					Model.instance().addShape(new Square(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()), 0));
					this.mouseDragStart = new Point2D.Double(arg0.getX(), arg0.getY());
					break;
				case RECTANGLE: 
					Model.instance().addShape(new Rectangle(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()),0, 0));
					this.mouseDragStart = new Point2D.Double(arg0.getX(), arg0.getY());
					break;
				case CIRCLE: 
					Model.instance().addShape(new Circle(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()),0));
					this.mouseDragStart = new Point2D.Double(arg0.getX(), arg0.getY());
					break;
				case ELLIPSE: 
					Model.instance().addShape(new Ellipse(Model.instance().getColor(), new Point2D.Double(arg0.getX(), arg0.getY()),0, 0));
					this.mouseDragStart = new Point2D.Double(arg0.getX(), arg0.getY());
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
		if(controllerMode == Mode.SHAPE && Model.instance().getCurrentShape() != Shape.type.TRIANGLE) {
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
		// TODO Auto-generated method stub
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
		switchStates(Mode.SHAPE);
	}

	@Override
	public void squareButtonHit() {
		Model.instance().setCurrentShape(Shape.type.SQUARE);
		switchStates(Mode.SHAPE);
	}

	@Override
	public void rectangleButtonHit() {
		Model.instance().setCurrentShape(Shape.type.RECTANGLE);
		switchStates(Mode.SHAPE);
	}

	@Override
	public void circleButtonHit() {
		Model.instance().setCurrentShape(Shape.type.CIRCLE);
		switchStates(Mode.SHAPE);
	}

	@Override
	public void ellipseButtonHit() {
		Model.instance().setCurrentShape(Shape.type.ELLIPSE);
		switchStates(Mode.SHAPE);
	}

	@Override
	public void triangleButtonHit() {
		Model.instance().setCurrentShape(Shape.type.TRIANGLE);
		switchStates(Mode.SHAPE);
	}
	
	@Override
	public void selectButtonHit() {
		// TODO Auto-generated method stub
		switchStates(Mode.SELECT);
	}

	@Override
	public void zoomInButtonHit() {
		// TODO Auto-generated method stub
		switchStates(Mode.ZOOM_IN);
	}

	@Override
	public void zoomOutButtonHit() {
		// TODO Auto-generated method stub
		switchStates(Mode.ZOOM_OUT);
	}
	
	public void switchStates(Mode m) {
		this.controllerMode = m;
		this.currentShapeIndex = -1;
		this.mouseDragStart = null;
		this.triangleCoordinates.clear();
		Model.instance().setSelectedShapeIndex(-1);
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
		if(arg0.getY() > mouseDragStart.y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = arg0.getY() - mouseDragStart.y;
				double newcorner = Math.min(lengthX, lengthY);
				
				square.setCenter(new Point2D.Double(mouseDragStart.x + newcorner/2, mouseDragStart.y + newcorner/2));
				square.setSize(newcorner);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = arg0.getY() - mouseDragStart.y;
				double newcorner = Math.min(lengthX, lengthY);
				
				square.setCenter(new Point2D.Double(mouseDragStart.x - newcorner/2, mouseDragStart.y + newcorner/2));
				square.setSize(newcorner);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < mouseDragStart.y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = mouseDragStart.y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				//change to set center of some sort 
				square.setCenter(new Point2D.Double(mouseDragStart.x + newcorner/2, mouseDragStart.y  - newcorner/2));
				square.setSize(newcorner);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = mouseDragStart.y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				square.setCenter(new Point2D.Double(mouseDragStart.x - newcorner/2, mouseDragStart.y - newcorner/2));
				square.setSize(newcorner);
			}
		}
		Model.instance().setLastShape(square);
	}
	
	public void handleActiveRectangle(MouseEvent arg0)
	{
		
		Rectangle rectangle = (Rectangle) Model.instance().getLastShape();
		//if the cursor is moving below the upper left corner
		if(arg0.getY() > mouseDragStart.y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = arg0.getY() - mouseDragStart.y;
				
				rectangle.setCenter(new Point2D.Double(mouseDragStart.x + lengthX/2, mouseDragStart.y + lengthY/2));
				rectangle.setHeight(lengthY);
				rectangle.setWidth(lengthX);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = arg0.getY() - mouseDragStart.y;
				
				rectangle.setCenter(new Point2D.Double(mouseDragStart.x - lengthX/2, mouseDragStart.y + lengthY/2));
				rectangle.setHeight(lengthY);
				rectangle.setWidth(lengthX);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < mouseDragStart.y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = mouseDragStart.y - arg0.getY();
				
				rectangle.setCenter(new Point2D.Double(mouseDragStart.x + lengthX/2, mouseDragStart.y  - lengthY/2));
				rectangle.setHeight(lengthY);
				rectangle.setWidth(lengthX);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = mouseDragStart.y - arg0.getY();
				
				rectangle.setCenter(new Point2D.Double(mouseDragStart.x - lengthX/2, mouseDragStart.y - lengthY/2));
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
		if(arg0.getY() > mouseDragStart.y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = arg0.getY() - mouseDragStart.y;
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setCenter(new Point2D.Double(mouseDragStart.x + newcorner/2, mouseDragStart.y + newcorner/2));
				circle.setRadius(newcorner / 2);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = arg0.getY() - mouseDragStart.y;
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setCenter(new Point2D.Double(mouseDragStart.x - newcorner/2, mouseDragStart.y + newcorner/2));
				circle.setRadius(newcorner / 2);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < mouseDragStart.y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = mouseDragStart.y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setCenter(new Point2D.Double(mouseDragStart.x + newcorner/2, mouseDragStart.y  - newcorner/2));
				circle.setRadius(newcorner / 2);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = mouseDragStart.y - arg0.getY();
				double newcorner = Math.min(lengthX, lengthY);
				
				circle.setCenter(new Point2D.Double(mouseDragStart.x - newcorner/2, mouseDragStart.y - newcorner/2));
				circle.setRadius(newcorner / 2);
			}
		}
		Model.instance().setLastShape(circle);
	}
	
	public void handleActiveEllipse(MouseEvent arg0)
	{
		
		Ellipse ellipse = (Ellipse) Model.instance().getLastShape();
		//if the cursor is moving below the upper left corner
		if(arg0.getY() > mouseDragStart.y)
		{
			//if the cursor is moving to the bottom right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = arg0.getY() - mouseDragStart.y;
				
				ellipse.setCenter(new Point2D.Double(mouseDragStart.x + lengthX/2, mouseDragStart.y + lengthY/2));
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}

			//if the cursor is moving to the bottom left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = arg0.getY() - mouseDragStart.y;
				
				ellipse.setCenter(new Point2D.Double(mouseDragStart.x - lengthX/2, mouseDragStart.y + lengthY/2));
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}
		}

		//if the cursor is moving above the upper left corner
		if(arg0.getY() < mouseDragStart.y)
		{
			//if the cursor is moving to the upper right quad
			if(arg0.getX() > mouseDragStart.x)
			{
				double lengthX = arg0.getX() - mouseDragStart.x;
				double lengthY = mouseDragStart.y - arg0.getY();
				
				ellipse.setCenter(new Point2D.Double(mouseDragStart.x + lengthX/2, mouseDragStart.y  - lengthY/2));
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}

			//if the cursor is moving to the upper left quad
			if(arg0.getX() < mouseDragStart.x)
			{
				double lengthX = mouseDragStart.x - arg0.getX();
				double lengthY = mouseDragStart.y - arg0.getY();
				
				ellipse.setCenter(new Point2D.Double(mouseDragStart.x - lengthX/2, mouseDragStart.y - lengthY/2));
				ellipse.setWidth(lengthX);
				ellipse.setHeight(lengthY);
			}
		}
		Model.instance().setLastShape(ellipse);
	}
	
	/* Implement Later */
	
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
