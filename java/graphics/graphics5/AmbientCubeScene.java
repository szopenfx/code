package graphics5;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Box;

import graphics.Scene;

/**
 * Creëer een visueel object met behulp van één van de geometrische primitieven, 
 * laat deze roteren rond de x-as en zorg ervoor dat de normaalvectoren van het object 
 * berekend worden. Geef het object een uiterlijk met behulp van de Material klasse en 
 * creëer een ‘ambient’ lichtbron die het object belicht. 
 * @author J.J. Molenaar
 */
public class AmbientCubeScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * <code><pre>
	 * bg --- tgrot --- b -- app -- m
	 *    |         '-- rot(a, tgrot) -- bb
	 *    '-- ambient -- bb
	 * </pre></code>
	 */
	protected BranchGroup createScene()
	{
		BoundingBox bb = new BoundingBox();
		
		AmbientLight ambient = new AmbientLight();
		ambient.setInfluencingBounds(bb);
		
		Material m = new Material();
		
		Appearance app = new Appearance();
		app.setMaterial(m);
		
		Box b = new Box(0.3f, 0.3f, 0.3f, Box.GENERATE_NORMALS, app);
		
		TransformGroup tgrot = new TransformGroup();
		tgrot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Alpha a = new Alpha(-1, 3000);
		
		RotationInterpolator rot = new RotationInterpolator(a, tgrot);
		rot.setSchedulingBounds(bb);
		
		tgrot.addChild(b);
		tgrot.addChild(rot);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(tgrot);
		bg.addChild(ambient);
		bg.compile();
		return bg;
	}
}
