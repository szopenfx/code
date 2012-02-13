package graphics5;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.Cylinder;

import graphics.Scene;

/**
 * Schrijf een methode met de naam ‘maakVisueelObject’ dat een visueel object, 
 * bestaande uit meerdere geometrische primitieven met verschillende kleuren, op 
 * een variabele positie creëert. Voeg met behulp van deze methode steeds vier 
 * objecten toe aan de virtuele wereld op 10, 20 en 30 eenheden vanaf de oorsprong. 
 * Voeg tevens een KeyNavigatorBehavior object toe waarmee genavigeerd kan worden 
 * door de virtuele wereld.
 * 
 * @author J.J. Molenaar
 */
public class TranslatedVisualObjectScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * Create popsicle. Why not?
	 * 
	 * <create><pre>
	 * tg ---- tg_stick ---- cyl_stick
	 *    |             '--- t3d_stick
	 *    |--- tg_candy ---- cyl_candy
	 *    |             '--- t3d_candy
	 *    '--- t3d_pos
	 * </pre></create>
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @return TransformGroup
	 */
	protected TransformGroup createPopsicle(float x, float y, float z)
	{
		// create stick
		ColoringAttributes ca_stick = new ColoringAttributes();
		ca_stick.setColor(0.75f, 0.5f, 0.0f);
		
		Appearance app_stick = new Appearance();
		app_stick.setColoringAttributes(ca_stick);
		
		Cylinder cyl_stick = new Cylinder(0.1f, 2.0f);
		cyl_stick.setAppearance(app_stick);

		Transform3D t3d_stick = new Transform3D();
		t3d_stick.setTranslation(new Vector3f(0.0f, -1.0f, 0.0f));
		
		TransformGroup tg_stick = new TransformGroup(t3d_stick);
		tg_stick.addChild(cyl_stick);
		
		// create candy
		ColoringAttributes ca_candy = new ColoringAttributes();
		ca_candy.setColor(0.0f, 1.0f, 0.0f);
		
		Appearance app_candy = new Appearance();
		app_candy.setColoringAttributes(ca_candy);
		
		Cylinder cyl_candy = new Cylinder(0.5f, 0.2f);
		cyl_candy.setAppearance(app_candy);
		
		Transform3D t3d_candy = new Transform3D();
		t3d_candy.setTranslation(new Vector3f(0.0f, 0.5f, 0.0f));
		t3d_candy.rotX(HALF_PI);
		
		TransformGroup tg_candy = new TransformGroup(t3d_candy);
		tg_candy.addChild(cyl_candy);
		
		// create position transform for shape
		Transform3D t3d_pos = new Transform3D();
		t3d_pos.setTranslation(new Vector3f(x, y, z));
		
		// join primitives into transform group
		TransformGroup tg = new TransformGroup(t3d_pos);
		tg.addChild(tg_stick);
		tg.addChild(tg_candy);
		return tg;
	}
	
	/**
	 * @return BranchGroup
	 */
	protected BranchGroup createScene()
	{
		BranchGroup bg = new BranchGroup();

		TransformGroup tgvp = simpleUniverse.getViewingPlatform().getViewPlatformTransform();
		KeyNavigatorBehavior knb = new KeyNavigatorBehavior(tgvp);
		knb.setSchedulingBounds(new BoundingBox());
		bg.addChild(knb);
		
		for(float pos = 5.0f; pos < 20.0f; pos += 5.0f)
		{
			bg.addChild(createPopsicle(pos, 0, 0));
			bg.addChild(createPopsicle(-pos, 0, 0));
			bg.addChild(createPopsicle(0, 0, pos));
			bg.addChild(createPopsicle(0, 0, -pos));
		}
		
		bg.compile();
		return bg;
	}

}
