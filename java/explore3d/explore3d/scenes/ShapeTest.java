package explore3d.scenes;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

/**
 * Class for testing a Shape3D object
 * @author J.J. Molenaar, J. Dooper
 */
public class ShapeTest extends Scene
{
	/**
	 * Make Eclipse shut up
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a shape test for a particular shape instance
	 * @param shape Object to display
	 */
	public ShapeTest(Shape3D shape)
	{
		super(shape);
	}
	
	/**
	 * <code><pre>
	 * bg ---- tg ---- s3d [RoomShape]
	 *    '--- knb(tgvp)
	 * </pre></code>
	 */
	protected void createScene()
	{
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		// roteren
		Alpha rotatorAlpha = new Alpha(-1, 4000);
		 
		Transform3D YAs = new Transform3D();
		YAs.rotX(Math.PI / 4.0f);
		
		TransformGroup RotatorTG = new TransformGroup();
		
		RotationInterpolator CylRotator = new RotationInterpolator(
				rotatorAlpha, RotatorTG, YAs, 0.0f, (float) Math.PI * 2.0f);
		CylRotator.setSchedulingBounds(new BoundingSphere());
		
		RotatorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		RotatorTG.addChild(CylRotator);
		RotatorTG.addChild((Shape3D) _data);
		tg.addChild(RotatorTG);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(tg);
		bg.compile();
		_root = bg;
	}
}
