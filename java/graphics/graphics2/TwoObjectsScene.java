package graphics2;

import graphics.Scene;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Creëer twee virtuele objecten met behulp van verschillende primitieven. Zorg ervoor 
 * dat deze objecten elkaar niet raken of snijden.
 * @author J.J. Molenaar
 */
public class TwoObjectsScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creëer twee virtuele objecten met behulp van verschillende primitieven.
	 * Zorg ervoor dat deze objecten elkaar niet raken of snijden.
	 * 
	 * <pre><code>
	 * bg ---- cyl_tg(cyl_trans) -- cyl -- cyl_app
	 *    '--- sph_tg(sph_trans) -- sph -- sph_app
	 * </code></pre>
	 * 
	 * @return Een BranchGroup object
	 */
	protected BranchGroup createScene()
	{
		BranchGroup bg = new BranchGroup();

		// create cylinder
		Appearance cyl_app = new Appearance();
		Cylinder cyl = new Cylinder(0.5f, 0.5f);
		Transform3D cyl_trans = new Transform3D();

		cyl_app.setColoringAttributes(new ColoringAttributes(1.0f, 0.0f, 0.0f,
				ColoringAttributes.SHADE_FLAT));
		cyl.setAppearance(cyl_app);
		cyl_trans.setTranslation(new Vector3f(1.0f, 0.0f, -2.0f));

		// create sphere
		Appearance sph_app = new Appearance();
		Sphere sph = new Sphere(0.5f);
		Transform3D sph_trans = new Transform3D();

		sph_app.setColoringAttributes(new ColoringAttributes(0.0f, 1.0f, 0.0f,
				ColoringAttributes.SHADE_FLAT));
		sph.setAppearance(sph_app);
		sph_trans.setTranslation(new Vector3f(-1.0f, 0.0f, -2.0f));

		// create transform groups
		TransformGroup cyl_tg = new TransformGroup(cyl_trans);
		TransformGroup sph_tg = new TransformGroup(sph_trans);

		bg.addChild(cyl_tg);
		bg.addChild(sph_tg);

		cyl_tg.addChild(cyl);
		sph_tg.addChild(sph);

		bg.compile();

		return bg;
	}
}
