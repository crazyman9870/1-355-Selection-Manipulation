package cs355.model.drawing;

import java.awt.geom.Point2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model extends CS355Drawing {

	//Use a singleton so that the model can be accessed by the view when repainting
	private static Model _instance;
	
	private Shape.type currentShape;
	private Color selectedColor;
	private int selectedShapeIndex;
	private ArrayList<Shape> shapes;


	//If the model had not been initialized, it will be.
	public static Model instance() {
		if (_instance == null) 
			_instance = new Model();
		return _instance;
	}
	
	public Model() {
		currentShape = Shape.type.NONE;
		selectedColor = Color.GREEN;
		selectedShapeIndex = -1;
		shapes = new ArrayList<Shape>();
	}
	
	//Notifies the observers
	public void notifyObservers() {
		super.notifyObservers();
	}
	
	private void updateView() {
		this.setChanged();
		this.notifyObservers();
	}
	
	public void setColor(Color c) {	
		selectedColor = c;	
		setChanged();
		notifyObservers();
	}
	
	public Color getColor()	{
		return selectedColor;
	}
		
	public int getSelectedShapeIndex() {
		return selectedShapeIndex;
	}

	public void setSelectedShapeIndex(int selectedShapeIndex) {
		this.selectedShapeIndex = selectedShapeIndex;
	}
	
	public int selectShape(Point2D.Double pt, double tolerance) {
		
		for(int i = shapes.size() - 1; i >= 0; i--) {
			Shape s = shapes.get(i);
			if(s.pointInShape(pt, tolerance)) {
				selectedShapeIndex = i;
				updateView();
				return selectedShapeIndex;
			}
		}
		selectedShapeIndex = -1;
		updateView();
		return selectedShapeIndex;
	}
	
	@Override
	public Shape getShape(int index) {
		return shapes.get(index);
	}

	@Override
	public int addShape(Shape s) {
		shapes.add(s);
		System.out.println(shapes.size());
		return shapes.size();
	}

	@Override
	public void deleteShape(int index) {
		shapes.remove(index);
	}

	@Override
	public void moveToFront(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void movetoBack(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveForward(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveBackward(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Shape> getShapes() {
		return shapes;
	}

	@Override
	public List<Shape> getShapesReversed() {
		ArrayList<Shape> copy = new ArrayList<>();
		Collections.reverse(copy);
		return null;
	}

	@Override
	public void setShapes(List<Shape> shapes) {
		this.shapes = (ArrayList<Shape>) shapes;
	}

	public Shape.type getCurrentShape() {
		return currentShape;
	}

	public void setCurrentShape(Shape.type currentMode) {
		this.currentShape = currentMode;
//		System.out.println(currentMode.name());
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(Color selectedColor) {
		this.selectedColor = selectedColor;
	}

	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}
	
	public Shape getLastShape()
	{	return shapes.get(shapes.size() - 1);	}
	
	public void setLastShape(Shape newShape) {	
		shapes.remove(shapes.size() - 1);
		shapes.add(newShape);
	}
	
	public void deleteLastShape() {
		shapes.remove(shapes.size()-1);
	}
	
	public void changeMade() {
		setChanged();
		notifyObservers();
	}
}
