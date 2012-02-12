package explore3d.behaviors;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import explore3d.progress.Progress;
import explore3d.scenes.RoomScene;
import explore3d.scenes.Scene;
import explore3d.shapes.GenericFileShape;
import explore3d.Constants;
import explore3d.DirectoryList;

/**
 * Mouse behavior, mainly for pointing-and-clicking but also for rotating the view
 * @author J.J. Molenaar, J. Dooper
 */
public class MouseLooker extends Behavior 
{
	private TransformGroup _target;
	private Transform3D _t3d;
	private Scene _scene;
	private GenericFileShape _selected = null;
	
	private boolean _isvalid = false;
	private int _prevX;
	private int _prevY;
	
	/**
	 * Construct behavior for a certain TransformGroup and a Scene
	 * @param target Target TransformGroup
	 * @param scene Target Scene
	 */
	public MouseLooker(TransformGroup target, Scene scene)
	{
		_target = target;
		_scene = scene;
		_t3d = new Transform3D();
	}
	
	/**
	 * Set mouse events to respond to
	 */
	public void initialize()
	{
		WakeupCriterion[] criteria = {
			new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED),
			new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED)

		};
		wakeupOn(new WakeupOr(criteria));
	}

	/**
	 * Find out what the mouse event was and process it
	 */
	public void processStimulus(Enumeration criteria)
	{
		WakeupCriterion wakeup;
		AWTEvent[] events;
		
		// for every wake-up criterion
		while(criteria.hasMoreElements())
		{
			// get my object from the fucking iterator and do the typecasting
			wakeup = (WakeupCriterion) criteria.nextElement();
			
			// if it's not an AWT event, ignore it
			if(!(wakeup instanceof WakeupOnAWTEvent))
				continue;

			// for every AWT event, process them
			events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
			for(int i = 0; i < events.length; i++)
			{
				if(events[i].getID() == MouseEvent.MOUSE_MOVED)
					processMouseMove(((MouseEvent) events[i]));
				if(events[i].getID() == MouseEvent.MOUSE_DRAGGED)
					processMouseMove(((MouseEvent) events[i]));
				if(events[i].getID() == MouseEvent.MOUSE_CLICKED)
					processMouseClick(((MouseEvent) events[i]));
				if(events[i].getID() == MouseEvent.MOUSE_PRESSED)
					processMousePress(((MouseEvent) events[i]));
				if(events[i].getID() == MouseEvent.MOUSE_RELEASED)
					processMouseRelease(((MouseEvent) events[i]));
			}
		}
		
		// re-initialize behavior
		initialize();
	}

	/**
	 * right mousebutton release disallows rotating of scene
	 * @param event Mouse Release event
	 */
	private void processMouseRelease(MouseEvent event)
	{
		if(event.getButton() == MouseEvent.BUTTON3)
			Constants.allowRotateToggle();
	}

	
	/**
	 * right mousebutton click allows rotating of scene
	 * @param event Mouse Press event
	 */
	private void processMousePress(MouseEvent event)
	{
		if(event.getButton() == MouseEvent.BUTTON3)
			Constants.allowRotateToggle();
	}

	/**
	 * Process mouse click; that means changing the scene if a file was already selected
	 * @param event MouseClick event, mostly ignored
	 */
	private void processMouseClick(MouseEvent event)
	{
		if(event.getButton() == MouseEvent.BUTTON1)
			if(_selected != null)
			{
				File file = _selected.getFile();
				Progress.report(this, "Clicked " + file);
				boolean refresh = DirectoryList.instance.navigate(file);
				if(refresh)
					((RoomScene) _scene).refreshScene(true);
				Constants.MAIN_FRAME.displayDirectory(_selected);
			}
	}

	/**
	 * Rotate camera in response to mouse move if mouse rotation was toggled by the keyboard behavior
	 * @param event Event parameters
	 */
	private void processMouseMove(MouseEvent event)
	{
		// get mouse position
		int x = event.getX();
		int y = event.getY();
		
		// if a mouse position was recorded before
		if(_isvalid)
		{
			// calculate difference
			int diffX = x - _prevX;
			int diffY = y - _prevY;

			// rotate view as many degrees as the mouse was moved in pixels
			if((diffX != 0 || diffY != 0) && Constants.allowRotate())
				rotateOverY(-diffX);
		}

		// pick selected item
		selectItem(x, y);
		
		// record current mouse position
		_prevX = x;
		_prevY = y;
		_isvalid = true;
	}

	/**
	 * Cast a PickRay from the x and y coordinates and store the selected item in _selected
	 * @param x
	 * @param y
	 */
	private void selectItem(int x, int y) 
	{
		// eye and mouse positions
		Point3d eye = new Point3d();
		Point3d mouse = new Point3d();
		Transform3D motion = new Transform3D();
		
		// fill eye and mouse with values
		_scene._canvas3D.getCenterEyeInImagePlate(eye);
		_scene._canvas3D.getPixelLocationInImagePlate(x, y, mouse);
		_scene._canvas3D.getImagePlateToVworld(motion);

		// make transform from eye and mouse
		motion.transform(eye);
		motion.transform(mouse);
		
		// change it into a direction from eye
		Vector3d direction = new Vector3d(mouse);
		direction.sub(eye);
		
		// cast pickray from eye in direction
		PickRay ray = new PickRay(eye, direction);
		
		// pick all objects in the pickray
		SceneGraphPath[] path = _scene._root.pickAll(ray);
		boolean shapefound = false;
		
		// if objects were found
		if(path != null)
		{
			// for every item in path
			for(int i = 0; i < path.length; i++)
			{
				// if a GenericFileShape was picked
				if(path[i].getObject() instanceof GenericFileShape)
				{
					GenericFileShape gs = (GenericFileShape) path[i].getObject();
					
					boolean newshape = _selected != gs;
					
					// if an object was selected, deselect it
					if(_selected != null && newshape)
						_selected.setSelected(false);
					
					// set new selected object
					_selected = gs;
					if(newshape)
						_selected.setSelected(true);
					
					// report shape is found
					shapefound = true;
				}
			}
		}
		
		// if no shape was found, deselect selected shape and forget about it too
		if(!shapefound && _selected != null)
		{
			_selected.setSelected(false);
			_selected = null;
		}
	}

	/**
	 * Rotate camera over Y axis (i.e. look left and right)
	 * @param diff Movement in degrees
	 */
	private void rotateOverY(int diff)
	{
		Transform3D rotation = new Transform3D();
		_target.getTransform(_t3d);
		rotation.rotY(((float) diff / 180 * Math.PI));
		_t3d.mul(rotation);
		_target.setTransform(_t3d);
	}
}
