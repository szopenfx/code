package explore3d.behaviors;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import explore3d.scenes.Scene;

/**
 * KeyNavigator class translates keyboard input to motion (also toggles certain behaviors in MouseLooker)
 * @author J.J. Molenaar, J. Dooper
 */
public class KeyNavigator extends Behavior
{
	private TransformGroup _target;
	private Transform3D _t3d = new Transform3D();
	private Scene _scene;
	
	private float _slowspeed = 0.1f;
	private float _normalspeed = 0.2f;
	private float _fastspeed = 0.4f;
	private double _speed = _normalspeed;
	
	/**
	 * Create KeyNavigator for a certain TransformGroup and a certain Scene object
	 * @param target Target TransformGroup
	 * @param scene Target Scene
	 */
	public KeyNavigator(TransformGroup target, Scene scene)
	{
		_target = target;
		_scene = scene;
		_t3d = new Transform3D();
	}
	
	/**
	 * Set trigger conditions
	 * window need to be focused to accept keys (for example when rotate is 
	 * selected the mainwindow is unfocused and wont accept input)
	 */
	public void initialize()
	{
		WakeupCriterion[] criteria = {
				new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED),
				new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED)
		};
		wakeupOn(new WakeupOr(criteria));
	}

	/**
	 * Process stimuli
	 * 
	 * <code><pre>
	 * W/up  move forward
	 * S/dn  move back
	 * A/lf  move left
	 * D/rt  move right
	 * F 	 toggle mouse look
	 * R	 toggle mouse look permanently
	 * </pre></code>
	 * @param criteria Events
	 */
	public void processStimulus(Enumeration criteria)
	{
		WakeupCriterion wakeup;
		AWTEvent[] events;
		
		// for every criterion
		while(criteria.hasMoreElements())
		{
			wakeup = (WakeupCriterion) criteria.nextElement();
			
			// ignore non-AWT events
			if(!(wakeup instanceof WakeupOnAWTEvent))
				continue;
			
			// get AWT events
			events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
			
			// for every AWT event
			for(int i = 0; i < events.length; i++)
			{
				//Progress.report(this, events[i].toString());
				if(events[i].getID() == KeyEvent.KEY_PRESSED)
					processKeyPress(((KeyEvent) events[i]));
			}
					
		}
		
		// re-initialize behavior
		initialize();
	}

	/**
	 * Process key press event
	 * z = slow speed
	 * x = normal speed
	 * c = fast speed
	 * q = up
	 * e = down
	 * since collision detection is active the buttons q and e won't work.
	 * @param event Event parameters, only used for scancode of key
	 */
	private void processKeyPress(KeyEvent event)
	{

		switch(event.getKeyCode())
		{
			case KeyEvent.VK_W:	
			case KeyEvent.VK_UP:
				performMove(new Vector3d(0, 0, -_speed));
				break;
			case KeyEvent.VK_S:	
			case KeyEvent.VK_DOWN:
				performMove(new Vector3d(0, 0, _speed));
				break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				performMove(new Vector3d(-_speed, 0, 0));
				break;
			case KeyEvent.VK_D:	
			case KeyEvent.VK_RIGHT:
				performMove(new Vector3d(_speed, 0, 0));
				break;
			case KeyEvent.VK_Q:	
				performMove(new Vector3d(0, -_speed, 0));
				break;
			case KeyEvent.VK_E:	
				performMove(new Vector3d(0, _speed, 0));
				break;
			case KeyEvent.VK_Z:
				_speed = _slowspeed;
				break;
			case KeyEvent.VK_X:
				_speed = _normalspeed;
				break;
			case KeyEvent.VK_C:
				_speed = _fastspeed;
				break;	
		}
	}

	/**
	 * Move the viewer in a direction
	 * @param direction
	 */
	private void performMove(Vector3d direction)
	{
		if(canMove(direction))
		{
			_target.getTransform(_t3d);
	
			Transform3D move = new Transform3D();
			move.setTranslation(direction);
			
			_t3d.mul(move);
			
			_target.setTransform(_t3d);
		}
	}

	/**
	 * Detect if view location can be moved in a direction
	 * collision detection with picktool. 
	 * @param direction
	 * @return true if the move does not cause a colission
	 */
	private boolean canMove(Vector3d direction) 
	{
		// point object to hold current position
		Point3d current = new Point3d();
		
		// get current position and calculate desired position
		_scene._simpleUniverse.getCanvas().getCenterEyeInImagePlate(current);
		Point3d desired = new Point3d(current.x + direction.x,
									  current.y + direction.y,
									  current.z + direction.z);

		// transform
		Transform3D transform = new Transform3D();
		_scene._simpleUniverse.getCanvas().getImagePlateToVworld(transform);
		transform.transform(current);
		transform.transform(desired);
		
		// pick object
		PickTool picker = new PickTool(_scene._root);
		picker.setShapeSegment(current, desired);
		picker.setMode(PickTool.GEOMETRY);
		
		try
		{
			// try to get object that's pointed to
			PickResult result = picker.pickAny();
			Object thing = result != null ? result.getObject() : null;
			return thing == null;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
