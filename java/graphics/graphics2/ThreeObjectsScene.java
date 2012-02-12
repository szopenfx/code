package graphics2;

import graphics.Scene;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;

/**
 * Creëer drie verschillende geometrische primitieven die elkaar niet raken of 
 * snijden en geef de drie objecten verschillende kleuren met behulp van de 
 * ColoringAttributes klasse. Laat van het eerste object gecreëerd in opdracht 1 
 * alleen de punten renderen, van het tweede het draadmodel en het derde gevuld.
 * @author J.J. Molenaar
 */
public class ThreeObjectsScene extends Scene
{
	private static final long serialVersionUID = 1L;

	/**
	 * <pre><code>
	 * bg -- tg ---- tg_1(tr_1) -- box_1 --- a_1
	 *          |                        '-- pa_1
	 *          |--- tg_2(tr_2) -- box_2 --- a_2
	 *          |                        '-- pa_2
	 *          '--- tg_3(tr_3) -- box_3 --- a_3
	 * </code></pre>
	 * 
	 * @return A branchgroup scene
	 */
	protected BranchGroup createScene()
	{
		BranchGroup bg = new BranchGroup();
		TransformGroup tg = new TransformGroup();
		
		// create box 1
		Appearance a_1 = new Appearance();
		PolygonAttributes pa_1 = new PolygonAttributes();
		Box box_1 = new Box();
		Transform3D tr_1 = new Transform3D();
		
		tr_1.setTranslation(new Vector3d(-4, 0, -10));
		a_1.setColoringAttributes(new ColoringAttributes(1.0f, 0.0f, 0.0f, ColoringAttributes.NICEST));
		pa_1.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		a_1.setPolygonAttributes(pa_1);
		box_1.setAppearance(a_1);

		TransformGroup tg_1 = new TransformGroup(tr_1);
		tg_1.addChild(box_1);
		
		// create box 2
		Appearance a_2 = new Appearance();
		PolygonAttributes pa_2 = new PolygonAttributes();
		Box box_2 = new Box();
		Transform3D tr_2 = new Transform3D();
		tr_2.setTranslation(new Vector3d(0, 0, -10));
		a_2.setColoringAttributes(new ColoringAttributes(0.0f, 1.0f, 0.0f, ColoringAttributes.NICEST));
		pa_2.setPolygonMode(PolygonAttributes.POLYGON_POINT);
		a_2.setPolygonAttributes(pa_2);
		box_2.setAppearance(a_2);
		TransformGroup tg_2 = new TransformGroup(tr_2);
		tg_2.addChild(box_2);
		
		// create box 3
		Appearance a_3 = new Appearance();
		Box box_3 = new Box();
		Transform3D tr_3 = new Transform3D();
		tr_3.setTranslation(new Vector3d(4, 0, -10));
		a_3.setColoringAttributes(new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.NICEST));
		box_3.setAppearance(a_3);
		TransformGroup tg_3 = new TransformGroup(tr_3);
		tg_3.addChild(box_3);
		
		// add transform groups
		tg.addChild(tg_1);
		tg.addChild(tg_2);
		tg.addChild(tg_3);
		
		bg.addChild(tg);
		bg.compile();
		
		return bg;
	}
}
